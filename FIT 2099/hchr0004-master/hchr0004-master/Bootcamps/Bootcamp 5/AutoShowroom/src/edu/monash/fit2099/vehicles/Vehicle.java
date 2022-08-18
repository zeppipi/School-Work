package edu.monash.fit2099.vehicles;

import edu.monash.fit2099.AutoShowroom;
import edu.monash.fit2099.bids.Bid;
import edu.monash.fit2099.bids.BidsManager;
import edu.monash.fit2099.exceptions.VehicleException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Vehicle class, this is where the vehicle object is made
 * it has the attributes:
 * maker: a user inputted string to tell the vehicle's maker
 * model: a user inputted string to tell the vehicle's model
 * vehicleID: a random generated ID attached to every vehicle object
 * instanceofBidHashmap: the hashmap of bids attached to every vehicle
 */
public abstract class Vehicle {   //cars

    private String maker;    //public because they need to be edited by edu.monash.fit2099.AutoShowroom
    private String model;
    private String vehicleID;

    private BidsManager instanceOfBidHashmap = new BidsManager();       //instance of the hashmap

    /**
     * a method that simply returns the hashmap so other classes can use it like in AutoShowroom
     * @see AutoShowroom
     *
     * @return instanceofBidHashmap
     */
    public BidsManager getInstanceOfBidHashmap() {
        return instanceOfBidHashmap;
    }

    /**
     * a boolean method making sure the input for maker was valid
     *
     * @param maker - vehicle's maker
     * @return isValid
     */
    public boolean setMaker(String maker) {     //boolean setters for maker
        boolean isValid = false;
        if(maker.length() >= 3 && maker.length() <= 15){
            isValid = true;
            this.maker = maker;
        }
        return isValid;
    }

    /**
     * a boolean method making sure the input for model was valid
     *
     * @param model - vehicle's model
     * @return isValid
     */
    public boolean setModel(String model) {     //boolean setter for model
        boolean isValid = false;
        if(model.length() >= 3 && model.length() <= 15){
            isValid = true;
            this.model = model;
        }
        return isValid;
    }

    /**
     * a method that prints out all the information in the vehicle object into a neat sentence
     */
    public void description(){ //keeps maker name, model name, and counter

        HashMap<String, Bid> tempHashMap = instanceOfBidHashmap.getBidHashMap();
        String tempString = "";

        for(HashMap.Entry<String, Bid> entry : tempHashMap.entrySet()){     //turn hashmap keys and values to strings

            tempString += "Buyer's ID: " + entry.getKey() + ": " + entry.getValue().description();

        }

        System.out.println("Vehicle ID: " + vehicleID + " | Maker:"+maker+" | Model:"+model+ " | Vehicle's Buyers: \n" + tempString);  //show car and their bidders

    }

    /**
     * a method that throws an exception when any of its inputs are invalid
     *
     * @param maker - vehicle's maker
     * @param model - vehicle's model
     * @throws VehicleException - calls the custom exception for vehicle errors
     */
    public Vehicle(String maker, String model) throws VehicleException {    //constructor with maker and model to give out vehicleID

        if((setMaker(maker)) && setModel(model)){
            this.maker = maker;
            this.model = model;
            vehicleID = randomizerID();
        }else {
            throw new VehicleException("Invalid Input: model or maker name either too long or too short");
        }

    }

    /**
     * a method that sets the attributes: maker, model, and vehicleID
     *
     * @param maker - sedan's maker
     * @param model - sedan's model
     * @param vehicleID - sedan's vehicle ID
     */
    public Vehicle(String maker, String model, String vehicleID){      //a constructor with all three attributes
        this.maker = maker;
        this.model = model;
        this.vehicleID = vehicleID;
    }

    /**
     * method to extract the vehicleID
     *
     * @return vehicleID
     */
    public String getVehicleID() {
        return vehicleID;
    }

    /**
     * a method that returns a randomized string, used for vehicle IDs and bidder IDs
     *
     * @see BidsManager
     * @return sb.toString()
     */
    public static String randomizerID(){  //random IDs made here, static method, so other classes can access it

        String LettersAndString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";    //random letters and numbers

        StringBuilder sb = new StringBuilder(8);    //ID's will all be 8 characters long

        for (int i = 0; i < 8; i++ ){
            int index = (int)(LettersAndString.length()*Math.random());     //get 8 random numbers

            sb.append(LettersAndString.charAt(index));      //append sb with a character taken from the random number's position in the 'LettersAndString'
        }

        return sb.toString();   //ID made

        //this function is taken from https://www.geeksforgeeks.org/generate-random-string-of-given-size-in-java/

    }

}
