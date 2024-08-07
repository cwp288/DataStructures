package spiderman;

import java.util.ArrayList;

public class Dimension {
    int number;
    int weight;
    int canon;
    public Dimension(int number, int weight, int canon) {
        this.number = number;
        this.weight = weight;
        this.canon = canon;
    }
    public static ArrayList<Dimension> readIndim(String input) {
        ArrayList<Dimension> dimensons = new ArrayList<>();
        StdIn.setFile(input);
        int amount = StdIn.readInt();
        StdIn.readInt();
        StdIn.readInt();
        for(int x = 0; x < amount;x++) {
            int number = StdIn.readInt();
            int canon = StdIn.readInt();
            int weight = StdIn.readInt();
            dimensons.add(new Dimension(number, weight, canon));
        }
        return dimensons;
    }
    public static Dimension finddim(int number,  ArrayList<Dimension> dimensions) {
        for(Dimension x : dimensions) {
            if(x.number == number) {
                return x;
            }
        }
        return null;
    }
}

    // Add a method to add a linked dimension

    /*
     * private int dimensionNumber;
    private int canonEvents;
    private int dimensionWeight;

    public Dimension(int dimensionNumber, int canonEvents, int dimensionWeight) {
        this.dimensionNumber = dimensionNumber;
        this.canonEvents = canonEvents;
        this.dimensionWeight = dimensionWeight;
        this.next = null;
        this.people = new ArrayList<>();
        this.linkedDimensions = new ArrayList<>(); // Initialize linkedDimensions
    }

    public int getDimensionNumber() {
        return dimensionNumber;
    }

    public int getCanonEvents() {
        return canonEvents;
    }

    public int getDimensionWeight() {
        return dimensionWeight;
    }

    public Dimension getNextDimension() {
        return next;
    }

    public void setNextDimension(Dimension next) {
        this.next = next;
    }

    public boolean equals(Dimension other) {
        return this.dimensionNumber == other.dimensionNumber;
    }

    public void addPerson(Person person) {
        people.add(person);
    }
    public void addLinkedDimension(Dimension dimension) {
        linkedDimensions.add(dimension);
    }

    // Add a method to get the people in this dimension
    public ArrayList<Person> getPeople() {
        return people;
    }

    // Add a method to get the linked dimensions
    public ArrayList<Dimension> getLinkedDimensions() {
        return linkedDimensions;
    }
     */