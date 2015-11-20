package at.tuwien.ase.controller.exceptions;

import java.lang.invoke.MethodHandles;

import org.apache.logging.log4j.*;
import org.apache.logging.log4j.core.impl.Log4jContextFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * Created by DanielHofer on 16.15.11.2015.
 */

@ControllerAdvice
public class GenericRestExceptionHandler {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String handleGenericException(final Exception exception) {
        logException(exception);
        return exception.getMessage();
    }


    //Add more exceptions handling methods as needed
    //...

    private void logException(final Exception exception) {
        logger.error(exception);
        exception.printStackTrace();
    }

}