package at.tuwien.ase.services;

import at.tuwien.ase.model.JsonStringWrapper;
import at.tuwien.ase.model.DslTemplate;

import java.util.LinkedList;

/**
 * Created by DanielHofer on 21.12.2015.
 */
public interface DslTemplateService {

    JsonStringWrapper writeDslTemplate(DslTemplate template) throws Exception;

    void deleteDslTemplateByID(int tID);

    DslTemplate getByID(int tID);

    LinkedList<DslTemplate> getAllDslTemplates();

}