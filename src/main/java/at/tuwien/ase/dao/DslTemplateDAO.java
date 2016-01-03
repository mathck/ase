package at.tuwien.ase.dao;

import at.tuwien.ase.model.DslTemplate;

import java.util.LinkedList;

/**
 * Created by DanielHofer on 21.12.2015.
 */
public interface DslTemplateDAO {

    void insertDslTemplate(DslTemplate template);
    void removeDslTemplateByID(int tID);

    DslTemplate findByID(int tID);
    LinkedList<DslTemplate> loadAll();

    void alterDslTemplateByID(DslTemplate template, int tID);

    int getNewID();

}