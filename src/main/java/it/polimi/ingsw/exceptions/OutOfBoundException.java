package it.polimi.ingsw.exceptions;

/**
 * Exception OutOfBoundException is thrown when the limits of an object are trying being exceeded.
 *
 * @author Luca Pirovano
 */
public class OutOfBoundException extends Exception{
    /**
     * Method getMessage returns the message of this OutOfBoundException object.
     *
     * @return the message (type String) of this OutOfBoundException object.
     */
    @Override
    public String getMessage() {
        return ("Error: Tower level not permitted");
    }

}
