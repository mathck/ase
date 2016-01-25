package at.tuwien.ase.services.impl;

import at.tuwien.ase.controller.exceptions.JavaxErrorHandler;
import at.tuwien.ase.controller.exceptions.ValidationException;
import at.tuwien.ase.dao.DslTemplateDAO;
import at.tuwien.ase.model.JsonStringWrapper;
import at.tuwien.ase.model.DslTemplate;
import at.tuwien.ase.model.javax.TaskElement;
import at.tuwien.ase.model.javax.Template;
import at.tuwien.ase.services.DslTemplateService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintViolation;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.util.regex.Pattern;

/**
 * Created by DanielHofer on 21.12.2015.
 */

@Service
public class DslTemplateServiceImpl implements DslTemplateService{

    @Autowired
    private DslTemplateDAO dslTemplateDAO;

    @Autowired
    private javax.validation.Validator validator;

    private JavaxErrorHandler errorHandler;
    private SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    private Schema schema;
    private static final String TEMPLATE_XSD_FILE_NAME = new String("dsl/template.xsd");
    private static final String TASK_ITEM_STRING = new String("taskElement");
    private static final String TASK_ITEM_REGEX = new String("\\{"+TASK_ITEM_STRING+":[0-9]+\\}");

    private static final Logger logger = LogManager.getLogger(DslTemplateServiceImpl.class);


    public JsonStringWrapper writeDslTemplate(DslTemplate template, String mode) throws Exception {
        int id;

        logger.debug("post new dsl template");

        if (!mode.trim().equals("validate") && !mode.trim().equals("create")){
            throw new ValidationException("mode not supported. Supported modes are: create, validate");
        }

        //Validate template Json
        Set<ConstraintViolation<DslTemplate>> constraintViolationsSubtask = validator.validate(template);
        if (!constraintViolationsSubtask.isEmpty()){
            Iterator<ConstraintViolation<DslTemplate>> flavoursIter = constraintViolationsSubtask.iterator();
            String validationError = new String("");

            while (flavoursIter.hasNext()){
                ConstraintViolation<DslTemplate> violation =  flavoursIter.next();
                validationError += violation.getPropertyPath()+": "+violation.getMessage()+"\n";
            }

            throw new ValidationException(validationError);
        }

        //unmarshal and validate xml template
        unmarshalTemplateXml(template);

        if (mode.trim().equals("create")) {
            //set id and creationDate
            id = dslTemplateDAO.getNewID();
            template.setId(id);
            template.setCreationDate(new Date());

            //insert template to db
            dslTemplateDAO.insertDslTemplate(template);

            return new JsonStringWrapper(id);
        }

        return new JsonStringWrapper(-1);

    }

    public void deleteDslTemplateByID(int tID) throws DataAccessException{
        logger.debug("delete dsl template with id="+tID);
        dslTemplateDAO.removeDslTemplateByID(tID);
    }

    public void updateDslTemplateById(DslTemplate template, int tID)  throws Exception {

        logger.debug("update dsl template with id="+tID);

        //Validate template Json
        Set<ConstraintViolation<DslTemplate>> constraintViolationsSubtask = validator.validate(template);
        if (!constraintViolationsSubtask.isEmpty()){
            Iterator<ConstraintViolation<DslTemplate>> flavoursIter = constraintViolationsSubtask.iterator();
            String validationError = new String("");

            while (flavoursIter.hasNext()){
                ConstraintViolation<DslTemplate> violation =  flavoursIter.next();
                validationError += violation.getPropertyPath()+": "+violation.getMessage()+"\n";
            }

            throw new ValidationException(validationError);
        }

        //unmarshal and validate xml template
        unmarshalTemplateXml(template);

        dslTemplateDAO.alterDslTemplateByID(template, tID);
    }

    public DslTemplate getByID(int tID) throws DataAccessException{
        logger.debug("get dsl template with id="+tID);
        return dslTemplateDAO.findByID(tID);
    }

    public LinkedList<DslTemplate> getAllDslTemplates() throws DataAccessException{
        logger.debug("get all dsl templates");
        return dslTemplateDAO.loadAll();
    }

    public LinkedList<DslTemplate> getAllDslTemplatesByUser(String uID) throws DataAccessException{
        logger.debug("get all dsl templates from user="+uID);
        return dslTemplateDAO.loadAllByUser(uID);
    }

    public Template unmarshalTemplateXml(DslTemplate dslTemplate) throws Exception{
        String taskBody;
        List<TaskElement> taskElementList;

        try {
            errorHandler = new JavaxErrorHandler();

            //get xsd file from src/main/resource
            ClassLoader classLoader = getClass().getClassLoader();
            schema = sf.newSchema(new File(classLoader.getResource(TEMPLATE_XSD_FILE_NAME).getFile()));

            JAXBContext jc = JAXBContext.newInstance(Template.class);

            Unmarshaller unmarshaller = jc.createUnmarshaller();
            unmarshaller.setSchema(schema);
            //set error handler for javax errors
            unmarshaller.setEventHandler(errorHandler);

            //read xml from template object and unmarshal
            StringReader reader = new StringReader(dslTemplate.getSyntax());
            Template template = (Template) unmarshaller.unmarshal(reader);

            //xsd validation
            validate(dslTemplate.getSyntax());

            taskBody = convertTaskBodyToString(template.getTaskBody().getContent());

            //validate: on a git hook task no task elements may be defined
            if (template.getIdentifier().isGithook() == true && template.getTaskElements() != null && template.getTaskElements().getTaskElement().size() > 0) {
                throw new ValidationException("No task elements allowed on a git hook template");
            }

            //validate: on a non git hook task at least one task elements must be defined
            if (template.getIdentifier().isGithook() == false && (template.getTaskElements() == null && template.getTaskElements().getTaskElement().size() == 0)) {
                throw new ValidationException("At least one task element must be defined on a 'non git hook' template");
            }

            //task element validation
            if (template.getTaskElements() != null) {

                taskElementList = template.getTaskElements().getTaskElement();

                //validate taskElements via regex
                LinkedList<Integer> taskElementsInTaskBodyList = new LinkedList<Integer>();
                Matcher matcher = Pattern.compile(TASK_ITEM_REGEX).matcher(taskBody);

                while (matcher.find()) {
                    //get taskelement id
                    taskElementsInTaskBodyList.add(Integer.valueOf(taskBody.substring(matcher.start() + TASK_ITEM_STRING.length() + 2, matcher.end() - 1)));
                }

                //check for duplicate ids
                boolean idDuplicate = false;
                int currentId;
                for (int i = 0; i < taskElementsInTaskBodyList.size(); i++) {

                    currentId = taskElementsInTaskBodyList.get(i);

                    idDuplicate = false;
                    for (int j = i + 1; j < taskElementsInTaskBodyList.size(); j++) {

                        if (currentId == taskElementsInTaskBodyList.get(j)) {
                            idDuplicate = true;
                        }

                    }

                    if (idDuplicate) {
                        throw new ValidationException("Duplicate task elements id found: " + currentId);
                    }

                }

                //compare count of task elements in body and in task element list
                if (taskElementsInTaskBodyList.size() != taskElementList.size()) {

                    if (taskElementsInTaskBodyList.size() < taskElementList.size()) {
                        throw new ValidationException("Not all defined task elements are used in task body");
                    }

                    if (taskElementsInTaskBodyList.size() > taskElementList.size()) {
                        throw new ValidationException("Not all task elements in task body are defined as task elements");
                    }

                }

                //check for each task element in taskBody: Is it defined under <taskElements> ?
                boolean idFound;

                for (int i = 0; i < taskElementsInTaskBodyList.size(); i++) {

                    idFound = false;
                    for (int j = 0; j < taskElementList.size(); j++) {

                        if (taskElementList.get(j).getId().intValue() == taskElementsInTaskBodyList.get(i)) {
                            idFound = true;
                        }
                    }

                    //taskElement in taskBody not found under taskElements --> throw exception
                    if (!idFound) {
                        throw new ValidationException("taskElement " + taskElementsInTaskBodyList.get(i) + " in taskBody is not specified under taskElements");
                    }
                }


                TaskElement taskElement;
                for (int i = 0; i < taskElementList.size(); i++) {

                    taskElement = taskElementList.get(i);

                    if (taskElement.getType().value().trim().equals("checkbox")) {
                        if (!taskElement.getStatus().trim().toLowerCase().equals("checked") && !taskElement.getStatus().trim().toLowerCase().equals("unchecked")) {
                            throw new ValidationException("Wrong status for taskItem with id " + taskElement.getId() + ". Allowed: checked or unchecked");
                        }
                    }

                    if (taskElement.getType().value().trim().equals("slider")) {
                        if (!taskElement.getValue().trim().toLowerCase().contains("|" + taskElement.getStatus().trim().toLowerCase())
                                && !taskElement.getValue().trim().toLowerCase().contains("|" + taskElement.getStatus().trim().toLowerCase() + "|")
                                && !taskElement.getValue().trim().toLowerCase().contains(taskElement.getStatus().trim().toLowerCase() + "|")
                                ) {
                            throw new ValidationException("Wrong status for taskItem with id " + taskElement.getId());
                        }
                    }

                    //solution must be present
                    if (!taskElement.getType().value().toLowerCase().equals("image")
                            && !taskElement.getType().value().trim().toLowerCase().equals("file")) {

                        if (taskElement.getSolution() != null && taskElement.getSolution().length() > 1) {

                            if (taskElement.getType().value().trim().equals("checkbox")) {
                                if (!taskElement.getSolution().trim().toLowerCase().equals("checked") && !taskElement.getSolution().trim().toLowerCase().equals("unchecked")) {
                                    throw new ValidationException("Wrong solution for taskItem with id " + taskElement.getId() + ". Allowed: checked or unchecked");
                                }
                            }

                            if (taskElement.getType().value().trim().equals("slider")) {
                                if (!taskElement.getValue().trim().toLowerCase().contains("|" + taskElement.getSolution().trim().toLowerCase())
                                        && !taskElement.getValue().trim().toLowerCase().contains("|" + taskElement.getSolution().trim().toLowerCase() + "|")
                                        && !taskElement.getValue().trim().toLowerCase().contains(taskElement.getSolution().trim().toLowerCase() + "|")
                                        ) {
                                    throw new ValidationException("Wrong solution for taskItem with id " + taskElement.getId());
                                }
                            }

                        } else {
                            throw new ValidationException("Solution must be present for task element: " + taskElement.getId());
                        }

                    }

                }
            }

            return template;

        } catch (UnmarshalException e) {

            //get list of validation errors and throw exception
            if (errorHandler.getErrors() == null || errorHandler.getErrors().equals("")) {
                throw new Exception("Unknow error: " + e.getMessage());
            }else{e.printStackTrace();
                throw new Exception(errorHandler.getErrors());
            }

        }

    }

    public String convertTaskBodyToString(List<Serializable> content) throws  Exception {

        String returnString = new String("");
        Object currentContent;

        for (int i = 0; i < content.size(); i++){

            currentContent = content.get(i);

            if (currentContent instanceof JAXBElement){
                returnString += "<"+((JAXBElement) currentContent).getName()+">" +
                        ((JAXBElement) currentContent).getValue() +
                        "</"+((JAXBElement) currentContent).getName()+">";
            }
            if (currentContent instanceof String){
                returnString += currentContent ;
            }

        }

        return returnString;

    }


    public void validate(String xml) throws ValidationException{

        File xsdFile = null;
        ClassLoader classLoader = getClass().getClassLoader();

        try {
            xsdFile =  new File(classLoader.getResource(TEMPLATE_XSD_FILE_NAME).getFile());
            SchemaFactory factory =
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(xsdFile));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));

        }catch(Exception ex){
            throw new ValidationException(ex.getMessage());
        }
    }



}
