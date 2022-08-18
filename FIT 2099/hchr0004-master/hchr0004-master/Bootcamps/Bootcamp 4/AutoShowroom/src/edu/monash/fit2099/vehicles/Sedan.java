package edu.monash.fit2099.vehicles;

public class Sedan extends Vehicle{

    private int seats;

    public Sedan(String maker, String model, int seats) {       //two constructors that match the parent's...
        super(maker, model);
        this.seats = seats;
    }

    public Sedan(String maker, String model, String vehicleID, int seats) {     //...that also initializes seats
        super(maker, model, vehicleID);
        this.seats = seats;
    }
}
