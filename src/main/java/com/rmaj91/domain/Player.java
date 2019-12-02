package com.rmaj91.domain;

import com.rmaj91.repository.GamesRepositoryImpl;
import java.io.Serializable;

/**
 * The Player01 class provides player model of '01 dart game type
 */

public class Player implements com.rmaj91.interfaces.Player,Serializable {

    /* Dependencies */
    private static GamesRepositoryImpl gamesRepositoryImpl;

    /*Variables*/
    private String name;
    private int points;
    private double average;
    private String[] throwFieldsValues;
    private int currentThrow;

    /*Constructor*/
    public Player() {
        average = 0;
        throwFieldsValues = new String[3];
        throwFieldsValues[0] = new String();
        throwFieldsValues[1] = new String();
        throwFieldsValues[2] = new String();
        currentThrow = 1;
    }

    /*Getters & Setters*/
    public String[] getThrowFieldsValues() {
        return throwFieldsValues;
    }

    public int getPoints() {
        return points;
    }

    public double getAverage() {
        return average;
    }

    public String getName() {
        return name;
    }

    public int getCurrentThrow() {
        return currentThrow;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public void setThrowFieldsByIndex(int index, String string) {
        this.throwFieldsValues[index] = string;
    }

    public void setCurrentThrow(int currentThrow) {
        this.currentThrow = currentThrow;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public static void setGamesRepositoryImpl(GamesRepositoryImpl gamesRepositoryImpl) {
        Player.gamesRepositoryImpl = gamesRepositoryImpl;
    }

    /*Public Methods*/
    public Player clonePlayer(){
        Player player = new Player();
        player.name = this.name;
        player.points = this.points;
        // Calculating average
        int currentRound = gamesRepositoryImpl.getIndexOfRound(gamesRepositoryImpl.getCurrentRound())+1;
        player.average = (double)(Game01.getStartingPoints() - player.points) / currentRound;
        return player;
    }
}