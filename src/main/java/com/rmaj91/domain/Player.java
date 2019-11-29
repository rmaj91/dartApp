package com.rmaj91.domain;

import java.util.Arrays;

public class Player{
    private String name;
    private int points;
    private int average;
    private String[] throwFields;
    private int currentThrow;

    public Player() {
        average=0;
        throwFields = new String[3];
        Arrays.fill(throwFields,"");
        currentThrow=1;
    }

    public Player clonePlayer(){
        Player player = new Player();
        player.name = this.name;
        player.points = this.points;
        //player.average = (Game01.startingPoints - this.points)/ (gamesRepositoryImpl.getIndexOfRound(Game01.this)+1);
        return player;
    }

    // Getters //

    public int getPoints() {
        return points;
    }

    public int getAverage() {
        return average;
    }

    public String getName() {
        return name;
    }

    public int getCurrentThrow() {
        return currentThrow;
    }

    public String[] getThrowFields() {
        return throwFields;
    }
    // Setters //

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