package com.mycompany.gui.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Generator {

    private Grid newGrid;
    private int numEmpty;
    private final Solution solution;


    public Generator() {
        this.solution = new Solution();
    }


    public Grid generateGrid(Difficulty diff) {

        this.setGrid(new Grid(diff));
        this.solution.solveFor(getGrid()).findSolution(getGrid().getCellList(), 1);

        for (Cell cell : getGrid()) {
            cell.setSolutionValue();
        }

        setNumEmpty(diff.numEmptyCells());
        emptyCells(getNumEmpty());

        this.getGrid().provisionCells();
        return this.getGrid();
    }


    public static List<Cell> allEmptyCells(Grid grid) {
        List<Cell> emptyList = new ArrayList<>();
        for (Cell cell : grid) {
            if (cell.isEmpty()) {
                emptyList.add(cell);
            }
        }
        return emptyList;
    }


   public void emptyCells(int numRemove) {
    getGrid().provisionCells();
    List<Cell> cellList = new ArrayList<>(getGrid().getCellList());
    Collections.shuffle(cellList); 

    int cellsEmptied = 0;

    for (Cell cell : cellList) {
        if (cellsEmptied == numRemove) {
            break;
        }

        if (!cell.isEmpty()) {
            cell.storeProvisionalValue();
            cell.setUserValue(0);

            this.solution.solveFor(getGrid());

            if (this.solution.findSolution(allEmptyCells(getGrid()), 3) == 1) {
                cellsEmptied++;
            } else {
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


    public int getNumEmpty() {
        return numEmpty;
    }


    private void setNumEmpty(int numEmpty) {
        this.numEmpty = numEmpty;
    }


    public Grid getGrid() {
        return newGrid;
    }


    private void setGrid(Grid newGrid) {
        this.newGrid = newGrid;
    }
}
