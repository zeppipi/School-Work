package edu.monash.fit2099.vehicles;

import edu.monash.fit2099.exceptions.TruckException;
import edu.monash.fit2099.exceptions.VehicleException;

/**
 * creates a vehicle object of truck type, truck is an extension of the vehicle class
 * so its attributes consists of everything in the vehicle class, with the addition of the
 * attribute capacity and wheels
 *
 * @see Vehicle
 */
public class Truck extends Vehicle{

    private int capacity;
    private int wheels;

    /**
     * method that throws an exception when any of its inputs are invalid
     *
     * @param maker - an attribute from the vehicle class
     * @param model - an attribute also from the vehicle class
     * @param capacity - an attribute specific to the truck class
     * @param wheels - another attribute specific to the truck class
     * @throws TruckException - calls custom exception for any truck related errors
     */
    public Truck(String maker, String model, int capacity, int wheels) throws VehicleException {    //two constructors that match the parent's...

        super(maker, model);
        if(setCapacity(capacity) && setWheels(wheels)){
            this.capacity = capacity;
            this.wheels = wheels;
        }else {
            throw new TruckException("Invalid Input: too many or too little wheels or capacity");
        }

    }

    /**
     * method that sets Truck's attributes
     *
     * @param maker - an attribute from the vehicle class
     * @param model - an attribute also from the vehicle class
     * @param vehicleID - an attribute from the vehicle class also
     * @param capacity - an attribute specific to the truck class
     * @param wheels - an attribute specific to the truck class
     */
    public Truck(String maker, String model, String vehicleID, int capacity, int wheels) {  //...that also initializes capacity and wheels

        super(maker, model, vehicleID);
        this.capacity = capacity;
        this.wheels = wheels;
    }

    /**
     * boolean method to check if the input in capacity was valid
     *
     * @param capacity - the truck's weight capacity
     * @return isValid
     */
    public boolean setCapacity(int capacity) {  //boolean setters

        boolean isValid = false;
        if(capacity >= 1 && capacity <= 15){
            isValid = true;
            this.capacity = capacity;
        }
        return isValid;
    }

    /**
     * boolean method to check if the input in wheels was valid
     *
     * @param wheels - the amount of wheels the truck has
     * @return isValid
     */
    public boolean setWheels(int wheels) {      //boolean setters

        boolean isValid = false;
        if(wheels >= 4 && wheels <= 16){
            isValid = true;
            this.wheels = wheels;
        }
        return isValid;
    }
}
