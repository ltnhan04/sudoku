package com.mycompany.gui.model;


public class CellPosition {

    private final int row;
    private final int column;
    private final int subgrid;


    public CellPosition(int row, int column) {
        this.row = row;
        this.column = column;

        int evaluate = this.row < 3 ? 0 : this.row < 6 ? 2 : 4;
        this.subgrid = (this.row / 3) + (this.column / 3) + evaluate;
    }


    public int getRow() {
        return this.row;
    }


    public int getColumn() {
        return this.column;
    }


    public int getSubgrid() {
        return this.subgrid;
    }


    @Override
    public String toString() {
        if (getColumn() + 65 >= 'A' && getColumn() + 65 <= 'Z') {
            return String.valueOf(getRow() + 1) + (char) (getColumn() + 65);
        }
        return String.valueOf(getRow() + 1) + "," + String.valueOf(getColumn() + 1);
    }
}
