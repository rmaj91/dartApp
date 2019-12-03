package com.rmaj91.domain;

import com.rmaj91.controller.BoardController;
import com.rmaj91.controller.CricketController;
import com.rmaj91.controller.MainController;
import com.rmaj91.interfaces.Playable;
import com.rmaj91.interfaces.PlayerInterface;
import com.rmaj91.repository.GamesRepositoryImpl;
import com.rmaj91.utility.SoundPlayer;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class Cricket implements Playable {

    /*Static Dependencies*/
    private static SoundPlayer soundPlayer;
    private static BoardController boardController;
    private static CricketController cricketController;
    private static GamesRepositoryImpl gamesRepositoryImpl;
    private static MainController mainController;

    /*Static Variables*/
    private static int[] fieldsToHit;
    private static int playersQuantity;
    private static int roundsMaxNumber;

    /*Variables*/
    PlayerCricket[] players;
    private int currentPlayer;

    /*Constructor*/
    public Cricket() {
        currentPlayer=1;
    }

    /*Setters & Getters*/
    public static SoundPlayer getSoundPlayer() {
        return soundPlayer;
    }

    public static void setSoundPlayer(SoundPlayer soundPlayer) {
        Cricket.soundPlayer = soundPlayer;
    }

    public static BoardController getBoardController() {
        return boardController;
    }

    public static void setBoardController(BoardController boardController) {
        Cricket.boardController = boardController;
    }

    public static CricketController getCricketController() {
        return cricketController;
    }

    public static void setCricketController(CricketController cricketController) {
        Cricket.cricketController = cricketController;
    }

    public static GamesRepositoryImpl getGamesRepositoryImpl() {
        return gamesRepositoryImpl;
    }

    public static void setGamesRepositoryImpl(GamesRepositoryImpl gamesRepositoryImpl) {
        Cricket.gamesRepositoryImpl = gamesRepositoryImpl;
    }

    public static MainController getMainController() {
        return mainController;
    }

    public static void setMainController(MainController mainController) {
        Cricket.mainController = mainController;
    }

    public static int[] getFieldsToHit() {
        return fieldsToHit;
    }

    public static void setFieldsToHit(int[] fieldsToHit) {
        Cricket.fieldsToHit = fieldsToHit;
    }

    public static int getPlayersQuantity() {
        return playersQuantity;
    }

    public static void setPlayersQuantity(int playersQuantity) {
        Cricket.playersQuantity = playersQuantity;
    }

    public static int getRoundsMaxNumber() {
        return roundsMaxNumber;
    }

    public static void setRoundsMaxNumber(int roundsMaxNumber) {
        Cricket.roundsMaxNumber = roundsMaxNumber;
    }

    public void setPlayers(PlayerCricket[] players) {
        this.players = players;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }



    /*METHODS FROM PLAYABLE*/

    @Override
    public void next() {

    }

    @Override
    public void back() {

    }

    @Override
    public void throwDart(MouseEvent event) {

    }

    @Override
    public void displayRound() {
        int currentRound = gamesRepositoryImpl.getIndexOfRound(this)+1;
        // Displaying current round
        boardController.getRoundsLabel().setText("Round: "+ currentRound +"/"+ Cricket.roundsMaxNumber);
        displayPlayersInfo();
        displayFieldsHighlight();

    }


    @Override
    public void setCurrentThrow(int throwNumber) {

    }

    @Override
    public Playable cloneRound() {
        return null;
    }

    @Override
    public void saveThrowFields() {

    }

    @Override
    public void calculatePoints() {

    }

    @Override
    public PlayerInterface[] getPlayers() {
        return players;
    }

    @Override
    public int getCurrentPlayer() {
        return 0;
    }


    @Override
    public void initAndDisplay() {

    }


    /*Private Methods*/

    private void displayPlayersInfo() {
        // Displaying throwTextFields
        for (int i = 0; i < 3; i++) {
            boardController.getThrowTextFieldArray()[i].setText(players[currentPlayer-1].getThrowFieldsValues()[i]);
        }

        ObservableList<Node> playerVBoxes = boardController.getCricketsScoreTable().getChildren();
        // Displaying fields hits
        for(int p=1;p <= playersQuantity;p++){
            for(int i=0;i<7;i++){
                Node node1 = playerVBoxes.get(p);
                Node node2 = ((VBox) node1).getChildren().get(i);
                if(players[currentPlayer-1].getHittedFields()[i] == 1)
                    ((Label)node2).setText(new String("I"));
                else if(players[currentPlayer-1].getHittedFields()[i] == 2)
                    ((Label)node2).setText(new String("II"));
                if(players[currentPlayer-1].getHittedFields()[i] > 3)
                    ((Label)node2).setText(new String("III"));
            }
        }


        // Displaying all players points
        for(int i = 1; i< Cricket.playersQuantity; i++) {
            Node node1 = playerVBoxes.get(i);
            Node node2 = ((VBox) node1).getChildren().get(8);
            ((Label)node2).setText(String.valueOf(players[i].getPoints()));
        }
    }

    private void displayFieldsHighlight() {
        for(int i = 1; i <= Cricket.playersQuantity; i++){
            Node node = boardController.getCricketsScoreTable().getChildren().get(i);
            ((VBox)node).getChildren().get(7).setStyle(null);
        }

        Node node = boardController.getCricketsScoreTable().getChildren().get(currentPlayer);
        ((VBox)node).getChildren().get(7).setStyle("-fx-background-color: #0388fc;");
    }


}
