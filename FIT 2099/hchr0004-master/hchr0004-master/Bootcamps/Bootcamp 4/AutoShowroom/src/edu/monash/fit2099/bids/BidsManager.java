package edu.monash.fit2099.bids;

import edu.monash.fit2099.vehicles.Vehicle;

import java.util.HashMap;

public class BidsManager {

    private HashMap<String, Bid> bidHashMap = new HashMap<String, Bid>();

    public BidsManager() {      //essentially getter
        this.bidHashMap = new HashMap<String, Bid>();
    }

    public void addBid(String buyerID, int bidPrice, String bidDate){       //essentially setter
        Bid bidder = new Bid(Vehicle.randomizerID(), bidDate, bidPrice);     //put the bid's info
        bidHashMap.put(buyerID, bidder);    //put in hashmap
    }

    public HashMap<String, Bid> getBidHashMap() {
        return bidHashMap;
    }

}
