package edu.monash.fit2099;

import edu.monash.fit2099.bids.BidsManager;
import edu.monash.fit2099.buyers.Buyer;
import edu.monash.fit2099.vehicles.Sedan;
import edu.monash.fit2099.vehicles.Truck;
import edu.monash.fit2099.vehicles.Vehicle;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class AutoShowroom{

    private Scanner scanner = new Scanner(System.in);
    private ArrayList<Vehicle> vehicleArrayList = new ArrayList<Vehicle>();     //arraylist of vehicles
    private ArrayList<Buyer> buyerArrayList = new ArrayList<Buyer>();           //arraylist of buyers

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
        System.out.println("7. Exit");
        System.out.print("Please Pick Your Option: ");

        int choice = scanner.nextInt();
        return choice;
    }

    public void createSedan(){                                      //sedan creator

        System.out.print("Add Sedan Maker: ");                      //ask sedan's maker
        String maker = scanner.nextLine();

        System.out.print("Add Sedan Model: ");                      //ask sedan's model
        String model = scanner.nextLine();

        System.out.print("Add the amount of seats: ");              //ask sedan's seats
        int seats = Integer.parseInt(scanner.nextLine());

        String sedanID = Vehicle.randomizerID();                    //make sedan's ID

        Sedan sedanInfo = new Sedan(maker, model, sedanID, seats);  //need variable to keep a new sedan
        vehicleArrayList.add(sedanInfo);                            //sedan added to vehicleArray

    }

    public void createTruck(){                                          //truck creator

        System.out.print("Add Truck Maker: ");                          //ask truck's maker
        String maker = scanner.nextLine();

        System.out.print("Add Truck Model: ");                          //ask truck's model
        String model = scanner.nextLine();

        System.out.print("Add the amount of wheels: ");                 //ask truck's wheels
        int wheels = Integer.parseInt(scanner.nextLine());

        System.out.print("Add the amount of capacity (in Tons): ");     //ask truck's wheels
        int capacity = Integer.parseInt(scanner.nextLine());

        String truckID = Vehicle.randomizerID();                        //make truck's ID

        Truck truckInfo = new Truck(maker, model, capacity, wheels);    //need variable to keep a new truck
        vehicleArrayList.add(truckInfo);                                //truck added to vehicleArray

    }

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

        Buyer buyerInfo = new Buyer(buyID, givenName, familyName);  //need variable to keep a new buyer
        buyerArrayList.add(buyerInfo);                              //buyer added to buyerArray

    }

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
        int bidPrice = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter the date (day/month/year): ");      //ask for date
        String bidDate = scanner.nextLine();

        for (int i = 0; i < vehicleArrayList.size(); i++) {

            if(vehicleArrayList.get(i).getVehicleID().equals(vehicleID)){
                vehicleArrayList.get(i).getInstanceOfBidHashmap().addBid(buyerID, bidPrice, bidDate);
            }

        }
    }

    public void displayFleet(){     //display vehicles

        for(int i = 0; i < vehicleArrayList.size(); i++){

            vehicleArrayList.get(i).description();

        }

    }

    public void displayBuyers(){

        for(int i = 0; i < buyerArrayList.size(); i++){

            buyerArrayList.get(i).description();

        }
    }

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
        }

    }

}
