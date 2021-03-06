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
     * @param key
     * @param value
     * @return
     */
    public boolean add(K key, V value){
        int hash = (key.hashCode() % capacity);

        if(table[hash] == null){
            table[hash] = new Entry[chainSize];
            table[hash][0] = new Entry<>(key,value);
            size++;
            counts[hash]++;
            return true;
        }
        else
        for(int i = 0; i < counts[hash]; i++){
            if(table[hash][i].getKey() == key ){
                return false;
            }
        }
        int length = table[hash].length;
        if(counts[hash] == length) {
            if (table[hash][length - 1].getKey() != null) {
                Entry[] chain = new Entry[length * 2];
                System.arraycopy(table[hash], 0, chain, 0, length);
                table[hash] = chain;
                table[hash][length] = new Entry<>(key, value);
                size++;
                counts[hash]++;
            }
        }
        else{
            /*
            for(int i=0; i<length; i++){
                if(table[hash][i].equals(null)){
                    table[hash][i] = new Entry<>(key,value);
                    break;
                }
            }
            */
            table[hash][counts[hash]] = new Entry<>(key,value);

            size++;
            counts[hash]++;
        }
        return true;
    }

    /**
     *
     * @param key
     * @return
     */
    public boolean contains(K key){
        int hash = key.hashCode() % capacity;
        for(int c = 0; c<counts[hash]; c++){
            if(table[hash][c].getKey().equals(key)){
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
     * @param key
     * @return
     */
    public boolean remove(K key){
        int hash = key.hashCode() % capacity;
        if(counts[hash] == 0 ){
            return false;
        }
        else
        for(int i = 0; i < counts[hash]; i++){
            if(table[hash][i].getKey().equals(key)){
                table[hash][i] = null;
                size--;
                counts[hash]--;
                System.arraycopy(table[hash],i+1,table[hash],i,counts[hash]-i);
                return true;
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
        K key;
        V value;
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
            str += "Entry Value : " + value;
            return str;
        }
    }
    public static int[] testData(int n){
        int[] numbers = new int[n];
        Random rad=new Random();
        for(int j=0;j<n;j++){
            numbers[j]=Math.abs(rad.nextInt());
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
                aTable.add(data[c], c);
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

            sumSet += t4/10000.0;
            sumSquaredSet += (t4/10000.0)*(t4/ 10000.0);
            sum += t2/10000.0;
            sumSquared+=(t2/10000.0)*(t2/10000.0);
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
            for(int c = 0; c < counts[i]; c++){
                str += table[i][c];
            }
        }
        return str;
    }
    public static void main(String[] args) {
        ArrayHashTable aTable = new ArrayHashTable();

        try {
            runExperiments(aTable);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
