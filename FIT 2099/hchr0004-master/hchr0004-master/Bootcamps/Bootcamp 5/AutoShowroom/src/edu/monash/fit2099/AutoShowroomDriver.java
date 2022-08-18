package edu.monash.fit2099;

/**
 * @author Hazael Frans Christian
 * @version September 1st, 2021
 */

/**
 * This is the AutoShowroomDriver, which is there the program starts
 * this class includes nothing but a PSVM (Public Static Void Main) method
 */
public class AutoShowroomDriver {

    /**
     * This method only handles user input for the selectMainMenu method in the AutoShowroom class
     * @see AutoShowroom
     *
     *
     * Users will be inputting a number from 1 to 9, and any of those numbers will be sent to AutoShowroom
     * if 10 is inputted, then the program will end
     *
     * @param args - runs the method
     */
    public static void main(String[] args) {

        AutoShowroom pi = new AutoShowroom();   //start here

        int selection;
        do{
            selection = pi.selectMainMenuItem();
            if((selection >= 1) && (selection < 10)){    //DRY gone

                pi.menuChoices(selection);

            }
            else if(selection == 10){

                System.out.println("Thank you for visiting FIT2099 Showroom");

            }
        }while(selection != 10);

    }

}
