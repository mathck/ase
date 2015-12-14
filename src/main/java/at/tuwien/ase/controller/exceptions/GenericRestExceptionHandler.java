package at.tuwien.ase.controller.exceptions;

import java.lang.invoke.MethodHandles;

import at.tuwien.ase.model.JsonStringWrapper;
import org.apache.logging.log4j.*;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Daniel Hofer on 15.11.2015.
 */

@ControllerAdvice
public class GenericRestExceptionHandler {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public JsonStringWrapper handleGenericException(final Exception exception) {
        logException(exception);
        return new JsonStringWrapper(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public JsonStringWrapper handleArgumentException(final IllegalArgumentException exception) {
        logger.warn(exception.getMessage());
        return new JsonStringWrapper(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EmptyResultDataAccessException.class)
    @ResponseBody
    protected JsonStringWrapper handleEmptyResult(final EmptyResultDataAccessException exception) {
        logger.warn("No entry available");
        return new JsonStringWrapper("No entry available");
    }

    //Add more exceptions handling methods as needed
    //...

    private void logException(final Exception exception) {
        logger.error(exception);
        exception.printStackTrace();
    }

}