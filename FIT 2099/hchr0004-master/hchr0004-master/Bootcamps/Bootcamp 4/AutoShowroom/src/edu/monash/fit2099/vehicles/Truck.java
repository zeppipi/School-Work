package edu.monash.fit2099.vehicles;

public class Truck extends Vehicle{

    private int capacity;
    private int wheels;

    public Truck(String maker, String model, int capacity, int wheels) {    //two constructors that match the parent's...
        super(maker, model);
        this.capacity = capacity;
        this.wheels = wheels;
    }

    public Truck(String maker, String model, String vehicleID, int capacity, int wheels) {  //...that also initializes capacity and wheels
        super(maker, model, vehicleID);
        this.capacity = capacity;
        this.wheels = wheels;
    }
}
