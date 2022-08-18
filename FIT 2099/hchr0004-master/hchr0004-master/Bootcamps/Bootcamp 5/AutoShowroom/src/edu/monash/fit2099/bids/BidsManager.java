package edu.monash.fit2099.bids;

import edu.monash.fit2099.vehicles.Vehicle;

import java.util.HashMap;

/**
 * the class that is responsible for putting the bid object into a hashmap that will be attached
 * to a vehicle
 *
 * @see Vehicle
 */
public class BidsManager {

    private HashMap<String, Bid> bidHashMap = new HashMap<String, Bid>();

    /**
     * constructor to access this class
     */
    public BidsManager() {      //essentially getter

        this.bidHashMap = new HashMap<String, Bid>();
    }

    /**
     * method to put the bid object into the hashmap
     *
     * @param bidder - bid object
     * @param buyerID - the ID of the buyer
     */
    public void addBid(String buyerID, Bid bidder){       //essentially setter

        bidHashMap.put(buyerID, bidder);    //put in hashmap
    }

    /**
     * a method that takes a bidPrice and bidDate, turn those into a bid object
     * which is then put into the hashmap
     *
     * @param buyerID - the ID of the buyer
     * @param bidDate - the date the bid takes place
     * @param bidPrice - the price of the bid
     */
    public void addBid(String buyerID, int bidPrice, String bidDate){       //essentially setter
        Bid bidder = new Bid(Vehicle.randomizerID(), bidDate, bidPrice);     //put the bid's info
        bidHashMap.put(buyerID, bidder);    //put in hashmap
   }

    /**
     * a method that returns the hashmap to whoever calls it
     *
     * @return bidHashmap
     */
    public HashMap<String, Bid> getBidHashMap() {
        return bidHashMap;
    }

}
