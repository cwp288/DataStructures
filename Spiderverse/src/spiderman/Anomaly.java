package spiderman;
import java.util.ArrayList;

public class Anomaly {
    String name;
    int homeDimension;
    int allottedTime;
    int canonEvents;
    boolean success;
    int totaltime;
    ArrayList<Integer> path; 
    
    public Anomaly(String name, int allottedTime) {
        this.canonEvents = 1; 
        this.success = false;
        this.path = new ArrayList<>();
        this.name = name;
        this.allottedTime = allottedTime;
    }
    public void setPath(ArrayList<Integer> path) {
        this.path = path;
    }
    public ArrayList<Integer> getPath() {
        return path;
    }
}