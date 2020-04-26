package it.polimi.ingsw.client;

/**
 * @author Alice Piemonti
 * This class is a simplified representation of the gameboard in model.
 * It allows to have the status of the gameboard on client side.
 */
public class ClientBoard {
    private Cell[][] grid;

    public ClientBoard(){
        grid = new Cell[5][5];
    }

    /**
     * @return two-dimensional array which represents the status of the game board.
     */
    public Cell[][] getGrid(){
        return grid;
    }

    /**
     * it's a representation of Worker's setWorker method.
     * @param row of the grid.
     * @param col of the grid.
     * @param color of the player/worker.
     */
    public void setColor(int row, int col, String color){
        grid[row][col].setColor(color);
    }

    /**
     * return the color of the worker into the specified cell of the grid
     * @param row of the grid.
     * @param col of the grid.
     * @return the color of the worker
     */
    public String getColor(int row, int col){
        return grid[row][col].getColor();
    }

    /**
     * it's a representation of Worker's move method
     * @param oldRow the row of previous worker's position.
     * @param oldCol the column of previous worker's position.
     * @param newRow the row of the actual worker's position.
     * @param newCol the column of the actual worker's position.
     */
    public void move(int oldRow, int oldCol, int newRow, int newCol){
        String color = grid[oldRow][oldCol].getColor();
        grid[oldRow][oldCol].setColor(null);
        grid[newRow][newCol].setColor(color);
    }

    /**
     * it's a representation of Apollo's move method
     * @param oldRow1 the row of previous Apollo's position (which is other worker's actual position).
     * @param oldCol1 the column of previous Apollo's position (which is other worker's actual position).
     * @param oldRow2 the row of previous other worker's position (which is Apollo's actual position).
     * @param oldCol2 the column of previous other worker's position (which is Apollo's actual position).
     */
    public void apolloDoubleMove(int oldRow1, int oldCol1, int oldRow2, int oldCol2){
        String color1 = grid[oldRow1][oldCol1].getColor();
        String color2 = grid[oldRow2][oldCol2].getColor();
        grid[oldRow1][oldCol1].setColor(color2);
        grid[oldRow2][oldCol2].setColor(color1);
    }

    /**
     * it's a representation of Minotaur's move method
     * @param oldRow1 the row of Minotaur's previous position.
     * @param oldCol1 the column of Minotaur's previous position.
     * @param oldRow2 the row of the other worker's previous position (which is Minotaur's actual position).
     * @param oldCol2 the column of the other worker's previous position (which is Minotaur's actual position).
     * @param newRow2 the row of the other worker's actual position.
     * @param newCol2 the column of the other worker's actual position.
     */
    public void minotaurDoubleMove(int oldRow1, int oldCol1, int oldRow2, int oldCol2, int newRow2, int newCol2){
        String color1 = grid[oldRow1][oldCol1].getColor();
        String color2 = grid[oldRow2][oldCol2].getColor();
        grid[newRow2][newCol2].setColor(color2);
        grid[oldRow2][oldCol2].setColor(color1);
        grid[oldRow1][oldCol1].setColor(null);
    }

    /**
     * it's a representation of worker's build method
     * @param row the row of build position.
     * @param col the column of build position.
     * @param dome true if worker wants to build a dome instead of a block. (Only for Atlas workers)
     */
    public void build(int row, int col, boolean dome){
        if(dome || grid[row][col].getLevel() == 3){
            grid[row][col].setDome(true);
        }
        else grid[row][col].addLevel();
    }

}