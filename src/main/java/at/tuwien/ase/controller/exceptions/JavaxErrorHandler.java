package at.tuwien.ase.controller.exceptions;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import java.lang.invoke.MethodHandles;

/**
 * Created by DanielHofer on 19.12.2015.
 */

public class JavaxErrorHandler implements ValidationEventHandler {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    String errors;

    public JavaxErrorHandler(){
        errors = new String("");
    }
/*
    public void warning(SAXParseException exception) throws SAXException {
        System.out.println("\nWARNING");
        exception.printStackTrace();
    }

    public void error(SAXParseException exception) throws SAXException {
        System.out.println("\nERROR");
        exception.printStackTrace();
    }

    public void fatalError(SAXParseException exception) throws SAXException {
        System.out.println("\nFATAL ERROR");
        exception.printStackTrace();
    }*/


    /*
    //handle validation event
    public boolean handleEvent(ValidationEvent event) {
        logger.warn("\nEVENT");
        logger.warn("SEVERITY:  " + event.getSeverity());
        logger.warn("MESSAGE:  " + event.getMessage());
        logger.warn("LINKED EXCEPTION:  " + event.getLinkedException());
        logger.warn("LOCATOR");
        logger.warn("    LINE NUMBER:  " + event.getLocator().getLineNumber());
        logger.warn("    COLUMN NUMBER:  " + event.getLocator().getColumnNumber());
        logger.warn("    OFFSET:  " + event.getLocator().getOffset());
        logger.warn("    OBJECT:  " + event.getLocator().getObject());
        logger.warn("    NODE:  " + event.getLocator().getNode());
        logger.warn("    URL:  " + event.getLocator().getURL());
        return true;
    }*/

    //handle validation event
    public boolean handleEvent(ValidationEvent event) {
        errors += "\n\nEVENT";
        errors += "\nSEVERITY:  " + event.getSeverity();
        errors += "\nMESSAGE:  " + event.getMessage();
        errors += "\nLINKED EXCEPTION:  " + event.getLinkedException();
        errors += "\nLOCATOR";
        errors += "\n    LINE NUMBER:  " + event.getLocator().getLineNumber();
        errors += "\n    COLUMN NUMBER:  " + event.getLocator().getColumnNumber();
        errors += "\n    OFFSET:  " + event.getLocator().getOffset();
        errors += "\n    OBJECT:  " + event.getLocator().getObject();
        errors += "\n    NODE:  " + event.getLocator().getNode();
        errors += "\n    URL:  " + event.getLocator().getURL();
        return true;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }


}

