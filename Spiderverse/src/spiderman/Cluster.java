package spiderman;
public class Cluster {
    int item;
    Cluster next;
    int weight;
    Cluster(int item, Cluster ne) {
        this.item = item;
        this.next = ne;
    }
}