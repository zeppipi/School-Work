package edu.monash.fit2099.exceptions;

/**
 * custom exception for when something went wrong with the sedan inputs
 * more specifically, when the seat attribute is invalid
 *
 * @see edu.monash.fit2099.vehicles.Vehicle
 */
public class SedanException extends VehicleException{

    /**
     * sends out an exception message
     * @param message - exception message to send out
     */

    public SedanException(String message) {
        super(message);
    }
}
