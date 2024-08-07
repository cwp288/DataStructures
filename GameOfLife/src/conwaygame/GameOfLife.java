package conwaygame;
import java.util.ArrayList;
/**
 * Conway's Game of Life Class holds various methods that will
 * progress the state of the game's board through it's many iterations/generations.
 *
 * Rules 
 * Alive cells with 0-1 neighbors die of loneliness.
 * Alive cells with >=4 neighbors die of overpopulation.
 * Alive cells with 2-3 neighbors survive.
 * Dead cells with exactly 3 neighbors become alive by reproduction.

 * @author Seth Kelley 
 * @author Maxwell Goldberg
 */
public class GameOfLife {

    // Instance variables
    private static final boolean ALIVE = true;
    private static final boolean  DEAD = false;

    private boolean[][] grid;    // The board has the current generation of cells
    private int totalAliveCells; // Total number of alive cells in the grid (board)

    /**
    * Default Constructor which creates a small 5x5 grid with five alive cells.
    * This variation does not exceed bounds and dies off after four iterations.
    */
    public GameOfLife() {
        grid = new boolean[5][5];
        totalAliveCells = 5;
        grid[1][1] = ALIVE;
        grid[1][3] = ALIVE;
        grid[2][2] = ALIVE;
        grid[3][2] = ALIVE;
        grid[3][3] = ALIVE;
    }

    /**
    * Constructor used that will take in values to create a grid with a given number
    * of alive cells
    * @param file is the input file with the initial game pattern formatted as follows:
    * An integer representing the number of grid rows, say r
    * An integer representing the number of grid columns, say c
    * Number of r lines, each containing c true or false values (true denotes an ALIVE cell)
    */
    public GameOfLife (String file) {
        StdIn.setFile(file);
        int r = StdIn.readInt();
        int c = StdIn.readInt();
        grid = new boolean[r][c];
        for(int i = 0; i < r; i++){
            for(int y = 0; y < c; y++){
                boolean cullen = StdIn.readBoolean();
                grid[i][y] = cullen;
           }
        }
    }

    /**
     * Returns grid
     * @return boolean[][] for current grid
     */
    public boolean[][] getGrid () {
        return grid;
    }
    
    /**
     * Returns totalAliveCells
     * @return int for total number of alive cells in grid
     */
    public int getTotalAliveCells () {
        return totalAliveCells;
    }

    /**
     * Returns the status of the cell at (row,col): ALIVE or DEAD
     * @param row row position of the cell
     * @param col column position of the cell
     * @return true or false value "ALIVE" or "DEAD" (state of the cell)
     */
    public boolean getCellState (int row, int col) {
        if(grid[row][col] == ALIVE){
            return true;
        } 
            return false;
        }

    /**
     * Returns true if there are any alive cells in the grid
     * @return true if there is at least one cell alive, otherwise returns false
     */
    public boolean isAlive () {
        boolean cullen = false;
        for(int i = 0; i < grid.length; i++){
            for(int y = 0; y < grid[i].length; y++){
                if(getCellState(i, y) == ALIVE){
                    cullen = true;
                }
            }
        }
        return cullen; // update this line, provided so that code compiles
    }

    /**
     * Determines the number of alive cells around a given cell.
     * Each cell has 8 neighbor cells which are the cells that are 
     * horizontally, vertically, or diagonally adjacent.
     * 
     * @param col column position of the cell
     * @param row row position of the cell
     * @return neighboringCells, the number of alive cells (at most 8).
     */
    public int numOfAliveNeighbors (int row, int col) {
        int cullen = 0;
        for(int i = -1; i <= 1; i++){
            for(int y = -1; y <= 1; y++){
                int ibruh = (row+r+grid.length)%grid.length;
                int ybruh = (col+c+grid[0].length)%grid[0].length;
                if((i != 0 || y != 0) && grid[ibruh][ybruh]){
                    cullen++;
                }
            }
        }
        return cullen;
    }

    /**
     * Creates a new grid with the next generation of the current grid using 
     * the rules for Conway's Game of Life.
     * 
     * @return boolean[][] of new grid (this is a new 2D array)
     */
    public boolean[][] computeNewGrid () {
         boolean[][] cullen = new boolean[grid.length][grid[0].length];//need to initalize new grid 
         for(int i = 0; i < cullen.length; i++){
            for(int y = 0; y < cullen[0].length; y++){
                if(grid[i][y] && numOfAliveNeighbors(i, y) <= 1){ //Rule one
                    cullen[i][y] = false;
                }
                else if(!grid[i][y] && numOfAliveNeighbors(i, y) == 3){ //Rule two
                    cullen[i][y] = true;
                }
                else if(grid[i][y] && numOfAliveNeighbors(i, y) == 2 || numOfAliveNeighbors(i, y) ==3){ // Rule three
                    cullen[i][y] = true;
                }
                else if((numOfAliveNeighbors(i, y) >= 4) && grid[i][y]){ //Rule four
                    cullen[i][y] = false;
                }
            }
         }
        return cullen;// update this line, provided so that code compiles
    }

    /**
     * Updates the current grid (the grid instance variable) with the grid denoting
     * the next generation of cells computed by computeNewGrid().
     * 
     * Updates totalAliveCells instance variable
     */
    public void nextGeneration () {
        grid = computeNewGrid();
        for(int i = 0; i < grid.length; i++){
            for(int y = 0; y < grid[0].length; y++){
                if(grid[i][y]){
                    totalAliveCells++;
                }
            }
        }
    }

    /**
     * Updates the current grid with the grid computed after multiple (n) generations. 
     * @param n number of iterations that the grid will go through to compute a new grid
     */
    public void nextGeneration (int n) {
        for(int i = 0; i < n; i++){
            nextGeneration();
        }
    }

    /**
     * Determines the number of separate cell communities in the grid
     * @return the number of communities in the grid, communities can be formed from edges
     */
    public int numOfCommunities() {
        WeightedQuickUnionUF cullen = new WeightedQuickUnionUF(grid.length, grid[0].length);
        for(int i = 0; i < grid.length; i++){
            for(int y = 0; y < grid[i].length; y++){
                if(grid[i][y]){
                    for(int z = -1; z <= 1; z++){
                        for(int x = -1; x <= 1; x++){
                            int bruh = (i+z+grid.length)%grid.length;
                            int dude = (y+x+grid[0].length)%grid[0].length;
                            if((z != 0 || x != 0) && (grid[bruh][dude])){
                                cullen.union(i,y,bruh,dude);
                            }
                        }

                    }
                }
            }
        }
        int water = 0;
        for(int g = 0; g < grid.length; g++){
            for(int h = 0; h < grid[g].length; h++){
                int omg = g*grid.length+h;
                if(grid[g][h] && cullen.find(g,h) == omg){
                    water++;
                }
            }
        }
        return water; // update this line, provided so that code compiles
    }
}
