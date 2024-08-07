package spiderman;
import java.util.*;
public class Person {
    public int dimension;
    public String name;
    public int signature;
    public Person(int dim, String name, int sig) {
        this.dimension = dim;
        this.name = name;
        this.signature = sig;
    }
    public static ArrayList<Person> readInSpiderverse(String input) {
        ArrayList<Person> spiderverse = new ArrayList<>();
        StdIn.setFile(input);
        int amount = StdIn.readInt();
        for(int x = 0; x < amount;x++) {
            int dimension = StdIn.readInt();
            String name = StdIn.readString();
            int dimensionalSignature = StdIn.readInt();
            spiderverse.add(new Person(dimension, name, dimensionalSignature));
        }
        return spiderverse;
    }
    public String getName(){
        return this.name;
    }   
    public int getDimension(){
        return this.dimension;
    }
     public static String findPerson(int dimension,  ArrayList<Person> spiderverse) {
        for(Person x : spiderverse) {
            if(x.dimension == dimension) {
                return x.name;
            }
        }
        return " ";
    }
    public static int findPerson(String name, ArrayList<Person> spiderverse) {
        for(Person x : spiderverse) {
            if(x.name.equals(name)) {
                return x.signature;
            }
        }
        return 0;
    }
   
}
