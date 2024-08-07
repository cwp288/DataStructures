package spiderman;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
 * 2. a lines, each with:
 *      i.    The dimension number (int)
 *      ii.   The number of canon events for the dimension (int)
 *      iii.  The dimension weight (int)
 * 
 * Step 2:
 * SpiderverseInputFile name is passed through the command line as args[1]
 * Read from the SpiderverseInputFile with the format:
 * 1. d (int): number of people in the file
 * 2. d lines, each with:
 *      i.    The dimension they are currently at (int)
 *      ii.   The name of the person (String)
 *      iii.  The dimensional signature of the person (int)
 * 
 * Step 3:
 * HubInputFile name is passed through the command line as args[2]
 * Read from the HubInputFile with the format:
 * One integer
 *      i.    The dimensional number of the starting hub (int)
 * 
 * Step 4:
 * CollectedOutputFile name is passed in through the command line as args[3]
 * Output to CollectedOutputFile with the format:
 * 1. e Lines, listing the Name of the anomaly collected with the Spider who
 *    is at the same Dimension (if one exists, space separated) followed by 
 *    the Dimension number for each Dimension in the route (space separated)
 * 
 * @author Seth Kelley
 */

public class CollectAnomalies {
    
      public static void main(String[] args) {
            if (args.length < 4) {
                StdOut.println("Execute: java -cp bin spiderman.CollectAnomalies <dimension input file> <spiderverse input file> <hub input file> <output file>");
                return;
            }
            String dimensionInputFile = args[0];
            String spiderverseInputFile = args[1];
            String hubInputFile = args[2];
            String anomaliesInputFile = args[3];
            processAnomalies(dimensionInputFile, spiderverseInputFile, hubInputFile, anomaliesInputFile);
        }
        public static void processAnomalies(String input1, String input2, String input3, String input4) {
            Cluster[] adj = Collider.getAdjacencyNodes(input1);
            ArrayList<Person> spiderverse = Person.readInSpiderverse(input2);
            StdIn.setFile(input3);
            int hub = StdIn.readInt();
            CollectAnomalies coll = new CollectAnomalies();
            coll.findAnomalies(adj, spiderverse, hub, input4);
        }
            public String BinarySearchPathway(Cluster[] adj, int start, int end) {
                int[] parent = new int[adj.length];
                Arrays.fill(parent, -1);
                Queue<Integer> queue = new LinkedList<>();
                boolean[] visited = new boolean[adj.length];
                StringBuilder path = new StringBuilder();
                queue.add(start);
                int index = Collider.findIndex(adj, new Cluster(start, null));
                visited[index] = true;
                while (!queue.isEmpty()) {
                    int current = queue.poll();
                    if (current == end) {
                        while (current != -1) {
                            path.insert(0, current).insert(0, " ");
                            int indexrr = Collider.findIndex(adj, new Cluster(current, null));
                            current = parent[indexrr];
                        }
                        return path.toString().trim();
                    }
                    int indexrr = Collider.findIndex(adj, new Cluster(current, null));
                    Cluster ptr = adj[indexrr];
                    while (ptr != null) {
                        int neighbor = ptr.item;
                        int indexr = Collider.findIndex(adj, new Cluster(neighbor, null));
                        if (!visited[indexr]) {
                            visited[indexr] = true;
                            parent[indexr] = current;
                            queue.add(neighbor);
                        }
                        ptr = ptr.next;
                    }
                }
                
                return "";
            }
            public void findAnomalies(Cluster[] adj, ArrayList<Person> spiderverse, int hub, String output) {
                ArrayList<String> bruh = new ArrayList<>();
                    for (Person spider : spiderverse) {
                    if (spider.dimension != spider.signature && spider.dimension != hub) {
                        String anomalyName = spider.name;
                        String spiderName = Person.findPerson(spider.dimension, spiderverse);
                        if (anomalyName.equals(spiderName)) {
                            bruh.add(anomalyName + " " + BinarySearchPathway(adj, hub, spider.dimension) + " " + reverseOrder(BinarySearchPathway(adj, hub, spider.dimension)));
                        } else {
                            bruh.add(anomalyName + " " + spiderName + " " + reverseOrder1(BinarySearchPathway(adj, hub, spider.dimension)));
                        }     
                    }
                    }
                    printOutput(bruh, output);
                }     
        public String reverseOrder(String input) {
            String[] numbers = input.split("\\s+");
            LinkedList<String> reversedList = new LinkedList<>();
                for (int i = 0; i < numbers.length - 1; i++) {
                    reversedList.addFirst(numbers[i]);
                }
                
                return String.join(" ", reversedList);
            }
        public String reverseOrder1(String input) {
            return Stream.of(input.trim().split("\\s+"))
                     .reduce((word, line) -> line + " " + word)
                     .orElse("");
        }

        private void printOutput(ArrayList<String> bruh, String output) {
            StdOut.setFile(output);
              for (String line : bruh) {
                StdOut.println(line);
              }
            
          }

}