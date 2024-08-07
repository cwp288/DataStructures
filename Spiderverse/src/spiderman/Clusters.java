package spiderman;

import java.util.ArrayList;

import org.w3c.dom.Node;

/**
 * Steps to implement this class main method:
 * 
 * Step 1:
 * DimensionInputFile name is passed through the command line as args[0]
 * Read from the DimensionsInputFile with the format:
 * 1. The first line with three numbers:
 *      i.    a (int): number of dimensions in the graph
 *      ii.   b (int): the initial size of the cluster table prior to rehashing
 *      iii.  c (double): the capacity(threshold) used to rehash the cluster table 
 * 
 * Step 2:
 * ClusterOutputFile name is passed in through the command line as args[1]
 * Output to ClusterOutputFile with the format:
 * 1. n lines, listing all of the dimension numbers connected to 
 *    that dimension in order (space separated)
 *    n is the size of the cluster table.
 * 
 * @author Seth Kelley
 */

 public class Clusters {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Execute: java -cp bin spiderman.Clusters <dimensionInputFile> <clusterOutputFile>");
            return;
        }
        String dimensionInputFile = args[0];
        String clusterOutputFile = args[1];
        Cluster[] clusterTable = createHashTable(dimensionInputFile);
        printOutput(clusterTable, clusterOutputFile);
    }

     public static int hash(int dimnum, int tableSize) {
        return dimnum % tableSize;
    }
    public static Cluster[] rehash(Cluster[] oldClusters, int newSize) {
        Cluster[] newClusters = new Cluster[newSize];
        for (int i = 0; i < newSize; i++) {
            newClusters[i] = new Cluster(-1, null); 
        }
        for (Cluster oldCluster : oldClusters) {
            Cluster currentCluster = oldCluster;
            while (currentCluster != null) {
                int item = currentCluster.item;
                int newHashValue = hash(item, newSize);
                Cluster newCluster = new Cluster(item, null);
                Cluster dummyNode = newClusters[newHashValue];
                Cluster nextNode = dummyNode.next;
                dummyNode.next = newCluster;
                newCluster.next = nextNode;
    
                currentCluster = currentCluster.next;
            }
        }
            for (int i = 0; i < newSize; i++) {
            newClusters[i] = newClusters[i].next;
        }
        return newClusters;
    }
   public static Cluster[] createHashTable(String file) {
    StdIn.setFile(file);
    int numDimensions = StdIn.readInt();
    int initialSize = StdIn.readInt();
    int loadFactor = StdIn.readInt();
    int capacity = initialSize * loadFactor;
    Cluster[] table = new Cluster[initialSize];
    for (int i = 0; i < numDimensions; i++) {
        int dimension = StdIn.readInt();
        StdIn.readInt(); // Read and ignore
        StdIn.readInt(); // Read and ignore
        int index = hash(dimension, initialSize);
        table[index] = new Cluster(dimension, table[index]);
        if (i + 1 == capacity) {
            initialSize *= 2;
            capacity = initialSize * loadFactor;
            table = rehash(table, initialSize);
        }
    }
    for (int j = 0; j < table.length; j++) {
        if (table[j] != null) {
            int prevIndex = (j == 0) ? table.length - 1 : j - 1;
            int prevPrevIndex = (j < 2) ? table.length - 2 + j : j - 2;
            Cluster lastNode = getLastNode(table[j]);
            lastNode.next = new Cluster(table[prevIndex].item, new Cluster(table[prevPrevIndex].item, null));
        }
    }
    return table;
}
 public static Cluster getLastNode(Cluster head) {
        Cluster current = head;
        while (current.next != null) {
            current = current.next;
        }
        return current;
    }     
   public static void printOutput(Cluster[] table, String file) {
    StdOut.setFile(file);
    for(int x = 0;x < table.length;x++) {
        Cluster ptr = table[x];
        while(ptr != null) {
            StdOut.print(ptr.item);
            StdOut.print(" ");
            ptr = ptr.next;
        }
        StdOut.println();
    }
   }
}