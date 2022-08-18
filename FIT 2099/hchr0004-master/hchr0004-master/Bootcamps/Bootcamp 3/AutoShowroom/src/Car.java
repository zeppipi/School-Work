import java.util.ArrayList;

public class Car{   //cars

    private String maker;    //public because they need to be edited by AutoShowroom
    private String model;

    private ArrayList<Bid> bidArray = new ArrayList<Bid>();

    public void description(int counter){ //keeps maker name, model name, and counter

        System.out.println("Car "+ "(" + (counter+1) + ")" + " Maker:"+maker+" and Model:"+model);  //show car

    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public ArrayList<Bid> getBidArray() {
        return bidArray;
    }

    public void addBid(Buyer newBuyer, int price, String date){
        bidArray.add(new Bid(randomizerBidID(), date, price, newBuyer));       //insert a bid to the bidArray
    }

    private String randomizerBidID(){  //bidID made here
        String LettersAndString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";    //random letters and numbers

        StringBuilder sb = new StringBuilder(8);    //ID's will all be 8 characters long

        for (int i = 0; i < 8; i++ ){
            int index = (int)(LettersAndString.length()*Math.random());     //get 8 random numbers

            sb.append(LettersAndString.charAt(index));      //append sb with a character taken from the random number's position in the 'LettersAndString'
        }

        return sb.toString()+"BID";   //bidID made

        //this function is taken from https://www.geeksforgeeks.org/generate-random-string-of-given-size-in-java/

    }

}
