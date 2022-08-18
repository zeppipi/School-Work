package edu.monash.fit2099.bids;

import edu.monash.fit2099.exceptions.BidException;

import java.util.regex.Pattern;

/**
 * the bid class consists of these private attributes:
 * bidID: an ID attached to a bid, generated randomly with no user input, the randomizer is placed in the
 *        vehicle class
 * @see edu.monash.fit2099.vehicles.Vehicle
 * bidDate: a date attached to a bid
 * bidPrice: the price of the bid
 */
public class Bid {

    private String bidID;
    private String bidDate;
    private int bidPrice;

    //setter and getter for bidID
    /**
     * method to extract the bidID
     *
     * @return bidID
     */
    public String getBidID() {
        return bidID;
    }

    /**
     * method to set the bidID
     *
     * @param bidID - the bidder's ID
     */
    public void setBidID(String bidID) {

        this.bidID = bidID;
    }

    //setter and getter for bidDate
    /**
     * method to extract the bidDate
     *
     * @return bidDate
     */
    public String getBidDate() {

        return bidDate;
    }

    /**
     * a boolean method to set the bidDate, which will only let users set the bidDate
     * when it is considered as valid.
     *
     * There are two validations, one is their format which is managed by dateFormat
     * The other validation is making sure the numbers inputted are valid days, month, and year
     * managed by an if statement
     *
     * @return isValid
     * @param bidDate - the date the bid takes place
     */
    public boolean setBidDate(String bidDate) {     //now a boolean setter

        boolean isValid = false;
        Pattern dateFormat = Pattern.compile("\\d{2}/\\d{2}/\\d{4}");
        if(dateFormat.matcher(bidDate).matches()){
            int day = Integer.parseInt(bidDate.substring(0,2));
            int month = Integer.parseInt(bidDate.substring(3,5));
            int year = Integer.parseInt(bidDate.substring(6));

            if((day >= 1 && day <= 31) && (month >= 1 && month <= 12) && (year >= 1930 && year <= 2021)){
                isValid = true;
                this.bidDate = bidDate;
            }
        }
        return isValid;
    }

    //setter and getter for bidPrice
    /**
     * method to extract the bid price
     *
     * @return bidPrice
     */
    public int getBidPrice() {

        return bidPrice;
    }

    /**
     * a boolean method where bid price can be set only if the input is valid
     *
     * @return isValid
     * @param bidPrice - the price of the bid
     */
    public boolean setBidPrice(int bidPrice) {      //now a boolean setter

        boolean isValid = false;
        if(bidPrice >= 0){
            isValid = true;
            this.bidPrice = bidPrice;
        }
        return isValid;
    }

    //constructor for all 3
    /**
     * a method with bid ID, bid Price, and bid Date
     *
     * @param bidID - the bidder's ID
     * @param bidDate - the date the bid takes place
     * @param bidPrice - the price of the bid
     */
    public Bid(String bidID, String bidDate, int bidPrice) {

        this.bidID = bidID;
        this.bidDate = bidDate;
        this.bidPrice = bidPrice;
    }

    /**
     * This method is where it checked if both bidDate and bidPrice was valid
     * if either of them was invalid, then it will throw an exception
     *
     * @exception BidException - Custom exception, for when something does wrong with the bid inputs
     * @param bidPrice - the price of the bid
     * @param bidDate - the date of the bid
     */
    public Bid(String bidDate, int bidPrice) throws BidException {

        if(setBidDate(bidDate) && setBidPrice(bidPrice)){
            this.bidDate = bidDate;
            this.bidPrice = bidPrice;
        }else {
            throw new BidException("Invalid Input: invalid date format or price in the negatives");
        }

    }

    /**
     * a method that returns the info in a bid object into a neat sentence
     *
     * @return ("Bidder's ID: " + bidID + " | Price($): " + bidPrice + " | Bid's date: " + bidDate + "\n")
     */
    public String description(){    //returns the concatenated string of the bidID's

        return ("Bidder's ID: " + bidID + " | Price($): " + bidPrice + " | Bid's date: " + bidDate + "\n");
    }

}
