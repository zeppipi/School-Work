import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class AutoShowroom{

    private ArrayList<Car> carArray = new ArrayList<Car>();     //array is now arraylist

    public void createCars(int choice){
        Scanner scanner = new Scanner(System.in);   //this function shows some menu

        switch (choice){
            case 1:     //sub menu starts here
                System.out.println("Here are the current cars with their bids and buyers:");
                if(carArray.size() == 0){
                    System.out.println("There are no cars currently\n");
                    break;
                }

                displayCars();

                break;
            case 2:
                System.out.println("Add Car Maker: ");
                String maker = scanner.nextLine();
                System.out.println("Add Car Model: ");
                String model = scanner.nextLine();

                carArray.add(new Car());    //car added to what the user inputted
                carArray.get(carArray.size()-1).setMaker(maker); carArray.get(carArray.size()-1).setModel(model);

                break;
            case 3:
                System.out.println("Here are the current cars with their bids and buyers:");
                displayCars();      //display the current cars

                if(carArray.size() == 0){
                    System.out.println("There are no cars to bid on currently\n");
                    break;
                }

                System.out.print("Which car do you want to add a bid?: ");
                int carChoice = scanner.nextInt();      //which car is the bid added too
                scanner.nextLine();      //scanner function bug, where it will skip a line when inputting an int then string

                if(carChoice > carArray.size() || carChoice < 1){
                    System.out.println("Car choice out of range\n");
                    break;
                }

                System.out.println("Add your Buyer ID: ");
                String buyerID = scanner.nextLine();
                System.out.println("Add your Given Name: ");
                String buyerGivenName = scanner.nextLine();
                System.out.println("Add your Family Name: ");
                String buyerFamilyName = scanner.nextLine();        //buyer information for the Buyer parameter

                System.out.println("Add your Bid Price: ");
                int bidPrice = Integer.parseInt(scanner.nextLine()); //scanner function bug, where it will skip a line when inputting an int then string
                System.out.println("Please Add Today's Date (day/month/year): ");
                String bidDate = scanner.nextLine();

                //carChoice - 1, because displayCars shows the cars with their index + 1 to make it more readable
                carArray.get(carChoice-1).addBid(new Buyer(buyerID, buyerGivenName, buyerFamilyName), bidPrice, bidDate);

                break;
        }

    }

    public void displayCars(){

        for(int i = 0; i < carArray.size(); i++){
            carArray.get(i).description(i);     //loops through the car descriptions

            ArrayList<Bid> temp = carArray.get(i).getBidArray();        //makes temp access the elements in bidArray
            for (int index = 0; index < carArray.get(i).getBidArray().size(); index++){
                System.out.println(temp.get(index).description());      //loops through buyer and bidder description
            }

        }
    }

}
