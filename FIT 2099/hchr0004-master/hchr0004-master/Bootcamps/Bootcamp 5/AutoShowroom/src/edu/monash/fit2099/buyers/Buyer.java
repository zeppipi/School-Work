package edu.monash.fit2099.buyers;

/**
 * this class is responsible for getting the buyer info that will turn into a buyer object
 * it consists of these private attributes:
 * buyerID: a user made ID that is attached to any given buyer
 * giveName: the buyer's given name
 * familyName: the buyer's family name
 */
public class Buyer {
    //picked strings for the IDs
    private String buyerID;         //making it say givenName and familyName instead of firstName and lastName makes it more clear for people with
    private String givenName;       //middle names or multiple middle names what part of their names it needed, lastName isn't full clear if it means
    private String familyName;      //just their last name or everything after their first name

    /**
     * constructor that deals with only the buyer ID
     */
    private Buyer(String newBuyerID) {      //constructor with only the buyerID attribute
        this.buyerID = newBuyerID;
    }

    /**
     * constructor that deals with all 3 attributes
     */
    private Buyer(String newBuyerID, String newGivenName, String newFamilyName) {    //constructor with all 3 attributes
        this.buyerID = newBuyerID;
        this.givenName = newGivenName;
        this.familyName = newFamilyName;
    }

    /**
     * a constructor with nothing in it, used to create a reflection of this class
     */
    public Buyer() {    //this constructor does nothing?
    }

    /**
     * to keep this class robust, this class is set to make sure that this method is the only way
     * to create a buyer object, and it will only create one when both the family name and given name
     * are valid inputs
     *
     * @return instanceBuyer - returns instanceBuyer when the input are valid, returns null when the inputs are not valid
     * @param buyerID - the ID of the buyer
     * @param familyName - the buyer's family name
     * @param givenName - the buyer's given name
     */
    public static Buyer getInstance(String buyerID, String givenName, String familyName){
        Buyer instanceBuyer = new Buyer();
        boolean givenNameValid = instanceBuyer.setGivenName(givenName);
        boolean familyNameValid = instanceBuyer.setFamilyName(familyName);
        instanceBuyer.setBuyerID(buyerID);

        if(givenNameValid && familyNameValid){
            return instanceBuyer;
        }else {
            return null;
        }
    }

    /**
     * a boolean method that checks if the given givenName is a valid input
     *
     * @return isValid
     * @param givenName - the buyer's given name
     */
    public boolean setGivenName(String givenName) {        //boolean setter for givenName
        boolean isValid = false;
        if(givenName.length() >= 2 && givenName.length() <= 15){
            isValid = true;
            this.givenName = givenName;
        }
        return isValid;
    }

    /**
     * a boolean method that checks if the given familyName is a valid input
     *
     * @return isValid
     * @param familyName - the buyer's family name
     */
    public boolean setFamilyName(String familyName) {      //boolean setter for familyName
        boolean isValid = false;
        if(familyName.length() >= 2 && familyName.length() <= 15){
            isValid = true;
            this.familyName = familyName;
        }
        return isValid;
    }

    /**
     * method to set the buyer ID
     *
     * @param buyerID - the ID of the buyer
     */
    public void setBuyerID(String buyerID) {
        this.buyerID = buyerID;
    }

    /**
     * method to get the buyer ID
     *
     * @return buyerID
     */
    public String getBuyerID() {
        return buyerID;
    }

    /**
     * method that prints out the buyer object's info into a neat sentence
     */
    public void description(){    //returns the concatenated string of the buyer's ID, their name, and their family name
        System.out.println("Buyer ID: "+ buyerID + " | Buyer Given Name: " + givenName + " | Buyer Family Name: " + familyName + "\n");
    }

}
