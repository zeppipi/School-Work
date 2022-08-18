public class Buyer {

    //picked strings for the IDs
    private String buyerID;         //making it say givenName and familyName instead of firstName and lastName makes it more clear for people with
    private String givenName;       //middle names or multiple middle names what part of their names it needed, lastName isn't full clear if it means
    private String familyName;      //just their last name or everything after their first name

    public Buyer(String newBuyerID) {      //constructor with only the buyerID attribute
        this.buyerID = newBuyerID;
    }

    public Buyer(String newBuyerID, String newGivenName, String newFamilyName) {    //constructor with all 3 attributes
        this.buyerID = newBuyerID;
        this.givenName = newGivenName;
        this.familyName = newFamilyName;
    }

    public void setGivenName(String givenName) {        //setter for givenName
        this.givenName = givenName;
    }

    public void setFamilyName(String familyName) {      //setter for familyName
        this.familyName = familyName;
    }

    public String description(){    //returns the concatenated string of the buyer's ID, their name, and their family name
        return ("Car buyer ID: "+ buyerID + " Car buyer Given Name: " + givenName + " Car Buyer Family Name: " + familyName);
    }

}
