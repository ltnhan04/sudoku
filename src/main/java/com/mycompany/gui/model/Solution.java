package com.mycompany.gui.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class Solution {

    private Grid grid;
    private int result;
    private int loop;


    public Solution solveFor(Grid grid) {
        this.grid = grid;
        this.result = 0;
        this.setLoop(0);
        return this;
    }


   public int findSolution(List<Cell> emptyCells, int numEmpty) {
    if (getLoop() < emptyCells.size()) {
        for (int digit : shuffleValues()) {
            if (grid.meetsConstraints(emptyCells.get(getLoop()), digit)) {
                emptyCells.get(getLoop()).setUserValue(digit);
                System.out.println("Setting cell " + emptyCells.get(getLoop()).getPosition() + " to " + digit);
                setLoop(getLoop() + 1);
                if (findSolution(emptyCells, numEmpty) >= numEmpty) {
                    return result;
                }
            }
        }
        emptyCells.get(getLoop()).setUserValue(0);
        System.out.println("Resetting cell " + emptyCells.get(getLoop()).getPosition());
        setLoop(getLoop() - 1);
        return result;
    } else {
        setLoop(getLoop() - 1);
        return ++result;
    }
}


    public List<Integer> shuffleValues() {
        List<Integer> possibleValues = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        Collections.shuffle(possibleValues);
        return possibleValues;
    }

    private int getLoop() {
        return loop;
    }

    private void setLoop(int loop) {
        this.loop = loop;
    }
}
