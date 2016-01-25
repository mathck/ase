package at.tuwien.ase.services;

import at.tuwien.ase.controller.exceptions.ValidationException;
import at.tuwien.ase.model.JsonStringWrapper;
import at.tuwien.ase.model.DslTemplate;
import at.tuwien.ase.model.javax.Template;
import org.springframework.dao.DataAccessException;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by DanielHofer on 21.12.2015.
 */
public interface DslTemplateService {

    /**
     *
     * If mode = create: Validate {@link DslTemplate} and store it to db
     * If mode = validate: Validate {@link DslTemplate}
     *
     * @param template  {@link DslTemplate} object
     * @param mode      mode (create or validate)
     * @return          Id of stored {@link DslTemplate} wrapped in {@link JsonStringWrapper}
     * @throws Exception      if an exception occurred
     */
    JsonStringWrapper writeDslTemplate(DslTemplate template, String mode) throws Exception;

    /**
     *
     * Delete {@link DslTemplate} from db by Id.
     *
     * @param tID  Id of {@link DslTemplate}.
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    void deleteDslTemplateByID(int tID) throws DataAccessException;

    /**
     *
     * Update {@link DslTemplate} in db by Id.
     *
     * @param template  {@link DslTemplate} object.
     * @param tID       Id of {@link DslTemplate}.
     * @throws Exception      if an exception occurred
     */
    void updateDslTemplateById(DslTemplate template, int tID) throws Exception;

    /**
     *
     * Get {@link DslTemplate} from db by Id.
     *
     * @param tID    Id of {@link DslTemplate}
     * @return       {@link DslTemplate} object
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    DslTemplate getByID(int tID)throws DataAccessException;

    /**
     *
     * Get all {@link DslTemplate} from db.
     *
     * @return {@link LinkedList} of {@link DslTemplate}
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    LinkedList<DslTemplate> getAllDslTemplates() throws DataAccessException;

    /**
     *
     * Get all {@link DslTemplate} from a specific {@link at.tuwien.ase.model.User}.
     *
     * @param uID  Id of {@link at.tuwien.ase.model.User}.
     * @return     {@link LinkedList} of {@link DslTemplate}
     * @throws DataAccessException      if an data access
     *                                  exception occurred
     */
    LinkedList<DslTemplate> getAllDslTemplatesByUser(String uID) throws DataAccessException;

    /**
     *
     * Unmarshal and validate {@link DslTemplate} against its xsd schema.
     * Return unmarshalled {@link Template} object.
     *
     * @param dslTemplate     {@link DslTemplate} object
     * @return                {@link Template} object
     * @throws Exception      if an exception occurred
     */
    Template unmarshalTemplateXml(DslTemplate dslTemplate) throws Exception;

    /**
     *
     * Convert the {@link at.tuwien.ase.model.javax.TaskBody} of @link Template}
     * to a string representation containing all xml elements.
     *
     * @param content   content of {@link at.tuwien.ase.model.javax.TaskBody} as {@link List}.
     * @return          xml representation of {@link at.tuwien.ase.model.javax.TaskBody} as string.
     * @throws Exception      if an exception occurred
     */
    String convertTaskBodyToString(List<Serializable> content) throws  Exception;

}