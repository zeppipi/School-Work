package edu.monash.fit2099;

public class AutoShowroomDriver {

    public static void main(String[] args) {
        AutoShowroom pi = new AutoShowroom();   //start here

        int selection;
        do{
            selection = pi.selectMainMenuItem();
            if((selection >= 1) && (selection < 7)){    //DRY gone

                pi.menuChoices(selection);

            }
            else if(selection == 7){

                System.out.println("Thank you for visiting FIT2099 Showroom");

            }
        }while(selection != 7);

    }

}
