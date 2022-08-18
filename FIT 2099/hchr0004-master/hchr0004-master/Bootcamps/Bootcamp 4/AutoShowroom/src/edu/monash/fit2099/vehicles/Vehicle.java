package edu.monash.fit2099.vehicles;

import edu.monash.fit2099.AutoShowroom;
import edu.monash.fit2099.bids.Bid;
import edu.monash.fit2099.bids.BidsManager;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Vehicle {   //cars

    private String maker;    //public because they need to be edited by edu.monash.fit2099.AutoShowroom
    private String model;
    private String vehicleID;

    private BidsManager instanceOfBidHashmap = new BidsManager();       //instance of the hashmap

    public BidsManager getInstanceOfBidHashmap() {
        return instanceOfBidHashmap;
    }

    public void description(){ //keeps maker name, model name, and counter

        HashMap<String, Bid> tempHashMap = instanceOfBidHashmap.getBidHashMap();
        String tempString = "";

        for(HashMap.Entry<String, Bid> entry : tempHashMap.entrySet()){     //turn hashmap keys and values to strings

            tempString += "Buyer's ID: " + entry.getKey() + ": " + entry.getValue().description();

        }

        System.out.println("Vehicle ID: " + vehicleID + " | Maker:"+maker+" | Model:"+model+ " | Vehicle's Buyers: \n" + tempString);  //show car and their bidders

    }

    public Vehicle(String maker, String model) {    //constructor with maker and model to give out vehicleID
        this.maker = maker;
        this.model = model;
        vehicleID = randomizerID();
    }

    public Vehicle(String maker, String model, String vehicleID) {      //a constructor with all three attributes
        this.maker = maker;
        this.model = model;
        this.vehicleID = vehicleID;
    }

    public String getVehicleID() {
        return vehicleID;
    }

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
