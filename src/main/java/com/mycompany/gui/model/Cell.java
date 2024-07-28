package com.mycompany.gui.model;

import java.util.Objects;
import javax.swing.JTextField;


public class Cell extends JTextField {

    protected int userValue;
    protected int solutionValue;
    protected int provisionalValue;
    private boolean locked;
    private final CellPosition position;
    private static final String COLOUR_RED = "\u001B[31m";
    private static final String COLOUR_RESET = "\u001B[0m";


    public Cell(int row, int column) {
        this.position = new CellPosition(row, column);
    }


    public Cell(CellPosition position, int solutionValue) {
        this.position = position;
        this.solutionValue = solutionValue;
    }

    public Cell(CellPosition position, boolean locked, int userValue) {
        this.position = position;
        this.locked = locked;
        this.userValue = userValue;
    }


    public CellPosition getPosition() {
        return this.position;
    }


    public boolean isLocked() {
        return this.locked;
    }


    public void setLocked(boolean locked) {
        this.locked = locked;
    }


    public boolean isEmpty() {
        return getUserValue() == 0;
    }


    public int getUserValue() {
        return this.userValue;
    }


    public void setUserValue(int userValue) {
        this.userValue = userValue;
    }


    public int getSolutionValue() {
        return this.solutionValue;
    }

   
    public void setSolutionValue() {
        this.solutionValue = userValue;
    }


    public void storeProvisionalValue() {
        this.provisionalValue = this.userValue;
    }

    public void fetchProvisionalValue() {
        this.userValue = this.provisionalValue;
    }


    @Override
    public String toString() {
        if (this.isLocked()) {

            return "[" + COLOUR_RED + getUserValue() + COLOUR_RESET + "]";
        }
        return ("[" + (isEmpty() ? "_" : getUserValue()) + "]");
    }


    public String cellDescription() {
        String description = "Cell at " + getPosition() + " (subgrid " + (getPosition().getSubgrid() + 1) + ")";
        description += (isLocked() ? " cannot be edited. " : (!isEmpty() ? " contains " + getUserValue() + "." : " is clear."));
        return description;
    }


    @Override
    public boolean equals(Object object) {
        return object != null
                && object.getClass() == this.getClass()
                && ((Cell) object).getUserValue() == this.getUserValue()
                && ((Cell) object).getPosition().getRow() == this.getPosition().getRow()
                && ((Cell) object).getPosition().getColumn() == this.getPosition().getColumn();
    }


    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + this.userValue;
        hash = 79 * hash + this.solutionValue;
        hash = 79 * hash + this.provisionalValue;
        hash = 79 * hash + (this.locked ? 1 : 0);
        hash = 79 * hash + Objects.hashCode(this.position);
        return hash;
    }
}
