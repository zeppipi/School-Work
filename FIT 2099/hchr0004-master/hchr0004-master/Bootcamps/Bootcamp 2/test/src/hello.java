import java.lang.reflect.Array;
import java.util.ArrayList;

public class hello {

    public static void main(String[] args) {

        ArrayList<Integer> aList =  new ArrayList<Integer>(10);
        int[] a = new int[10];

        ArrayList<Double> arrayListName = new ArrayList<Double>();
        arrayListName.add(0,10.0);
        arrayListName.add(1,45.6);
        arrayListName.add(2,21.9);
        arrayListName.add(3,8.01);
        arrayListName.add(1,5.65);
        arrayListName.remove(arrayListName.size()-2);
        arrayListName.clear();

        System.out.println(arrayListName.toString());
        
        aList.add(a[0]);
        
        String.format("%1f, %2f, %3f, %4f, %%5f ");

        test array[] = new test[6];
        array[5] = new test();

    }

}
