package com.mycompany.gui.model;

import java.util.Random;


public enum Difficulty {


    BEGINNER(15, 3),
    INTERMEDIATE(25,5),
    ADVANCED(33, 8);

    private final int variance = 2;
    private final int maxHints;
    private final int emptyCells;


    Difficulty(int averageBlanks, int maxHints) {
        this.emptyCells = new Random().nextInt(((averageBlanks + variance) - (averageBlanks - variance)) + 1) + (averageBlanks - variance);
        this.maxHints = maxHints;
    }


    public int getMaxHints() {
        return maxHints;
    }


    public int numEmptyCells() {
        return emptyCells;
    }


    @Override
    public String toString() {
        return new StringBuffer(this.name().length())
                .append(Character.toTitleCase(this.name().charAt(0)))
                .append(this.name().toLowerCase().substring(1)).toString();
    }
}
