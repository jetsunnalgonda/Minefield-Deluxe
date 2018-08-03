package com.games.aurelius.minefielddeluxe;

public class MinefieldGame {

    private Integer numberOfBombs;
    private Integer sizeOfField;

    Integer numberOfLives;

    Minefield minefield;

    public MinefieldGame(Integer numberOfBombs, Integer sizeOfField, Integer numberOfLives) {
        System.out.println("* MinefieldGame constructor *");

        this.numberOfBombs = numberOfBombs;
        this.sizeOfField = sizeOfField;
        this.numberOfLives = numberOfLives;

        System.out.println("numberOfBombs = " + numberOfBombs);

        minefield = new Minefield(numberOfBombs, sizeOfField, numberOfLives);

    }
}
