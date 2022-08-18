package edu.monash.fit2099.bids;

public class Bid {

    private String bidID;
    private String bidDate;
    private int bidPrice;

    //setter and getter for bidID
    public String getBidID() {
        return bidID;
    }

    public void setBidID(String bidID) {
        this.bidID = bidID;
    }

    //setter and getter for bidDate
    public String getBidDate() {
        return bidDate;
    }

    public void setBidDate(String bidDate) {
        this.bidDate = bidDate;
    }

    //setter and getter for bidPrice
    public int getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(int bidPrice) {
        this.bidPrice = bidPrice;
    }

    //constructor for all 3
    public Bid(String bidID, String bidDate, int bidPrice) {
        this.bidID = bidID;
        this.bidDate = bidDate;
        this.bidPrice = bidPrice;
    }

    public String description(){    //returns the concatenated string of the bidID's
        return ("Bidder's ID: " + bidID + " | Price($): " + bidPrice + " | Bid's date: " + bidDate + "\n");
    }

}
