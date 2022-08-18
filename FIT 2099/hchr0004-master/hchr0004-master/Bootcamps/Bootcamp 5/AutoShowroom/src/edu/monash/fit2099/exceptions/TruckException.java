package edu.monash.fit2099.exceptions;

 /**
 * custom exception for when something went wrong with the truck's input
 * more specifically, when the wheels or capacity attribute are invalid
 *
 * @see edu.monash.fit2099.vehicles.Truck
 */
public class TruckException extends VehicleException{

    /**
     * sends out an exception message
     * @param message - exception message to send out
     */

    public TruckException(String message) {
        super(message);
    }
}
