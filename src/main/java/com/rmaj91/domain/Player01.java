package com.rmaj91.domain;

import com.rmaj91.controller.BoardController;
import com.rmaj91.interfaces.PlayerInterface;
import com.rmaj91.repository.GamesRepositoryImpl;

import java.io.Serializable;
import java.util.Arrays;

/**
 * The Player01 class provides player model of '01 dart game type
 */
public class Player01 implements PlayerInterface, Serializable {

    //==================================================================================================
    // Dependencies
    //==================================================================================================
    private static com.rmaj91.interfaces.GamesRepository gamesRepository;
    private static BoardController boardController;


    //==================================================================================================
    // Properties
    //==================================================================================================
    private String name;
    private int points;
    private double average;
    private String[] throwFieldsContent;
    private int currentThrow;


    //==================================================================================================
    // Constructors
    //==================================================================================================
    public Player01(String name, int points) {
        this.name = name;
        this.points = points;
        average = 0;
        throwFieldsContent = new String[3];
        Arrays.fill(throwFieldsContent, new String());
        currentThrow = 1;
    }

    public Player01(Player01 player01) {
        this.name = player01.name;
        this.points = player01.points;
        calculateAverage();
        this.throwFieldsContent = new String[3];
        Arrays.fill(this.throwFieldsContent, new String());
        this.currentThrow = 1;
    }


    //==================================================================================================
    // Public Methods
    //==================================================================================================
    public String getThrowFieldsContentByIndex(int i) {
        return throwFieldsContent[i];
    }

    public void setThrowFieldsByIndex(int index, String throwFieldValue) {
        this.throwFieldsContent[index] = throwFieldValue;
    }

    @Override
    public void display() {
        boardController.getAverageLabel().setText("Average: " + String.format("%.1f", average));
        boardController.getPlayerNameLabel().setText(name);
        boardController.getPlayerPointsLabel().setText(String.valueOf(points));
        for (int i = 0; i < 3; i++)
            boardController.getThrowTextField(i).setText(throwFieldsContent[i]);

    }


    //==================================================================================================
    // Private Methods
    //==================================================================================================
    private void calculateAverage() {
        int startingPoints = ((Game01) gamesRepository.getZeroRound()).getCurrentPlayer().points;
        int round = gamesRepository.getNumberOfRound(gamesRepository.getCurrentRound());
        if (round == 0)
            round++;
        this.average = (startingPoints - this.points) / round;
    }





    /*Getters & Setter*/

    public int getPoints() {
        return points;
    }

    public static void setGamesRepository(GamesRepositoryImpl gamesRepository) {
        Player01.gamesRepository = gamesRepository;
    }

    public static void setBoardController(BoardController boardController) {
        Player01.boardController = boardController;
    }

    public String getName() {
        return name;
    }

    public void setCurrentThrow(int currentThrow) {
        this.currentThrow = currentThrow;
    }


    public void setPoints(int points) {
        this.points = points;
    }

    public int getCurrentThrow() {
        return currentThrow;
    }
}