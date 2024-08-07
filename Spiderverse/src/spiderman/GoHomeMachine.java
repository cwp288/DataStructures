package spiderman;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
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
 * Read from the SpotInputFile with the format:
 * One integer
 *      i.    The dimensional number of the starting hub (int)
 * 
 * Step 4:
 * AnomaliesInputFile name is passed through the command line as args[3]
 * Read from the AnomaliesInputFile with the format:
 * 1. e (int): number of anomalies in the file
 * 2. e lines, each with:
 *      i.   The Name of the anomaly which will go from the hub dimension to their home dimension (String)
 *      ii.  The time allotted to return the anomaly home before a canon event is missed (int)
 * 
 * Step 5:
 * ReportOutputFile name is passed in through the command line as args[4]
 * Output to ReportOutputFile with the format:
 * 1. e Lines (one for each anomaly), listing on the same line:
 *      i.   The number of canon events at that anomalies home dimensionafter being returned
 *      ii.  Name of the anomaly being sent home
 *      iii. SUCCESS or FAILED in relation to whether that anomaly made it back in time
 *      iv.  The route the anomaly took to get home
 * 
 * @author Seth Kelley
 */

public class GoHomeMachine {
    
    public static void main(String[] args) {

        if ( args.length < 5 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.GoHomeMachine <dimension INput file> <spiderverse INput file> <hub INput file> <anomalies INput file> <report OUTput file>");
                //java -cp bin spiderman.GoHomeMachine dimension.in spiderverse.in hub.in anomalies.in report.out
                return;
        }
            String dimensionInputFile = args[0];
            String spiderverseInputFile = args[1];
            String hubInputFile = args[2];
            String anomaliesInputFile = args[3];
            String outputFile = args[4];
            
            ex(dimensionInputFile, spiderverseInputFile, hubInputFile, anomaliesInputFile, outputFile);
        }
        
        public static void ex(String dimensionInputFile, String spiderverseInputFile, String hubInputFile, String anomaliesInputFile, String outputFile) {            
            Cluster[] adj = Collider.getAdjacencyNodes(dimensionInputFile);
            ArrayList<Person> spider = Person.readInSpiderverse(spiderverseInputFile);
            StdIn.setFile(hubInputFile);
            int hubDimension = StdIn.readInt();
            ArrayList<Anomaly> anomalies = readA(anomaliesInputFile);
            ArrayList<Dimension> dimensions = Dimension.readIndim(dimensionInputFile);
            for (int i = 0; i < anomalies.size(); i++) {
                Anomaly x = anomalies.get(i);
                x.setPath(dijkstra(adj, hubDimension, Person.findPerson(x.name, spider), dimensions));
                int totalTime = 0;
                ArrayList<Integer> pather = x.path;
                for (int y = 0; y < pather.size() - 1; y++) {
                    int currentDimension = pather.get(y);
                    int nextDimension = pather.get(y + 1);
                    totalTime += getEdgeWeight(currentDimension, nextDimension, dimensions);
                }
                x.totaltime = totalTime;
            }
            writeOutput(anomalies, outputFile, spider, dimensions);
        }
        public static ArrayList<Anomaly> readA(String anomaliesInputFile) {
            ArrayList<Anomaly> anomalies = new ArrayList<>();
            StdIn.setFile(anomaliesInputFile);
            int numAnomalies = StdIn.readInt();
            for (int i = 0; i < numAnomalies; i++) {
                String name = StdIn.readString();
                int allottedTime = StdIn.readInt();
                anomalies.add(new Anomaly(name, allottedTime));
            }
            return anomalies;
        }
        
        public static ArrayList<Integer> dijkstra(Cluster[] adj, int startDimension, int endDimension, ArrayList<Dimension> dimensions) {
            int[] shortestPath = new int[adj.length];
            Arrays.fill(shortestPath, Integer.MAX_VALUE);
            PriorityQueue<Pair> eq = new PriorityQueue<>(Comparator.comparingInt(p -> p.weight));
            int startIndex = Collider.findIndex(adj, new Cluster(startDimension, null));
            eq.offer(new Pair(startIndex, 0));
            shortestPath[startIndex] = 0;
            int[] predecessor = new int[adj.length];
            Arrays.fill(predecessor, -1);
            while (!eq.isEmpty()) {
                Pair pair = eq.poll();
                int u = pair.vertex;        
                if (adj[u].item == endDimension) break;
                Cluster ptr = adj[u];
                while (ptr != null) {
                    int v = ptr.item;
                    int w = getEdgeWeight(adj[u].item, v, dimensions);
                    int indexV = Collider.findIndex(adj, new Cluster(v, null));
                    if (shortestPath[u] != Integer.MAX_VALUE && shortestPath[indexV] > shortestPath[u] + w) {
                        shortestPath[indexV] = shortestPath[u] + w;
                        eq.offer(new Pair(indexV, shortestPath[indexV]));
                        predecessor[indexV] = u; 
                    }
        
                    ptr = ptr.next;
                }
            }
            ArrayList<Integer> pathList = new ArrayList<>();
            int current = Collider.findIndex(adj, new Cluster(endDimension, null));
            while (current != -1 && predecessor[current] != -1) {
                pathList.add(0, adj[current].item); 
                current = predecessor[current];
            }
            pathList.add(0, startDimension);
            ArrayList<Integer> path = new ArrayList<>();
            for (int i = 0; i < pathList.size(); i++) {
                path.add(pathList.get(i));
            }
            return path;
        }
        
        public static int getEdgeWeight(int source, int destination, ArrayList<Dimension> dimensions) {
            Dimension bruh1 = Dimension.finddim(source, dimensions);
            Dimension bruh2 = Dimension.finddim(destination, dimensions);
            if (bruh1 != null && bruh2 != null) {
                return bruh1.weight + bruh2.weight;
            }
            return 0; 
        }
        
        public static void writeOutput(ArrayList<Anomaly> anomalies, String outputFile, ArrayList<Person> spider, ArrayList<Dimension> dimensions) {
           StdOut.setFile(outputFile);
           for (int i = 0; i < anomalies.size(); i++) {
            Anomaly anomaly = anomalies.get(i);
            if (anomaly.allottedTime >= anomaly.totaltime) {
                Dimension home = Dimension.finddim(Person.findPerson(anomaly.name, spider), dimensions);
                StdOut.print(home.canon);
                StdOut.print(" " + anomaly.name + " SUCCESS ");
                for (int j = 0; j < anomaly.path.size(); j++) {
                    Integer x = anomaly.path.get(j);
                    StdOut.print(x);
                    StdOut.print(" ");
                }
            } else {
                Dimension home = Dimension.finddim(Person.findPerson(anomaly.name, spider), dimensions);
                StdOut.print((home.canon - 1));
                StdOut.print(" " + anomaly.name + " FAILED ");
                for (int j = 0; j < anomaly.path.size(); j++) {
                    Integer x = anomaly.path.get(j);
                    StdOut.print(x);
                    StdOut.print(" ");
                }
            }
            StdOut.println();
        }
           
        }
        
}
