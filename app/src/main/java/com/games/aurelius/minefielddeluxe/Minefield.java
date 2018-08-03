package com.games.aurelius.minefielddeluxe;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.games.aurelius.minefielddeluxe.Minefield.GameResult.Failed;
import static java.lang.Math.random;
import static java.lang.Math.round;

public class Minefield {


    public Minefield(Integer numberOfBombs, Integer sizeOfField, Integer numberOfLives) {
        System.out.println("* Minefield constructor *");

        this.numberOfBombs = numberOfBombs;
        this.sizeOfField = sizeOfField;
        this.numberOfLives = numberOfLives;
//        self.mineSquare = [[Int]]()

        remainingCells = sizeOfField * sizeOfField;

        System.out.println("numberOfBombs = " + numberOfBombs);


//        print("sizeOfField = \(sizeOfField)")
        makeTheFieldReady();

//        print(mineSquare.description)
//        openEverything()

    }

    enum GameResult {
        Failed,
        Success,
        Continuing
    }

    GameResult gameResult = GameResult.Continuing;

    private List<ArrayList<Integer>> mineSquare = new ArrayList<ArrayList<Integer>>();
    List<ArrayList<Minecell>> mineCellSquare = new ArrayList<ArrayList<Minecell>>();
    private Integer remainingCells;
    Integer numberOfLives;

    private Integer sizeOfField;

    private Boolean numbered = false;
    private Integer numberOfBombs;


    private void placeRandomBombs() {
        // Pick random points on the field so that we can place the bombs there
        Integer maxLocation = this.sizeOfField * this.sizeOfField;
        //let numberOfLocations = numberOfBombs
        List<Integer> locations = new ArrayList<Integer>();
        for(Integer i = 0; i < maxLocation; i++) {
            locations.add(i);
        }
        System.out.println("placeRandomBombs() locations = " + locations);

        List<Integer> randomLocations = getRandomNumbers(locations, this.numberOfBombs);
        mineSquare = convertToTwoDimensional(randomLocations, this.sizeOfField);

        System.out.println("randomLocations = " + randomLocations);

        System.out.println("this.numberOfBombs = " + this.numberOfBombs);
        System.out.println("this.sizeOfField = " + this.sizeOfField);


    }

    private void placeNumbers() {
        for (Integer rowIndex = 0; rowIndex < sizeOfField; rowIndex++) {
            for (Integer colIndex = 0; colIndex < sizeOfField; colIndex++) {
                if (mineSquare.get(rowIndex).get(colIndex) != -1) {
                    for (Integer i = 0; i < 8; i++) {
                        long shiftedRow = rowIndex - Math.round(Math.sin(Math.PI * i / 4));
                        long shiftedCol = colIndex + Math.round(Math.cos(Math.PI * i / 4));

//                        print("mineSquare[rowIndex][colIndex] = \(mineSquare[rowIndex][colIndex])")
                        if ((shiftedRow >= 0) && (shiftedRow < sizeOfField)) {
                            if (shiftedCol >= 0 && shiftedCol < sizeOfField) {
                                if (mineSquare.get((int) shiftedRow).get((int) shiftedCol) == -1) {
                                    Integer cellValue = mineSquare.get(rowIndex).get(colIndex);
                                    cellValue += -(mineSquare.get((int) shiftedRow).get((int) shiftedCol));
                                    mineSquare.get(rowIndex).set(colIndex, cellValue);
                                }
                            }
                        }

                    }
                    //mineSquare[rowIndex]
                }

            }
        }
    }

    private void makeTheMineField() {
        for (Integer i = 0; i < sizeOfField; i++) {
            List<Minecell> subArray = new ArrayList<Minecell>();

            for (Integer j = 0; j < sizeOfField; j++) {
                subArray.add(new Minecell());
            }
            mineCellSquare.add((ArrayList<Minecell>) subArray);
        }

        for (Integer rowIndex = 0; rowIndex < sizeOfField; rowIndex++) {
            for (Integer colIndex = 0; colIndex < sizeOfField; colIndex++) {
                mineCellSquare.get(rowIndex).get(colIndex).opened = false;
                mineCellSquare.get(rowIndex).get(colIndex).number = mineSquare.get(rowIndex).get(colIndex);
                if (mineSquare.get(rowIndex).get(colIndex) == -1) {
                    mineCellSquare.get(rowIndex).get(colIndex).hasBomb = true;
                }

            }
        }


    }

    private void makeTheFieldReady() {
        placeRandomBombs();
        placeNumbers();
        makeTheMineField();

        System.out.println("mineSquare = " + mineSquare);
//        System.out.println("mineCellSquare = " + mineCellSquare);

    }

//    private mutating func openEverything() {
//        for rowIndex in 0..<sizeOfField {
//            for colIndex in 0..<sizeOfField {
//                mineCellSquare[rowIndex][colIndex].opened = true
//            }
//        }
//    }


    public void openCells(IndexPath clickedCell) {
        Integer row = clickedCell.x;
        Integer col = clickedCell.y;

        System.out.println("Minefield openCells clickedCell.x = " + clickedCell.x + " clickedCell.y = " + clickedCell.y);

//        mineCellSquare.get(row).get(col).opened = true;
//        System.out.println("mineCellSquare.get(row).get(col).opened = " + mineCellSquare.get(row).get(col).opened);

        if (mineCellSquare.get(row).get(col).flagged) { return; }

        // If we clicked a cell containing a bomb,
        // make the game over and open all of the cells containing bomb
        if (mineCellSquare.get(row).get(col).hasBomb) {
            System.out.println("bomb");
            numberOfLives -= 1;
            if (numberOfLives == 0) {
                endTheGame();
            } else {
                mineCellSquare.get(row).get(col).opened = true;
            }

            // or if we click a cell that has no number open the adjacent empty cells,
            // and adjacent numbered cells
            // (Flood-fill algorithm)
        } else if (mineCellSquare.get(row).get(col).number == 0 && !mineCellSquare.get(row).get(col).flagged) {
            System.out.println("flood fill time");
            mineCellSquare.get(row).get(col).opened = true;
            remainingCells -= 1;
            floodFill(col, row);
            // or if we just clicked on a numbered cell only open that cell
        } else {
            System.out.println("number cell");
            mineCellSquare.get(row).get(col).opened = true;
            remainingCells -= 1;

        }


        if (remainingCells == numberOfBombs) { gameResult = GameResult.Success; }

    }


//    private func remainingCells() -> Int {
//        var remaining = sizeOfField * sizeOfField
//        for i in 0..<sizeOfField {
//            for j in 0..<sizeOfField {
//                remaining -= mineCellSquare[i][j].opened == true && !mineCellSquare[i][j].hasBomb ? 1 : 0
//            }
//        }
//        return remaining
//    }

//    private mutating func isCellEmpty(_ x: Int, _ y: Int) -> Bool {
//        if x < 0 || y < 0 { return false }
//        if x >= sizeOfField || y >= sizeOfField { return false }
//
//        if mineCellSquare[y][x].number == 0 {
//            mineCellSquare[y][x].opened = true
//            return true
//        } else {
//            return false
//        }
//
//    }

//    private  func isCellEmpty(_ x: Int, _ y: Int) -> Bool {
//        if x < 0 || y < 0 { return false }
//        if x >= sizeOfField || y >= sizeOfField { return false }
//        return mineCellSquare[y][x].number == 0
//    }

//    private func isCellNumbered(_ x: Int, _ y: Int) -> Bool {
//        if x < 0 || y < 0 { return false }
//        if x >= sizeOfField || y >= sizeOfField { return false }
//        return mineCellSquare[y][x].number > 0
//    }
//
//    private func fillAdjacent(_ x: Int, _ y: Int) {
//
//    }
//
    private void floodFill(Integer x, Integer y) {
        for (Integer rowOffset = -1; rowOffset <= 1; rowOffset++) {
            for (Integer colOffset = -1; colOffset <= 1; colOffset++) {
                Integer row = y + rowOffset;
                Integer col = x + colOffset;
                if (row > -1 && row < sizeOfField && col > -1 && col < sizeOfField ) {
                    Minecell neighbor = mineCellSquare.get(row).get(col);
                    if (!neighbor.hasBomb && !neighbor.opened && !neighbor.flagged) {
                        IndexPath indexPath = new IndexPath();
                        indexPath.x = row;
                        indexPath.y = col;
                        openCells(indexPath);
                    }
                }
            }
        }
    }
//
    private void endTheGame() {
        gameResult = GameResult.Failed;

        for (int i = 0; i < sizeOfField; i++) {
            for (int j = 0; j < sizeOfField; j++) {
                if (mineCellSquare.get(i).get(j).hasBomb) {
                    mineCellSquare.get(i).get(j).opened = true;
                }
            }
        }

    }

    private List<Integer> getRandomNumbers(List<Integer> theArray, Integer numberOfNumbers) {
        List<Integer> duplicateArray = new ArrayList<Integer>();
        List<Integer> randomKeys = new ArrayList<Integer>();
        Random rand = new Random();

        for (Integer i = 0; i < theArray.size(); i++) {
            duplicateArray.add(theArray.get(i));
        }

        for(Integer i = 1; i <= numberOfNumbers; i++) {
            Integer arrayKey = rand.nextInt(duplicateArray.size());

            randomKeys.add(duplicateArray.get(arrayKey));

//            System.out.println("arrayKey = " + arrayKey);
//            System.out.println("randomKeys = " + randomKeys);
//            System.out.println("duplicateArray before = " + duplicateArray);

            // In Java, the remove method takes element as the argument, not key associated with the element
//            duplicateArray.remove(arrayKey);
            duplicateArray.remove(duplicateArray.get(arrayKey));

//            Integer randomKey = duplicateArray.remove(arrayKey);
            System.out.println("duplicateArray after = " + duplicateArray);

        }
        return randomKeys;
    }

    private List<ArrayList<Integer>> convertToTwoDimensional(List<Integer> locationsArray, Integer convertedWidth) {

        List<ArrayList<Integer>> twoDimensionalArray = new ArrayList<ArrayList<Integer>>();

        for (Integer i = 0; i < convertedWidth; i++) {
            List<Integer> subArray = new ArrayList<Integer>();

            for (Integer j = 0; j < convertedWidth; j++) {
                subArray.add(0);
            }
            twoDimensionalArray.add((ArrayList<Integer>) subArray);
        }
        for (Integer idx = 0; idx < locationsArray.size(); idx++) {
            Integer locationValue = locationsArray.get(idx);
            Integer rowIndex = (locationValue - locationValue % convertedWidth) / convertedWidth;
            Integer colIndex = locationValue % convertedWidth;

            twoDimensionalArray.get(rowIndex).set(colIndex, -1);

        }

        return twoDimensionalArray;
    }

}
