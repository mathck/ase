package at.tuwien.ase.services.impl;

import at.tuwien.ase.controller.exceptions.JavaxErrorHandler;
import at.tuwien.ase.dao.DslTemplateDAO;
import at.tuwien.ase.model.JsonStringWrapper;
import at.tuwien.ase.model.DslTemplate;
import at.tuwien.ase.model.javax.Template;
import at.tuwien.ase.services.DslTemplateService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.Serializable;
import java.io.StringReader;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by DanielHofer on 21.12.2015.
 */

@Service
public class DslTemplateServiceImpl implements DslTemplateService{

    @Autowired
    private DslTemplateDAO dslTemplateDAO;

    private JavaxErrorHandler errorHandler;
    private SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    private Schema schema;
    private static final String TEMPLATE_XSD_FILE_NAME = "dsl/template.xsd";

    private static final Logger logger = LogManager.getLogger(DslTemplateServiceImpl.class);


    public JsonStringWrapper writeDslTemplate(DslTemplate template) throws Exception {
        int id;

        logger.debug("post new dsl template");

        //unmarshal xml template
        unmarshalTemplateXml(template);


        //set id and creationDate
        id = dslTemplateDAO.getNewID();
        template.setId(id);
        template.setCreationDate(new Date());

        //insert template to db
        dslTemplateDAO.insertDslTemplate(template);

        return new JsonStringWrapper(id);

    }

    public void deleteDslTemplateByID(int tID) {
        logger.debug("delete dsl template with id="+tID);
        dslTemplateDAO.removeDslTemplateByID(tID);
    }

    public DslTemplate getByID(int tID) {
        logger.debug("get dsl template with id="+tID);
        return dslTemplateDAO.findByID(tID);
    }

    public LinkedList<DslTemplate> getAllDslTemplates() {
        logger.debug("get all dsl templates");
        return dslTemplateDAO.loadAll();
    }

    public Template unmarshalTemplateXml(DslTemplate dslTemplate) throws Exception{
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



}
