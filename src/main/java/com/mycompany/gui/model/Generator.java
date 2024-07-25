package com.mycompany.gui.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Generates a new Sudoku puzzle.
 *
 * @author Nhan
 * @version 1.0
 */
public class Generator {

    // Generator Attributes
    private Grid newGrid;
    private int numEmpty;
    private final Solution solution;

    /**
     * Constructs a new solution to the grid.
     */
    public Generator() {
        // Create a solution for the new instance
        this.solution = new Solution();
    }

    /**
     * Generates a difficulty-dependent Sudoku grid.
     *
     * @param diff the user-selected difficulty
     * @return a valid grid with open cells to be filled by the user
     */
    public Grid generateGrid(Difficulty diff) {

        // Get a solution for the grid
        this.setGrid(new Grid(diff));
        this.solution.solveFor(getGrid()).findSolution(getGrid().getCellList(), 1);

        // Set the solution value for each cell
        for (Cell cell : getGrid()) {
            cell.setSolutionValue();
        }

        // Remove some digits from the grid
        setNumEmpty(diff.numEmptyCells());
        emptyCells(getNumEmpty());

        // Save & return a valid grid with open cells 
        this.getGrid().provisionCells();
        return this.getGrid();
    }

    /**
     * @param grid the grid which contains the cells
     * @return a list of all empty cells on the grid
     */
    public static List<Cell> allEmptyCells(Grid grid) {
        List<Cell> emptyList = new ArrayList<>();
        // Add the cell to emptyList if cell is empty
        for (Cell cell : grid) {
            if (cell.isEmpty()) {
                emptyList.add(cell);
            }
        }
        return emptyList;
    }

    /**
     * Removes difficulty-depended number of digits from the grid.
     *
     * @param numRemove the amount of digits (cells) to empty
     */
   public void emptyCells(int numRemove) {
    getGrid().provisionCells();
    List<Cell> cellList = new ArrayList<>(getGrid().getCellList());
    Collections.shuffle(cellList); // Randomize the selection of cells

    int cellsEmptied = 0;

    for (Cell cell : cellList) {
        if (cellsEmptied == numRemove) {
            break;
        }

        if (!cell.isEmpty()) {
            cell.storeProvisionalValue();
            cell.setUserValue(0);

            this.solution.solveFor(getGrid());

            // Ensure the grid still has a unique solution
            if (this.solution.findSolution(allEmptyCells(getGrid()), 3) == 1) {
                cellsEmptied++;
            } else {
                // Revert the change if the solution is not unique
                cell.fetchProvisionalValue();
            }
        }
    }

    lockHints();
}

private void lockHints() {
    for (Cell cell : getGrid()) {
        cell.setLocked(!cell.isEmpty());
    }
}

    /**
     * @return the number of intended empty cells
     */
    public int getNumEmpty() {
        return numEmpty;
    }

    /**
     * @param numEmpty the number of intended empty cells
     */
    private void setNumEmpty(int numEmpty) {
        this.numEmpty = numEmpty;
    }

    /**
     * @return the grid
     */
    public Grid getGrid() {
        return newGrid;
    }

    /**
     * @param newGrid the new grid to set
     */
    private void setGrid(Grid newGrid) {
        this.newGrid = newGrid;
    }
}
