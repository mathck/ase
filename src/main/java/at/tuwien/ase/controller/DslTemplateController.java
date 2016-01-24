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
@RequestMapping("/api/")
public class DslTemplateController {

    @Autowired
    private DslTemplateService ts;

    @Autowired
    private GenericRestExceptionHandler genericRestExceptionHandler;

    private static final Logger logger = LogManager.getLogger(DslTemplateController.class);


    // @author Daniel Hofer
    @RequestMapping(value = "workspace/templates", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public JsonStringWrapper createDslTemplate(@RequestBody DslTemplate dslTemplate, @RequestParam("mode") String mode) throws Exception  {
        return ts.writeDslTemplate(dslTemplate, mode);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/templates/{tID}", method = RequestMethod.PATCH)
    @ResponseBody
    public void updateDslTemplate(@RequestBody DslTemplate dslTemplate, @PathVariable("tID") int tID) throws Exception  {
        ts.updateDslTemplateById(dslTemplate, tID);
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
    @RequestMapping(value = "workspace/users/{uID}/templates", method = RequestMethod.GET)
    @ResponseBody
    public LinkedList<DslTemplate> getAllDslTemplatesByUser(@PathVariable("uID") String uID) throws Exception {
        return ts.getAllDslTemplatesByUser(uID);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/templates/{tID}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteDslTemplateByID(@PathVariable("tID") int tID)  throws Exception {
        ts.deleteDslTemplateByID(tID);
    }


}
