package spiderman;

import java.util.ArrayList;
import java.util.List;

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
 * SpotInputFile name is passed through the command line as args[2]
 * Read from the SpotInputFile with the format:
 * Two integers (line seperated)
 *      i.    Line one: The starting dimension of Spot (int)
 *      ii.   Line two: The dimension Spot wants to go to (int)
 * 
 * Step 4:
 * TrackSpotOutputFile name is passed in through the command line as args[3]
 * Output to TrackSpotOutputFile with the format:
 * 1. One line, listing the dimenstional number of each dimension Spot has visited (space separated)
 * 
 * @author Seth Kelley
 */

public class TrackSpot {
    public static void main(String[] args) {

        if ( args.length < 4 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.TrackSpot <dimension INput file> <spiderverse INput file> <spot INput file> <trackspot OUTput file>");
                return;
        }
        String input = args[2];
        String output = args[3];
        Cluster[] cullen = Collider.getAdjacencyNodes(args[0]);
        TrackSpot find = new TrackSpot(cullen.length);
        find.fp(input,cullen,output);
    }
    public ArrayList<Integer> p;
    public ArrayList<Integer> v;

    public TrackSpot(int vert) {
        this.p = new ArrayList<>(vert);
        this.v = new ArrayList<>(vert);
    }
    public void fp(String input, Cluster[] cullen, String output) {
        StdIn.setFile(input);
        int id = StdIn.readInt();
        StdIn.readLine();
        int finalD = StdIn.readInt();
        this.depthFirstSearch(new Cluster(id, null), new Cluster(finalD, null), cullen);
        this.printOutput(finalD, output);
    }
    public  boolean depthFirstSearch(Cluster current, Cluster target, Cluster[] cullen) {
        if(current.item == target.item)   {
            return true;
        }
        v.add(current.item);
        p.add(current.item);
        int index = Collider.findIndex(cullen, new Cluster(current.item, target));
        Cluster ptr = cullen[index];
        while(ptr != null) {
            if(!v.contains(ptr.item)) {
                if(depthFirstSearch(ptr, target, cullen)) {
                    return true;
                }
            }
            ptr = ptr.next;
        }
        return false;
    }
    public void printOutput(int last, String output) {
        StdOut.setFile(output);
        v.add(last);
        for (int cluster : v) {
            StdOut.print(cluster);
            StdOut.print(" ");
            
        }
    }
}
