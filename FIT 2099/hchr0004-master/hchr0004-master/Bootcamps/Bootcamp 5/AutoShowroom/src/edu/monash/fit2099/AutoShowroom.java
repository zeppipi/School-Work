package edu.monash.fit2099;

import edu.monash.fit2099.bids.Bid;
import edu.monash.fit2099.bids.BidsManager;
import edu.monash.fit2099.buyers.Buyer;
import edu.monash.fit2099.exceptions.BidException;
import edu.monash.fit2099.exceptions.SedanException;
import edu.monash.fit2099.exceptions.TruckException;
import edu.monash.fit2099.exceptions.VehicleException;
import edu.monash.fit2099.vehicles.Sedan;
import edu.monash.fit2099.vehicles.Truck;
import edu.monash.fit2099.vehicles.Vehicle;

import java.lang.reflect.Array;
import java.util.*;

/**
 * The brains of the program, inputs and outputs of the program will come from here
 * its attributes consists of scanner, which is used, so it can accept user input, and an array of
 * buyer and vehicle objects
 */
public class AutoShowroom{

    private Scanner scanner = new Scanner(System.in);
    private ArrayList<Vehicle> vehicleArrayList = new ArrayList<Vehicle>();     //arraylist of vehicles
    private ArrayList<Buyer> buyerArrayList = new ArrayList<Buyer>();           //arraylist of buyers

    /**
     * This method presents the main menu to the user
     *
     * @return choice - returns the number the user inputted
     */
    public int selectMainMenuItem(){    //console IO main menu
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to FIT2099 Showroom");
        System.out.println("--------------------------");
        System.out.println("1. Add Sedan");
        System.out.println("2. Add Truck");
        System.out.println("3. Add Bid");
        System.out.println("4. Add Buyer");
        System.out.println("5. Display Fleet");
        System.out.println("6. Display Buyers");
        System.out.println("7. Display Highest Bidder");
        System.out.println("8. Display Lowest Bidder");
        System.out.println("9. Delete a bid");
        System.out.println("10. Exit");
        System.out.print("Please Pick Your Option: ");

        //int choice = scanner.nextInt();

        try{
            int choice = scanner.nextInt();
            return choice;
        } catch(InputMismatchException e){
            System.out.println("Please enter a number");
            return 11;
        }

        //return choice;
    }

    /**
     * gives a sedan for the vehicleArrayList
     */
    public void createSedan(){                                      //sedan creator
        System.out.print("Add Sedan Maker: ");                      //ask sedan's maker
        String maker = scanner.nextLine();

        System.out.print("Add Sedan Model: ");                      //ask sedan's model
        String model = scanner.nextLine();

        System.out.print("Add the amount of seats: ");              //ask sedan's seats
        String seatsString = scanner.nextLine();

        try{                                                        //catching user input exception
            int seats = Integer.parseInt(seatsString);
        } catch (NumberFormatException e){
            System.out.println("Invalid Input: Please enter a numeric for the amount of seats");
            return;
        }

        int seats = Integer.parseInt(seatsString);

        String sedanID = Vehicle.randomizerID();                    //make sedan's ID

        try{                                                        //catching exceptions
            Sedan sedanInfoException = new Sedan(maker, model, seats);
        }catch (SedanException e){
            System.out.println(e.getMessage());
            return;
        } catch (VehicleException e) {
            System.out.println(e.getMessage());
            return;
        }

        Sedan sedanInfo = new Sedan(maker, model, sedanID, seats);  //need variable to keep a new sedan

        vehicleArrayList.add(sedanInfo);                            //sedan added to vehicleArray

    }

    /**
     * gives a truck for the vehicleArrayList
     */
    public void createTruck(){                                          //truck creator
        System.out.print("Add Truck Maker: ");                          //ask truck's maker
        String maker = scanner.nextLine();

        System.out.print("Add Truck Model: ");                          //ask truck's model
        String model = scanner.nextLine();

        System.out.print("Add the amount of wheels: ");                 //ask truck's wheels
        String wheelsString = scanner.nextLine();

        System.out.print("Add the amount of capacity (in Tons): ");     //ask truck's wheels
        String capacityString = scanner.nextLine();

        try{
            int capacity = Integer.parseInt(capacityString);
            int wheels = Integer.parseInt(wheelsString);
        }catch (NumberFormatException e){
            System.out.println("Invalid Input: Please enter numerics for the amount of capacity and wheels");
            return;
        }

        int capacity = Integer.parseInt(capacityString);
        int wheels = Integer.parseInt(wheelsString);

        String truckID = Vehicle.randomizerID();                        //make truck's ID

        try{                                                            //catching exceptions
            Truck truckInfoException = new Truck(maker, model, capacity, wheels);
        }catch (TruckException e){
            System.out.println(e.getMessage());
            return;
        }catch (VehicleException e){
            System.out.println(e.getMessage());
            return;
        }

        Truck truckInfo = new Truck(maker, model, truckID, capacity, wheels);    //need variable to keep a new truck
        vehicleArrayList.add(truckInfo);                                //truck added to vehicleArray

    }

    /**
     * creates a buyer object which then will be sent to buyerArrayList
     */
    public void createBuyer(){                                      //buyer creator
        ArrayList<String> buyerIDArrayList = new ArrayList<String>();

        for (int i = 0; i < buyerArrayList.size(); i++) {

            buyerIDArrayList.add(buyerArrayList.get(i).getBuyerID());

        }

        System.out.print("Enter an alphanumerical Buyer ID: ");      //enter an ID
        String buyID = scanner.nextLine();

        if(buyerIDArrayList.contains(buyID)){                  //checks if this vehicle ID exists or not
            System.out.println("This buyer ID already exists");
            return;
        }

        System.out.print("Enter your Given Name: ");                //enter your given name
        String givenName = scanner.nextLine();

        System.out.print("Enter your Family Name: ");               //enter family name
        String familyName = scanner.nextLine();

        Buyer buyerValidation = Buyer.getInstance(buyID, givenName, familyName);    //the 'static factory method' of catching exceptions
        if(buyerValidation != null){
            buyerArrayList.add(buyerValidation);
        }else {
            System.out.println("Invalid Input: Given name or Family name either too short or too long");
            return;
        }

    }

    /**
     * creates a bidder object, which will be given to the vehicle object
     * the bidder was bidding to
     *
     * the vehicle object has a hashmap, where the key is the buyer ID and the value is a bid object
     * so this a is used to make sure the bid is going to the correct vehicle
     */
    public void createBidders(){                                    //bidder creator
        ArrayList<String> vehicleIDArraylist = new ArrayList<String>();
        ArrayList<String> buyerIDArraylist = new ArrayList<String>();

        for(int i = 0; i < vehicleArrayList.size(); i++){       //make a temp list of all vehicle IDs

            vehicleIDArraylist.add(vehicleArrayList.get(i).getVehicleID());

        }

        for (int i = 0; i < buyerArrayList.size() ; i++) {      //make a temp list of all buyer IDs

            buyerIDArraylist.add(buyerArrayList.get(i).getBuyerID());

        }

        System.out.print("Enter a vehicle ID: ");                   //ask for vehicle ID
        String vehicleID = scanner.nextLine();

        if(!(vehicleIDArraylist.contains(vehicleID))){                  //checks if this vehicle ID exists or not
            System.out.println("Vehicle ID does not exist");
            return;
        }

        System.out.print("Enter your Buyer ID: ");                  //ask for your buyer ID
        String buyerID = scanner.nextLine();

        if(!(buyerIDArraylist.contains(buyerID))){                      //checks if this buyer ID exists or not
            System.out.println("Buyer ID does not exist");
            return;
        }

        //now time to actually insert some info
        System.out.print("Enter your bid price (in dollars): ");    //ask for money
        String bidPriceString = scanner.nextLine();

        try{
            int bidPrice = Integer.parseInt(bidPriceString);
        }catch (NumberFormatException e){
            System.out.println("Invalid Input: Please enter only numbers for the price");
            return;
        }

        int bidPrice = Integer.parseInt(bidPriceString);

        System.out.print("Enter the date (dd/mm/yyyy): ");      //ask for date
        String bidDate = scanner.nextLine();

        try{                                                    //catching exceptions
            Bid bidderInfo = new Bid(bidDate, bidPrice);
        } catch (BidException e) {
            System.out.println(e.getMessage());
            return;
        }

        Bid bidderInfo = new Bid(Vehicle.randomizerID(), bidDate, bidPrice);    //bidder info in a temp variable
        for (int i = 0; i < vehicleArrayList.size(); i++) {     //add bid into the vehicle info

            if(vehicleArrayList.get(i).getVehicleID().equals(vehicleID)){
                vehicleArrayList.get(i).getInstanceOfBidHashmap().addBid(buyerID, bidderInfo);
            }

        }
    }

    /**
     * loops through the vehicle array, so they can be shown to the user
     */
    public void displayFleet(){     //display vehicles
        for(int i = 0; i < vehicleArrayList.size(); i++){

            vehicleArrayList.get(i).description();

        }

    }

    /**
     * loops through the buyer array, so they can be shown to the user
     */
    public void displayBuyers(){
        for(int i = 0; i < buyerArrayList.size(); i++){

            buyerArrayList.get(i).description();

        }
    }

    /**
     * shows the highest bid for a current vehicle
     *
     * with the bid object being attached to a hashmap in a vehicle object, loops are used to go
     * through all the bid prices and compare with each other to find the highest one
     */
    public void displayHighestBidder(){
        ArrayList<String> vehicleIDArraylist = new ArrayList<String>();
        int highestBid = 0;
        String highestKey = "Nobody";

        for(int i = 0; i < vehicleArrayList.size(); i++){       //make a temp list of all vehicle IDs

            vehicleIDArraylist.add(vehicleArrayList.get(i).getVehicleID());

        }

        System.out.print("Enter a vehicle ID: ");                   //ask for vehicle ID
        String vehicleID = scanner.nextLine();

        if(!(vehicleIDArraylist.contains(vehicleID))){                  //checks if this vehicle ID exists or not
            System.out.println("Vehicle ID does not exist");
            return;
        }

        for (int i = 0; i < vehicleArrayList.size(); i++) {     //get the right car

            if(vehicleArrayList.get(i).getVehicleID().equals(vehicleID)){
                HashMap<String, Bid> tempBidHashmap = vehicleArrayList.get(i).getInstanceOfBidHashmap().getBidHashMap();

                if(tempBidHashmap.size() == 0){                 //exception?
                    System.out.println("There are currently no bids on this vehicle");
                    return;
                }

                for(Map.Entry mapElement : tempBidHashmap.entrySet()){      //loop through the hashmap
                    Bid currentBidObject = (Bid)mapElement.getValue();
                    int currentBidInt = currentBidObject.getBidPrice();

                    if(highestBid < currentBidInt){        //a new highest bid is found
                        highestBid = currentBidInt;
                        highestKey = (String)mapElement.getKey();
                    }
                }

                //display the highest bidder
                System.out.println("The Highest Bid on this Vehicle is: $" + highestBid + " by the bidID: "+ highestKey);

            }

        }

    }

    /**
     * displays the lowest bidder for a vehicle
     *
     * the way this method works is identical to displayHighestBidder, with the only difference being,
     * instead of finding the highest bid price, it finds the lowest on instead
     */
    public void displayLowestBidder(){
        ArrayList<String> vehicleIDArraylist = new ArrayList<String>();
        int lowestBid = 0;
        String lowestKey = "Nobody";

        for(int i = 0; i < vehicleArrayList.size(); i++){       //make a temp list of all vehicle IDs

            vehicleIDArraylist.add(vehicleArrayList.get(i).getVehicleID());

        }

        System.out.print("Enter a vehicle ID: ");                   //ask for vehicle ID
        String vehicleID = scanner.nextLine();

        if(!(vehicleIDArraylist.contains(vehicleID))){                  //checks if this vehicle ID exists or not
            System.out.println("Vehicle ID does not exist");
            return;
        }

        for (int i = 0; i < vehicleArrayList.size(); i++) {     //get the right car

            if(vehicleArrayList.get(i).getVehicleID().equals(vehicleID)){
                HashMap<String, Bid> tempBidHashmap = vehicleArrayList.get(i).getInstanceOfBidHashmap().getBidHashMap();

                if(tempBidHashmap.size() == 0){                 //exception?
                    System.out.println("There are currently no bids on this vehicle");
                    return;
                }

                lowestKey = tempBidHashmap.get(i).getBidID();
                lowestBid = tempBidHashmap.get(i).getBidPrice();

                for(Map.Entry mapElement : tempBidHashmap.entrySet()){      //loop through the hashmap
                    Bid currentBidObject = (Bid)mapElement.getValue();
                    int currentBidInt = currentBidObject.getBidPrice();

                    if(lowestBid > currentBidInt){        //a new highest bid is found
                        lowestBid = currentBidInt;
                        lowestKey = (String)mapElement.getKey();
                    }
                }

                //display the lowest bidder
                System.out.println("The Lowest Bid on this Vehicle is: $" + lowestBid + " by the bidID: "+ lowestKey);

            }

        }

    }

    /**
     * deletes a bid for a vehicle via the bid ID
     *
     * also works similarly to displayHighestBidder, but instead of finding the highest bid price, it will
     * instead extract the bid's ID and compare if the bid ID the user inputs matches and when it does,
     * it deletes that element in the hashmap that has that bid ID
     */
    public void deleteBid(){
        ArrayList<String> vehicleIDArraylist = new ArrayList<String>();
        for(int i = 0; i < vehicleArrayList.size(); i++){       //make a temp list of all vehicle IDs

            vehicleIDArraylist.add(vehicleArrayList.get(i).getVehicleID());

        }

        System.out.print("Enter a vehicle ID: ");                   //ask for vehicle ID
        String vehicleID = scanner.nextLine();

        if(!(vehicleIDArraylist.contains(vehicleID))){                  //checks if this vehicle ID exists or not
            System.out.println("Vehicle ID does not exist");
            return;
        }

        System.out.print("Enter bidder ID: ");
        String bidderID = scanner.nextLine();

        for(int i = 0; i < vehicleArrayList.size(); i++){           //get the right car
            if(vehicleArrayList.get(i).getVehicleID().equals(vehicleID)){
                HashMap<String, Bid> tempBidHashmap = vehicleArrayList.get(i).getInstanceOfBidHashmap().getBidHashMap();

                if(tempBidHashmap.size() == 0){                 //exception?
                    System.out.println("There are currently no bids on this vehicle");
                    return;
                }

                for(Map.Entry mapElement : tempBidHashmap.entrySet()){
                    Bid currentBidObject = (Bid)mapElement.getValue();
                    String currentBidID = currentBidObject.getBidID();

                    String currentKey = (String)mapElement.getKey();

                    if(bidderID.equals(currentBidID)){
                        tempBidHashmap.remove(currentKey);
                        return;
                    }

                }
                System.out.println("That bid ID doesn't exist");    //if it leaves this for loop it means the ID was never found
                return;

            }
        }

    }

    /**
     * @param choice - the number inputted from AutoShowroomDriver is processed here
     *               depending on what number choice is, it will run any of the 9 methods above
     */
    public void menuChoices(int choice){     //this function shows some menu

        Scanner scanner = new Scanner(System.in);   //look at all of these menus

        switch (choice){
            case 1:
                createSedan();
                break;

            case 2:
                createTruck();
                break;

            case 3:
                createBidders();
                break;

            case 4:
                createBuyer();
                break;

            case 5:
                displayFleet();
                break;

            case 6:
                displayBuyers();
                break;

            case 7:
                displayHighestBidder();

            case 8:
                displayLowestBidder();

            case 9:
                deleteBid();
        }

    }

}
