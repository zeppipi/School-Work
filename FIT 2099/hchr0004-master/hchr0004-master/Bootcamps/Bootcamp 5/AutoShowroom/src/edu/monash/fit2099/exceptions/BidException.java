package edu.monash.fit2099.exceptions;

/**
 * custom exception for when something went wrong with the bid's inputs
 */
public class BidException extends Exception{

    /**
     * sends out the exception message
     * @param message - exception message to send out
     */

    public BidException(String message) {
        super(message);
    }
}
