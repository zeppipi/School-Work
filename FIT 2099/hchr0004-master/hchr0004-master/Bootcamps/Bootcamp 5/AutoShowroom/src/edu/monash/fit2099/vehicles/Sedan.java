package edu.monash.fit2099.vehicles;

import edu.monash.fit2099.exceptions.SedanException;
import edu.monash.fit2099.exceptions.VehicleException;

/**
 * creates a vehicle object of sedan type, sedan is an extension of the vehicle class
 * so its attributes consists of everything in the vehicle class, with the addition of the
 * attribute seats
 *
 * @see Vehicle
 */
public class Sedan extends Vehicle{

    private int seats;

    /**
     * method that throws an exception when any of its inputs are invalid
     *
     * @param maker - an attribute from the vehicle class
     * @param model - an attribute also from the vehicle class
     * @param seats - an attribute specific to the sedan class
     * @throws SedanException - calls the custom exception for any sedan related errors
     */
    public Sedan(String maker, String model, int seats) throws VehicleException {       //two constructors that match the parent's...

        super(maker, model);
        if(setSeats(seats)){
            this.seats = seats;
        } else {
            throw new SedanException("Invalid input: too many or too little seats");
        }

    }

    /**
     * method that sets sedan's attributes
     *
     * @param maker - an attribute from the vehicle class
     * @param model - an attribute also from the vehicle class
     * @param vehicleID - an attribute from the vehicle class also
     * @param seats - an attribute specific to the sedan class
     */
    public Sedan(String maker, String model, String vehicleID, int seats){     //...that also initializes seats
        super(maker, model, vehicleID);
        this.seats = seats;
    }

    /**
     * a boolean method that checks if the input for seats was a valid input
     *
     * @param seats - the amount of seats in the sedan
     * @return isValid
     */
    public boolean setSeats(int seats) {        //boolean setter for seats
        boolean isValid = false;
        if(seats == 4 || seats == 5){
            isValid = true;
            this.seats = seats;
        }
        return isValid;
    }
}
