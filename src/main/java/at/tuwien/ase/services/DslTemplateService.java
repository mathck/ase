package at.tuwien.ase.services;

import at.tuwien.ase.model.JsonStringWrapper;
import at.tuwien.ase.model.DslTemplate;
import at.tuwien.ase.model.javax.Template;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by DanielHofer on 21.12.2015.
 */
public interface DslTemplateService {

    JsonStringWrapper writeDslTemplate(DslTemplate template, String mode) throws Exception;

    void deleteDslTemplateByID(int tID);
    void updateDslTemplateById(DslTemplate template, int tID) throws Exception;

    DslTemplate getByID(int tID);

    LinkedList<DslTemplate> getAllDslTemplates();
    LinkedList<DslTemplate> getAllDslTemplatesByUser(String uID);

    Template unmarshalTemplateXml(DslTemplate dslTemplate) throws Exception;
    String convertTaskBodyToString(List<Serializable> content) throws  Exception;

}