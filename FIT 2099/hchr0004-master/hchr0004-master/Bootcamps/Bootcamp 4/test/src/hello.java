import java.util.function.ToIntFunction;
import java.util.regex.Pattern;

public class hello {

    public static void main(String[] args) {
        String date = "25/08/2021";

        int day = Integer.parseInt(date.substring(0,1));
        int month = Integer.parseInt(date.substring(3,4));
        int year = Integer.parseInt(date.substring(6));
        Pattern dateFormat = Pattern.compile("\\d{2}/\\d{2}/\\d{4}");

        if(dateFormat.matcher(date).matches()){
            System.out.println("following format");
        }else{
            System.out.println("no");
        }


    }

}
