package edu.monash.fit2099.exceptions;

/**
 * custom exception for when something went wrong with the vehicle inputs
 * more specifically, when the model or maker attributes are invalid
 *
 * @see edu.monash.fit2099.vehicles.Vehicle
 */
public class VehicleException extends Exception {

    /**
     * sends out an exception message
     * @param message - exception message to send out
     */

    public VehicleException(String message) {
        super(message);
    }
}
