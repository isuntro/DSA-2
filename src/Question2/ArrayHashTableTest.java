package Question2;

import java.util.*;
/**
 * Created by 100125468 on 15/03/2017.
 */
public class ArrayHashTableTest {
    int n = 50000;
    ArrayHashTable table = new ArrayHashTable();
    Random rad = new Random();
    int[] numbers = new int[n];


    @org.junit.Test
    public void add() throws Exception {
        for(int c=0; c<n; c++){
            numbers[c] = Math.abs(rad.nextInt());
        }
        for(int i=0; i < n ; i++){
            table.add(numbers[i],i);
        }
        for(int i=0; i < n; i++){
            table.remove(numbers[i]);
        }
        for (int number : numbers) {
            System.out.println(table.contains(number));
        }

    }

    @org.junit.Test
    public void contains() throws Exception {
        for (int number : numbers) {
            System.out.println(table.contains(number));
        }
    }

}