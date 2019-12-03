package com.rmaj91.domain;

import com.rmaj91.interfaces.PlayerInterface;
import com.rmaj91.repository.GamesRepositoryImpl;

import java.util.Arrays;

public class PlayerCricket implements PlayerInterface {

    /* Dependencies */
    private static GamesRepositoryImpl gamesRepositoryImpl;

    /*Variables*/
    private String name;
    private int points;
    private int[] hittedFields;
    private String[] throwFieldsValues;
    private int currentThrow;

    /*Constructor*/
    public PlayerCricket() {
        points = 0;
        throwFieldsValues = new String[3];
        Arrays.fill(throwFieldsValues,new String());
        hittedFields = new int[7];
        Arrays.fill(hittedFields,0);
        currentThrow = 1;
    }

    /*Getters & Setters*/
    public static GamesRepositoryImpl getGamesRepositoryImpl() {
        return gamesRepositoryImpl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int[] getHittedFields() {
        return hittedFields;
    }

    public void setHittedFields(int[] hittedFields) {
        this.hittedFields = hittedFields;
    }

    public String[] getThrowFieldsValues() {
        return throwFieldsValues;
    }

    public void setThrowFieldsValues(String[] throwFieldsValues) {
        this.throwFieldsValues = throwFieldsValues;
    }

    public int getCurrentThrow() {
        return currentThrow;
    }

    public void setCurrentThrow(int currentThrow) {
        this.currentThrow = currentThrow;
    }

    public static void setGamesRepositoryImpl(GamesRepositoryImpl gamesRepositoryImpl) {
        PlayerCricket.gamesRepositoryImpl = gamesRepositoryImpl;
    }

    /*Public Methods*/
    public PlayerCricket clonePlayer(){
        PlayerCricket player = new PlayerCricket();
        player.name = this.name;
        player.points = this.points;
        for (int i = 0; i < 7; i++) {
            player.hittedFields[i] = this.hittedFields[i];
        }

        return player;
    }
}
