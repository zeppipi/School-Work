public class Bid {

    private String bidID;
    private String bidDate;
    private int bidPrice;
    Buyer bidBuyerData = new Buyer("test", "test", "test");

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

    //setter and getter for bidBuyerData
    public Buyer getBidBuyerData() {
        return bidBuyerData;
    }

    public void setBidBuyerData(Buyer bidBuyerData) {
        this.bidBuyerData = bidBuyerData;
    }

    //constructor for all 4
    public Bid(String bidID, String bidDate, int bidPrice, Buyer bidBuyerData) {
        this.bidID = bidID;
        this.bidDate = bidDate;
        this.bidPrice = bidPrice;
        this.bidBuyerData = bidBuyerData;
    }

    public String description(){    //returns the concatenated string of the bidID's
        return ("Buyer ID: " + bidBuyerData.description() + " \nBidder's ID: " + bidID + " Bid's Price($): " + bidPrice + " Bid's date: " + bidDate + "\n");
    }

}
