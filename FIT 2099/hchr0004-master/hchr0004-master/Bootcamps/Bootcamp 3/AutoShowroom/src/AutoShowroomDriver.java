import java.util.Scanner;

public class AutoShowroomDriver {

    public static void main(String[] args) {
        AutoShowroom pi = new AutoShowroom();   //start here

        int selection;
        do{
            selection = selectMainMenuItem();
            switch(selection){
                case 1:
                    pi.createCars(selection);
                    break;
                case 2:
                    pi.createCars(selection);        //adding cars and bidding happens in the same function
                    break;
                case 3:
                    pi.createCars(selection);
                    break;
                case 4:
                    System.out.println("Thank you for visiting FIT2099 Showroom");
            }
        }while(selection != 4);

    }

    public static int selectMainMenuItem(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to FIT2099 Showroom");
        System.out.println("--------------------------");
        System.out.println("1. Show Showroom");
        System.out.println("2. Add car");
        System.out.println("3. Add Bid");
        System.out.println("4. Exit");
        System.out.print("Please Pick Your Option: ");

        int choice = scanner.nextInt();
        return choice;
    }

}
