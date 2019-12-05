package com.rmaj91.domain;

import com.rmaj91.controller.BoardController;
import com.rmaj91.interfaces.PlayerInterface;
import com.rmaj91.repository.GamesRepositoryImpl;

import java.io.Serializable;
import java.util.Arrays;

public class PlayerCricket implements PlayerInterface, Serializable {

    /* Dependencies */
    private static GamesRepositoryImpl gamesRepository;
    private static BoardController boardController;

    /*Variables*/
    private String name;
    private int points;
    private int[] hittedFields;
    private String[] throwFieldsContent;
    private int currentThrow;

    /*Constructor*/
    public PlayerCricket(String name) {
        this.name = name;
        points = 0;
        hittedFields = new int[7];
        Arrays.fill(hittedFields, 0);
        throwFieldsContent = new String[3];
        Arrays.fill(throwFieldsContent, new String());
        currentThrow = 1;
    }

    /*Copying Constructor*/
    public PlayerCricket(PlayerCricket playerCricket) {
        this.name = playerCricket.name;
        this.points = playerCricket.points;
        hittedFields = new int[7];
        for (int i = 0; i < 7; i++) {
            this.hittedFields[i] = playerCricket.hittedFields[i];
        }
        throwFieldsContent = new String[3];
        Arrays.fill(throwFieldsContent, new String());
        currentThrow = 1;
    }

    public void setHittedFields(int[] hittedFields) {
        this.hittedFields = hittedFields;
    }

    public String getThrowFieldContent(int index) {
        return this.throwFieldsContent[index];
    }

    public void setThrowFieldsByIndex(int index, String string) {
        this.throwFieldsContent[index] = string;
    }

    public void setHittedFieldsbyIndex(int index, int newValue) {
        this.hittedFields[index] = newValue;
    }

    public int getHittedFieldsByIndex(int index) {
        return hittedFields[index];
    }

    @Override
    public void display() {
        boardController.getPlayerPointsLabel().setText(String.valueOf(points));
        for (int i = 0; i < 3; i++)
            boardController.getThrowTextField(i).setText(throwFieldsContent[i]);

    }






    /*Getters & Setters*/

    public static void setBoardController(BoardController boardController) {
        PlayerCricket.boardController = boardController;
    }

    public static GamesRepositoryImpl getGamesRepository() {
        return gamesRepository;
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

    public String[] getThrowFieldsContent() {
        return throwFieldsContent;
    }

    public void setThrowFieldsContent(String[] throwFieldsContent) {
        this.throwFieldsContent = throwFieldsContent;
    }

    public int getCurrentThrow() {
        return currentThrow;
    }

    public void setCurrentThrow(int currentThrow) {
        this.currentThrow = currentThrow;
    }

    public static void setGamesRepository(GamesRepositoryImpl gamesRepository) {
        PlayerCricket.gamesRepository = gamesRepository;
    }

}
