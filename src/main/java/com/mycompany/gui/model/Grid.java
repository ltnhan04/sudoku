package com.mycompany.gui.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;


public class Grid implements Iterable<Cell> {

    private final int SIZE = 9;
    private final Cell[][] cells;
    private final List<Cell> cellList;
    private final List<List<Cell>> subgrids;
    private final Difficulty difficulty;


    public Grid(Difficulty diff) {
        this.cells = new Cell[this.SIZE][this.SIZE];
        this.cellList = new ArrayList<>(this.SIZE * this.SIZE);
        this.subgrids = generateSubgrids();
        this.difficulty = diff;
        initialiseGrid();
    }


    private void initialiseGrid() {
        for (int row = 0; row < this.SIZE; row++) {
            for (int column = 0; column < this.SIZE; column++) {
                Cell cell = new Cell(row, column);
                this.cells[row][column] = cell;
                this.cellList.add(cell);
                this.subgrids.get(cell.getPosition().getSubgrid()).add(cell);
            }
        }
    }


    private List<List<Cell>> generateSubgrids() {
        List<List<Cell>> gridList = new ArrayList<>(this.SIZE);
        for (int i = 0; i < this.SIZE; i++) {
            gridList.add(new ArrayList<>());
        }
        return gridList;
    }


    public List<Cell> getCellList() {
        return this.cellList;
    }


    public List<List<Cell>> getSubgrids() {
        return this.subgrids;
    }


    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void hint(boolean entireGrid) {
        ArrayList<Cell> emptyCells = new ArrayList<>();

        for (Cell cell : cellList) {
            if (cell.isEmpty()) {
                emptyCells.add(cell);
            }
        }

        Collections.shuffle(emptyCells);

        for (Cell cell : emptyCells) {
            if (entireGrid) {
                cell.setUserValue(cell.getSolutionValue());
                cell.setLocked(true);
            } else if (!entireGrid && cell.isEmpty()) {
                cell.setUserValue(cell.getSolutionValue());
                cell.setLocked(true);
                return;
            }
        }
    }


    public boolean isSolved() {
        for (Cell cell : this) {
            if (cell.getUserValue() != cell.getSolutionValue()) {
                return false;
            }
        }
        return true;
    }


    public void provisionCells() {
        for (Cell cell : this) {
            cell.storeProvisionalValue();
        }
    }

    public void fetchCellProvision() {
        for (Cell cell : this) {
            cell.fetchProvisionalValue();
        }
    }


    public boolean isFilled() {
        for (int i = 0; i < this.cellList.size(); i++) {
            if (this.cellList.get(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean meetsConstraints(Cell cell, int value) {
        return checkRow(cell.getPosition().getRow(), value)
            && checkColumn(cell.getPosition().getColumn(), value)
            && checkSubgrid(cell, value);
    }

    private boolean checkRow(int row, int value) {
        for (Cell cell : cells[row]) {
            if (value == cell.getUserValue()) {
                System.out.println("Conflict in row: " + row);
                return false;
            }
        }
        return true;
    }

    private boolean checkColumn(int column, int value) {
        for (Cell[] columnCells : cells) {
            if (value == columnCells[column].getUserValue()) {
                System.out.println("Conflict in column: " + column);
                return false;
            }
        }
        return true;
    }

    private boolean checkSubgrid(Cell currentCell, int value) {
        for (Cell cell : subgrids.get(currentCell.getPosition().getSubgrid())) {
            if (value == cell.getUserValue()) {
                System.out.println("Conflict in subgrid: " + currentCell.getPosition().getSubgrid());
                return false;
            }
        }
        return true;
    }


    public Cell getCell(int row, char column) {
        return this.cells[row - 1][(Character.toUpperCase(column) - 65)];
    }


    public Cell getCell(int xPos, int yPos) {
        return this.cells[xPos - 1][yPos - 1];
    }


    @Override
    public ListIterator<Cell> iterator() {
        return shuffleCells().listIterator();
    }


    public ArrayList<Cell> shuffleCells() {
        ArrayList<Cell> shuffledCells = new ArrayList<>(cellList);
        Collections.shuffle(shuffledCells);
        return shuffledCells;
    }

    public Cell getCellAt(CellPosition position) {
        for (Cell cell : cellList) {
            if (cell.getPosition().equals(position)) {
                return cell;
            }
        }
        throw new IllegalArgumentException("Cell at position " + position + " not found.");
    }
}
