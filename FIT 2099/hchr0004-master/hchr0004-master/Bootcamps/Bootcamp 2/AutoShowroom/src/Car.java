public class Car{   //cars

    public String maker;    //public because they need to be edited by AutoShowroom
    public String model;

    public void getCarDescription(String maker, String model, Integer counter){ //keeps maker name, model name, and counter

        System.out.println("Car "+ "(" + (counter+1) + ")" + " Maker:"+maker+" and Model:"+model);  //show car

    }

}
