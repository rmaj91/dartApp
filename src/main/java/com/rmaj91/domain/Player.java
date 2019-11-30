package com.rmaj91.domain;

import com.rmaj91.repository.GamesRepositoryImpl;

public class Player{

    public static GamesRepositoryImpl gamesRepositoryImpl;

    private String name;
    private int points;
    private double average;
    private String[] throwFields;
    private int currentThrow;

    public Player() {
        average=0;

        throwFields = new String[3];
        throwFields[0] = new String();
        throwFields[1] = new String();
        throwFields[2] = new String();

//        throwFields = new String[3];
//        for (String throwField : throwFields) {
//            throwField = new String();
//        }
        currentThrow=1;
    }

    public Player clonePlayer(){
        Player player = new Player();
        player.name = this.name;
        player.points = this.points;
        // calculating average
        player.average = (Game01.getStartingPoints() - player.points) / (gamesRepositoryImpl.getIndexOfRound(gamesRepositoryImpl.getCurrentRound())+1);
        return player;
    }

    // Getters //

    public String[] getThrowFields() {
        return throwFields;
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


    // Setters //


    public void setAverage(double average) {
        this.average = average;
    }

    public void setThrowFieldsByIndex(int index, String string) {
        this.throwFields[index] = string;
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
}