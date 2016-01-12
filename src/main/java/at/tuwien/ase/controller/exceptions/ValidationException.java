package at.tuwien.ase.controller.exceptions;

/**
 * Created by DanielHofer on 12.01.2016.
 */
public class ValidationException extends Exception{


    public ValidationException(){
        super();
    }


    public ValidationException(String message)
    {
        super(message);
    }

}
