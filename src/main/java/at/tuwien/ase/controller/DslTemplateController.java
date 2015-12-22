package at.tuwien.ase.controller;

import at.tuwien.ase.controller.exceptions.GenericRestExceptionHandler;
import at.tuwien.ase.model.DslTemplate;
import at.tuwien.ase.model.JsonStringWrapper;
import at.tuwien.ase.services.DslTemplateService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;

/**
 * Created by Daniel Hofer on 21.12.2015.
 */

@RestController
public class DslTemplateController {

    @Autowired
    private DslTemplateService ts;

    @Autowired
    private GenericRestExceptionHandler genericRestExceptionHandler;

    private static final Logger logger = LogManager.getLogger(DslTemplateController.class);


    // @author Daniel Hofer
    @RequestMapping(value = "workspace/templates", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public JsonStringWrapper createDslTemplate(@RequestBody DslTemplate dslTemplate) throws Exception  {
        return ts.writeDslTemplate(dslTemplate);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/templates/{tID}", method = RequestMethod.GET)
    @ResponseBody
    public DslTemplate getDslTemplateByID(@PathVariable("tID") int tID) throws Exception {
        return ts.getByID(tID);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/templates", method = RequestMethod.GET)
    @ResponseBody
    public LinkedList<DslTemplate> getAllDslTemplates() throws Exception {
        return ts.getAllDslTemplates();
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/templates/{tID}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteIssueByID(@PathVariable("tID") int tID)  throws Exception {
        ts.deleteDslTemplateByID(tID);
    }


}
