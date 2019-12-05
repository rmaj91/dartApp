package com.rmaj91.domain;

import com.rmaj91.controller.BoardController;
import com.rmaj91.interfaces.PlayerInterface;
import com.rmaj91.repository.GamesRepositoryImpl;

import java.io.Serializable;
import java.util.Arrays;

public class PlayerMasterCricket implements Serializable, PlayerInterface {

    //==================================================================================================
    // Dependencies
    //==================================================================================================
    private static GamesRepositoryImpl gamesRepository;
    private static BoardController boardController;


    //==================================================================================================
    // Properties
    //==================================================================================================
    private String name;
    private int[] points;
    private int[] hittedFields;
    private String[] throwFieldsContent;
    private int currentThrow;
    private int currentThrownFieldIndex;


    //==================================================================================================
    // Constructors
    //==================================================================================================
    public PlayerMasterCricket(String name) {
        this.name = name;
        points = new int[MasterCricket.getNumberOfPlayers()];
        hittedFields = new int[7];
        Arrays.fill(hittedFields, 0);
        throwFieldsContent = new String[3];
        Arrays.fill(throwFieldsContent, new String());
        currentThrow = 1;
        currentThrownFieldIndex = 0;
    }

    public PlayerMasterCricket(PlayerMasterCricket playerMasterCricket) {
        this.name = playerMasterCricket.name;
        points = new int[MasterCricket.getNumberOfPlayers()];
        for (int i = 0; i < MasterCricket.getNumberOfPlayers(); i++) {
            this.points[i] = playerMasterCricket.points[i];
        }
        hittedFields = new int[7];
        for (int i = 0; i < 7; i++) {
            this.hittedFields[i] = playerMasterCricket.hittedFields[i];
        }
        throwFieldsContent = new String[3];
        Arrays.fill(throwFieldsContent, new String());
        currentThrow = 1;
        this.currentThrownFieldIndex = playerMasterCricket.currentThrownFieldIndex;
    }


    //region Assesors @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    //==================================================================================================
    // Assesors
    //==================================================================================================
    public void setHittedFields(int[] hittedFields) {
        this.hittedFields = hittedFields;
    }

    public void setCurrentThrownFieldIndex(int currentThrownFieldIndex) {
        this.currentThrownFieldIndex = currentThrownFieldIndex;
    }

    public void setPoints(int[] points) {
        this.points = points;
    }

    public static void setBoardController(BoardController boardController) {
        PlayerMasterCricket.boardController = boardController;
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

    public int[] getHittedFields() {
        return hittedFields;
    }

    public int getCurrentThrow() {
        return currentThrow;
    }

    public void setCurrentThrow(int currentThrow) {
        this.currentThrow = currentThrow;
    }

    public static void setGamesRepository(GamesRepositoryImpl gamesRepository) {
        PlayerMasterCricket.gamesRepository = gamesRepository;
    }
    //endregion=====


    //==================================================================================================
    // Custom Assesors
    //==================================================================================================

    public String getThrowFieldContent(int index) {
        return this.throwFieldsContent[index];
    }

    public void setHittedFieldsbyIndex(int index, int newValue) {
        this.hittedFields[index] = newValue;
    }

    public int getHittedFieldsByIndex(int index) {
        return hittedFields[index];
    }

    public void setThrowFieldsByIndex(int index, String string) {
        this.throwFieldsContent[index] = string;
    }

    public int getPointsByPlayerIndex(int index) {
        return points[index];
    }

    public void setPointsByPlayerIndex(int index, int points) {
        this.points[index] = points;
    }


    //==================================================================================================
    // Public Methods
    //==================================================================================================
    @Override
    public void display() {
        for (int i = 0; i < 3; i++)
            boardController.getThrowTextField(i).setText(throwFieldsContent[i]);
    }
}
