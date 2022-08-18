public class AutoShowroom{

    Car[] carArray;     //huh? instance array to Car

    public void printStatus(){  //will be responsible for printing anything
        System.out.println("Welcome to FIT2099 Showroom");
        createCars();
        displayCars();
        System.out.println("Thank you for visiting FIT2099 Showroom");
    }

    public void createCars(){     //this is unfortunately looks very hard coded
        carArray = new Car[3];

        carArray[0] = new Car();
        carArray[0].maker = "BMW";  carArray[0].model = "X7";

        carArray[1] = new Car();
        carArray[1].maker = "Audi";  carArray[1].model = "A8";

        carArray[2] = new Car();
        carArray[2].maker = "Mercedes";  carArray[2].model = "GLS";

    }

    public void displayCars(){
        for(int i = 0; i < carArray.length; i++){

            //loops between here to Car
            carArray[i].getCarDescription(carArray[i].maker, carArray[i].model, i);

        }
    }

}
