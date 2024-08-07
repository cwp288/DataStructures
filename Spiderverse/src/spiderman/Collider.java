package spiderman;

import java.util.ArrayList;
import java.util.List;

public class Collider {

    public static void main(String[] args) {
        if (args.length < 3) {
            StdOut.println("Execute: java -cp bin spiderman.Collider <dimension INput file> <spiderverse INput file> <collider OUTput file>");
            return;
        }

        String dimensionInputFile = args[0];
        String spiderverseInputFile = args[1];
        String colliderOutputFile = args[2];

        Cluster[] adjacencyList = getAdjacencyNodes(dimensionInputFile);
        Clusters.printOutput(adjacencyList, colliderOutputFile);

    }

    public static int findIndex(Cluster[] adj, Cluster temp) {
        for (int x = 0; x < adj.length; x++) {
            if (adj[x].item == temp.item)
                return x;
        }
        return -1;
    }
    public static Cluster[] getAdjacencyNodes(String dimensionInput) {
        StdIn.setFile(dimensionInput);
        int numDimensions = StdIn.readInt();
        int initialSize = StdIn.readInt();
        int loadFactor = StdIn.readInt();
        int capacity = initialSize * loadFactor;
        Cluster[] clusters = new Cluster[initialSize];
        Cluster[] adjacencyNodes = new Cluster[numDimensions];

        for (int i = 0; i < numDimensions; i++) {
            int dimensionNumber = StdIn.readInt();
            adjacencyNodes[i] = new Cluster(dimensionNumber, null);
            StdIn.readInt();  // Skip unused data
            StdIn.readInt();  // Skip unused data
            int hashIndex = Clusters.hash(dimensionNumber, initialSize);
            clusters[hashIndex] = insertCluster(clusters[hashIndex], dimensionNumber);

            if (i + 1 == capacity) {
                initialSize *= 2;
                capacity = initialSize * loadFactor;
                clusters = Clusters.rehash(clusters, initialSize);
            }
        }

        linkWrapAroundNodes(clusters);
        buildAdjacencyList(clusters, adjacencyNodes);
        return adjacencyNodes;
    }

    public static Cluster insertCluster(Cluster head, int dimensionNumber) {
        if (head == null) {
            return new Cluster(dimensionNumber, null);
        } else {
            return new Cluster(dimensionNumber, head);
        }
    }

    public static void linkWrapAroundNodes(Cluster[] clusters) {
        int length = clusters.length;
        for (int i = 0; i < length; i++) {
            if (clusters[i] != null) {
                Cluster last = Clusters.getLastNode(clusters[i]);
                last.next = new Cluster(getItemSafe(clusters, i - 1, length), new Cluster(getItemSafe(clusters, i - 2, length), null));
            }
        }
    }

    public static int getItemSafe(Cluster[] clusters, int index, int length) {
        if (index < 0) index += length;
        return clusters[index % length].item;
    }
    public static void buildAdjacencyList(Cluster[] clusters, Cluster[] adj) {
        for (int i = 0; i < clusters.length; i++) {
            if (clusters[i] != null) {
                Cluster current = clusters[i].next;
                while (current != null) {
                    int index = findIndex(adj, clusters[i]);
                    addToAdjacencyList(adj, index, current.item);
                    int backIndex = findIndex(adj, current);
                    addToAdjacencyList(adj, backIndex, clusters[i].item);
                    current = current.next;
                }
            }
        }
    }

    public static void addToAdjacencyList(Cluster[] adj, int index, int item) {
        Cluster tail = Clusters.getLastNode(adj[index]);
        tail.next = new Cluster(item, null);
    }
}
