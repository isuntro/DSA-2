package Question2;

import Question1.Matrix;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

/**
 * Created by tiberiusimionvoicu on 14/03/2017.
 */
public class ArrayHashTable<K,V> {
    private Entry<K,V>[][] table;
    private int size = 0;
    private int[] counts;
    private final int capacity = 10;
    private int chainSize = 5;

    /**
     *
     */
    public ArrayHashTable(){
        table = new Entry[capacity][];
        for(int i=0; i<capacity; i++){
            table[i] = null;
        }
        counts = new int[10];
    }

    /**
     *
     * key
     * @param value
     * @return
     */
    public boolean add(V value){
        int hash = value.hashCode();
        int hashIndex = (hash % capacity);
        int index = -1;
        if(table[hashIndex] == null){
            table[hashIndex] = new Entry[chainSize];
            table[hashIndex][0] = new Entry(hash,value);
            size++;
            counts[hashIndex]++;
            if(table[hashIndex][0] == null){
                System.out.println(" NULL ELEMENT ADDED");
            }
            return true;
        }
        else
        for(int i = 0; i < table[hashIndex].length; i++){
            if(table[hashIndex][i] != null) {
                if (table[hashIndex][i].getKey().equals(value)) {
                    return false;
                }
            }
            else if(index == -1){
                index = i;
            }
        }
        if(index != -1){
            table[hashIndex][index] = new Entry(hash,value);
            size++;
            counts[hashIndex]++;
            if(table[hashIndex][index] == null){
                System.out.println(" NULL ELEMENT ADDED");
            }
            return true;
        }
        int length = table[hashIndex].length;
        if(counts[hashIndex] == length) {
            if (table[hashIndex][length - 1].getKey() != null) {
                Entry[] chain = new Entry[length * 2];
                System.arraycopy(table[hashIndex], 0, chain, 0, length);
                table[hashIndex] = chain;
                table[hashIndex][counts[hashIndex]] = new Entry(hash, value);
                size++;
                counts[hashIndex]++;
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @return
     */
    public boolean contains(V value){
        int hashIndex = value.hashCode() % capacity;
        for(int c = 0; c<counts[hashIndex]; c++){
            if(table[hashIndex][c].getKey().equals(value)){
                return true;
            }
        }
        return false;
    }

    /** Wont work cause of implementation of add
     *  CHANGE IT
     *  CURRENTLY ADD AND CONTAINS WORKING PROPERLY
     *  CHECK IF SHOULD BE AS SPECIFICATION
     *
     * @return
     */
    public boolean remove(V value){
        int hashIndex = value.hashCode() % capacity;
        if(counts[hashIndex] == 0 ){
            return false;
        }
        else
        for(int i = 0; i < table[hashIndex].length; i++){
            if(table[hashIndex][i] != null) {
                if (table[hashIndex][i].getKey().equals(value)) {
                    table[hashIndex][i] = null;
                    size--;
                    counts[hashIndex]--;
                    // if number of items in the chain is half its length
                    // make the chains length a half of the original
                    // and copy the values over
                    // G
                    if(counts[hashIndex] == table[hashIndex].length/2 && counts[hashIndex] >= 5){
                        Entry[] chain = new Entry[counts[hashIndex]];
                        System.arraycopy(table[hashIndex],0,chain,0,counts[hashIndex]);
                        table[hashIndex] = chain;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *
     * @param <K>
     * @param <V>
     */
    private static class Entry<K,V> implements Map.Entry<K,V>{
        private K key;
        private V value;
        public Entry(){
            key = null;
            value = null;
        }
        public Entry(K key, V value){
            this.key = key;
            this.value = value;
        }
        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            return this.value = value;
        }
        public String toString(){
            String str = "";
            str += "Entry Key : " + key;
            str += " Entry Value : " + value;
            return str;
        }
    }
    public static int[] testData(int n){
        int[] numbers = new int[n];
        Random rad=new Random();
        for(int j=0;j<n;j++){
            numbers[j] = Math.abs(rad.nextInt());
        }
        return numbers;
    }
    public static double[] experiment(ArrayHashTable aTable, int reps, int n){
        int[] data;
        double sum = 0,sumSet = 0;
        double sumSquared = 0,sumSquaredSet = 0;

        double[] dataRes = new double[7];
        data = testData(n);
        HashSet testSet = new HashSet(10);
        for(int i=0; i<reps; i++) {
            long t1=System.nanoTime();

            for (int c = 0; c < n; c++) {
                aTable.add(data[c]);
            }
            for (int c = 0; c < n; c++) {
                aTable.remove((data[c]));
            }

            long t2=System.nanoTime()-t1;

            long t3=System.nanoTime();
            for (int c = 0; c < n; c++) {
                testSet.add(data[c]);
            }
            for (int c = 0; c < n; c++) {
                testSet.remove((data[c]));
            }
            long t4 = System.nanoTime()-t3;

            sumSet += t4/1000000.0;
            sumSquaredSet += (t4/1000000.0)*(t4/ 1000000.0);
            sum += t2/1000000.0;
            sumSquared+=(t2/1000000.0)*(t2/1000000.0);
        }
        double meanSet = sumSet/reps;
        double varianceSet = sumSquaredSet/reps-(meanSet*meanSet);
        double stdDevSet = Math.sqrt(varianceSet);
        double mean=sum/reps;
        double variance=sumSquared/reps-(mean*mean);
        double stdDev=Math.sqrt(variance);
        dataRes[0] = n;
        dataRes[1] = mean;
        dataRes[2] = variance;
        dataRes[3] = stdDev;
        dataRes[4] = meanSet;
        dataRes[5] = varianceSet;
        dataRes[6] = stdDevSet;
        return dataRes;
        }
    public static void runExperiments(ArrayHashTable aTable) throws IOException {
        double[] results;
        int n = 0;
        while ( n < 50000){
            if( n < 10000){
                n += 1000;
            }
            else
                n += 5000;
            results = experiment(aTable,1000,n);
            Matrix.writeCsv(results, "tableData1.csv");
        }

    }
    public String toString(){
        String str = "";
        for(int i=0; i<10; i++){
            str = str + " Index " + i + " Counts : " + counts[i] + "\n";
            for(int c = 0; c < table[i].length; c++){
                if(table[i][c] != null){
                    str += table[i][c];
                    str += "\n";
                }
            }
        }
        return str;
    }
    public static void main(String[] args) {
        ArrayHashTable aTable = new ArrayHashTable();
        int[] keys = testData(50);

        for(int i=0; i<keys.length; i++){
            aTable.add(i);
            if(i == 10) {
                System.out.println(" REMOVE "+ i +  " size" + aTable.size + aTable );
            }
            else if(i == 20) {
                System.out.println(" REMOVE "+ i +  " size " + aTable.size + aTable );
            }
            else if(i == 35) {
                System.out.println(" REMOVE "+ i +  " size " + aTable.size + aTable );
            }
            else if(i == 50) {
                System.out.println(" REMOVE "+ i +  " size " + aTable.size + aTable );
            }
        }
        for(int i=0; i<keys.length; i++){
            aTable.remove(i);
             if(i == 10) {
                 System.out.println(" REMOVE "+ i +  " size " + aTable.size + aTable );
            }
            else if(i == 20) {
                 System.out.println(" REMOVE  "+ i +  " size " + aTable.size + aTable );
            }
            else if(i == 35) {
                 System.out.println(" REMOVE "+ i +  " size " + aTable.size + aTable );
            }
            else if(i == 49) {
                System.out.println(" REMOVE "+ i +  " size" + aTable.size + aTable );
            }
        }
/*
        try {
            runExperiments(aTable);
        } catch (IOException e) {
            e.printStackTrace();
        }
*/

    }

}
