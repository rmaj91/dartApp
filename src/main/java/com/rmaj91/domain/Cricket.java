package com.rmaj91.domain;

import com.rmaj91.controller.BoardController;
import com.rmaj91.controller.CricketController;
import com.rmaj91.controller.MainController;
import com.rmaj91.interfaces.Playable;
import com.rmaj91.interfaces.PlayerInterface;
import com.rmaj91.repository.GamesRepositoryImpl;
import com.rmaj91.utility.Filters;
import com.rmaj91.utility.SoundPlayer;
import com.rmaj91.utility.Utilities;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Arrays;

import static com.rmaj91.utility.Utilities.getAngleIndex;
import static com.rmaj91.utility.Utilities.getRadiusIndex;

public class Cricket implements Playable {

    /*Static Dependencies*/
    private static SoundPlayer soundPlayer;
    private static BoardController boardController;
    private static CricketController cricketController;
    private static GamesRepositoryImpl gamesRepositoryImpl;
    private static MainController mainController;

    /*Static Variables*/
    private static int playersQuantity;
    private static int roundsMaxNumber;
    private static int currentFieldToThrowIndex;
    private static ArrayList<Integer> fieldsToHit;

    /*Variables*/
    PlayerCricket[] players;
    private int currentPlayer;

    /*Constructor*/
    public Cricket() {
        currentPlayer = 1;
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

    public static int getCurrentFieldToThrowIndex() {
        return currentFieldToThrowIndex;
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

    public static ArrayList<Integer> getFieldsToHit() {
        return fieldsToHit;
    }

    public static void setFieldsToHit(ArrayList<Integer> fieldsToHit) {
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

    public static void setCurrentFieldToThrowIndex(int currentFieldToThrowIndex) {
        Cricket.currentFieldToThrowIndex = currentFieldToThrowIndex;
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
        saveThrowFields();
        goToNextRoundOrPlayer();
    }

    @Override
    public void back() {
        clearThrowTextFields();
        clearFieldsHits();
        saveThrowFields();
        goToPreviousRoundOrPlayer();
    }

    private void clearFieldsHits() {

        if( currentPlayer != 1 && gamesRepositoryImpl.getIndexOfRound(this) != 0)
            return;
        if (gamesRepositoryImpl.getIndexOfRound(this) == 0){
            for (int hittedField : players[currentPlayer-1].getHittedFields()) {
                hittedField = 0;
            }
        }
        else {
            int[] currenRoundtHitedFields = players[currentPlayer - 1].getHittedFields();
            int[] previousRoundHittedFields = ((PlayerCricket) gamesRepositoryImpl.getPreviousRound().getPlayers()[currentPlayer - 1]).getHittedFields();
            currenRoundtHitedFields = previousRoundHittedFields;
        }
    }


    @Override
    public void throwDart(MouseEvent event) {

        int currentThrow = players[currentPlayer - 1].getCurrentThrow();
        if(currentThrow > 3)
            return;
        // Geting cartesian coordinates of cursor over dartboard
        double xCartesian = event.getX() - 248;
        double yCartesian = -(event.getY() - 248.5);
        int radiusIndex = getRadiusIndex(xCartesian, yCartesian);
        int angleIndex = getAngleIndex(xCartesian, yCartesian);
        String fieldName = new String();


        if (radiusIndex != 7){
            for (Filters.IndexMapper indexMapper : Utilities.filters.getIndexMapperList()) {
                if (indexMapper.hasFieldName(radiusIndex, angleIndex)) {
                    fieldName = indexMapper.getFieldName();
                    break;
                }
            }
            soundPlayer.playSound(fieldName);


            System.out.println(fieldName);

            writeFieldNameAndSetFocus(currentThrow, fieldName);
            players[currentPlayer - 1].setCurrentThrow(currentThrow+1);
            saveThrowFields();
            displayRound();
        }
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
        players[currentPlayer-1].setCurrentThrow(throwNumber);
    }

    @Override
    public Playable cloneRound() {
        Cricket cricket = new Cricket();

        PlayerCricket[] playersArray = new PlayerCricket[playersQuantity];
        Arrays.fill(playersArray,new PlayerCricket());
        cricket.players = playersArray;
        // Copying players
        for(int i = 0; i<this.players.length; i++){
            cricket.players[i] = this.players[i].clonePlayer();
        }
        return cricket;
    }

    @Override
    public void saveThrowFields() {
        for (int i = 0; i < 3; i++)
            this.players[currentPlayer-1].setThrowFieldsByIndex(i,new String(boardController.getThrowTextFieldArray()[i].getText()));
        calculatePoints();
    }

    @Override
    public void calculatePoints() {

        if(gamesRepositoryImpl.getIndexOfRound(this) == 0){
            players[currentPlayer-1].getHittedFields()[currentFieldToThrowIndex] = 0;
            players[currentPlayer-1].setPoints(0);
        }
        else{
            players[currentPlayer-1].getHittedFields()[currentFieldToThrowIndex]
                    = ((PlayerCricket)gamesRepositoryImpl.getPreviousRound().getPlayers()[currentPlayer-1]).getHittedFields()[currentFieldToThrowIndex];
            int previousRoundPoints = gamesRepositoryImpl.getPreviousRound().getPlayers()[currentPlayer-1].getPoints();
            players[currentPlayer-1].setPoints(previousRoundPoints);
        }

        int thrownFields = 0;
        int thrownPoints = 0;
        // Each loop step for throw
        for (int i = 0; i < 3; i++) {
            ThrowValues throwValue = parsethrowFieldNameIntoThrowValues(players[currentPlayer-1].getThrowFieldsValues()[i]);
            // Calulating thrown points value
            if(throwValue.getValue() == fieldsToHit.get(currentFieldToThrowIndex)) {
                // jezeli i zmiana current to hit
                thrownFields = throwValue.getMulitplier();

                int throwFieldsToAdd = 0;
                if(players[currentPlayer-1].getHittedFields()[currentFieldToThrowIndex] >= 3) {
                    throwFieldsToAdd = thrownFields;
                }
                else
                    throwFieldsToAdd = players[currentPlayer-1].getHittedFields()[currentFieldToThrowIndex] + thrownFields -3;
                if(throwFieldsToAdd < 0)
                    throwFieldsToAdd = 0;
                players[currentPlayer-1].getHittedFields()[currentFieldToThrowIndex] += thrownFields;

                thrownPoints = throwFieldsToAdd * throwValue.getValue();
                int oldPoints = players[currentPlayer-1].getPoints();

                if(isFieldClosed()){
                    // players[currentPlayer-1].getHittedFields()[currentFieldToThrowIndex] += thrownFields;
                    checkIfWinner();
                    currentFieldToThrowIndex++;
                    thrownPoints = 0;
                }
                players[currentPlayer-1].setPoints(oldPoints + thrownPoints);
                thrownFields = 0;
                thrownPoints = 0;
            }


        }
        System.out.print(players[currentPlayer-1].getHittedFields()[currentFieldToThrowIndex]+"\t"+currentFieldToThrowIndex);
        System.out.println();
    }

    private boolean isFieldClosed() {
        for (int i = 0; i < playersQuantity; i++) {
            if(players[i].getHittedFields()[currentFieldToThrowIndex] < 3)
                return false;
        }
        return true;
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

    /**
     * This method parses dart boards field name/content eg. "SIGNLE 2" or even "44"(theres not such field but
     * player can enter sum of all thrown fields instead of clicking on board), into ThrowValues object.
     * @param fieldContent name of the dart board field
     * @return ThrowValues object, which containt field value and multiplier
     */
    private ThrowValues parsethrowFieldNameIntoThrowValues(String fieldContent){

        // If regular integer
        int value;
        try{
            value = Integer.parseInt(fieldContent);
            if(value < 0)
                return new ThrowValues(0,1);
            else
                return new ThrowValues(value,1);
        }catch (Exception e){
        }

        // Deleting digits and letters
        String stringValue = fieldContent.replaceAll("[^0-9]","");
        String multiplierString = fieldContent.replaceAll("[^a-zA-Z]","");

        // If stringValue has digits, parse to int
        if(!stringValue.equals("")){
            value = Integer.parseInt(stringValue);
        }
        else
            return new ThrowValues(0,0);

        // Defining multiplier
        int multiplier;
        if(multiplierString.equalsIgnoreCase("single"))
            multiplier=1;
        else if(multiplierString.equalsIgnoreCase("double"))
            multiplier=2;
        else if(multiplierString.equalsIgnoreCase("triple"))
            multiplier=3;
            // if there's another word but not multipier nam
        else
            return new ThrowValues(0,0);

        return new ThrowValues(value,multiplier);
    }

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
                if(players[p-1].getHittedFields()[i] == 0)
                    ((Label)node2).setText("");
                else if(players[p-1].getHittedFields()[i] == 1)
                    ((Label)node2).setText("I");
                else if(players[p-1].getHittedFields()[i] == 2)
                    ((Label)node2).setText("II");
                else /*(players[p-1].getHittedFields()[i] >= 3)*/
                    ((Label)node2).setText("III");
            }
        }

        // Displaying all players points
        for(int i = 1; i <= Cricket.playersQuantity; i++) {
            Node node1 = playerVBoxes.get(i);
            Node node2 = ((VBox) node1).getChildren().get(8);
            ((Label)node2).setText(String.valueOf(players[i-1].getPoints()));
        }
    }

    private void displayFieldsHighlight() {
        for(int i = 1; i <= Cricket.playersQuantity; i++){
            Node node = boardController.getCricketsScoreTable().getChildren().get(i);
            ((VBox)node).getChildren().get(7).setStyle(null);
        }
        Node node = boardController.getCricketsScoreTable().getChildren().get(currentPlayer);
        ((VBox)node).getChildren().get(7).setStyle("-fx-background-color: #0388fc;");

        // Removing Highlighting cricket fields
        for (int i = 0; i < 7; i++) {
            Label label = (Label)boardController.getCricketsFields().getChildren().get(i);
            label.setStyle(null);
        }

        for (int i = 0; i < 7; i++) {
            Label label = (Label)boardController.getCricketsFields().getChildren().get(i);
            //int fieldToThrow = Cricket.fieldsToHit.get(currentFieldToThrowIndex);
            if(i == currentFieldToThrowIndex){
                label.setStyle("-fx-background-color: #0388fc;");
                break;
            }
        }

    }

    private void goToNextRoundOrPlayer() {
        if(gamesRepositoryImpl.getIndexOfRound(this)+1 < Cricket.roundsMaxNumber || currentPlayer != playersQuantity){
            // setting current throw to one to start from 1st throw after back round
            players[currentPlayer - 1].setCurrentThrow(1);
            // if last player in round
            if (currentPlayer == Cricket.playersQuantity){
                gamesRepositoryImpl.pushRound(this.cloneRound());
            }
            else
                currentPlayer++;

            boardController.getThrowField1().requestFocus();
            gamesRepositoryImpl.getCurrentRound().displayRound();
        }

    }

    private void goToPreviousRoundOrPlayer() {
        // if there's not 1st player in 1s round
        if(gamesRepositoryImpl.getIndexOfRound(this) > 0 || currentPlayer != 1){
            if(currentPlayer == 1){
                gamesRepositoryImpl.pullRound();
                ((Cricket)gamesRepositoryImpl.getCurrentRound()).setCurrentPlayer(playersQuantity);
            }
            else
                currentPlayer--;
        }
        else
            return;
        gamesRepositoryImpl.getCurrentRound().displayRound();
    }

    private void writeFieldNameAndSetFocus(int currentThrow, String fieldName) {
        if (currentThrow == 1) {
            boardController.getThrowField1().setText(new String(fieldName));
            boardController.getThrowField2().requestFocus();
        } else if (currentThrow == 2) {
            boardController.getThrowField2().setText(new String(fieldName));
            boardController.getThrowField3().requestFocus();
        } else if (currentThrow == 3)
            boardController.getThrowField3().setText(new String(fieldName));
    }

    private void checkIfWinner() {

        for (int i = 0; i < playersQuantity; i++)
            for(int j=0;j<fieldsToHit.size();j++){
                if(players[currentPlayer-1].getHittedFields()[i] < 3)
                    return;
                if(players[currentPlayer-1].getPoints() < players[i].getPoints() && i != currentPlayer-1)
                    return;
            }
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Winner");
        alert.setHeaderText(players[currentPlayer-1].getName()+ " Won!!!"+"\nCongratulations!");
        alert.showAndWait();
        soundPlayer.playSound("win");
        boardController.getMainStackPane().setDisable(true);
        return;
    }

    private void clearThrowTextFields() {
        if( currentPlayer != 1 && gamesRepositoryImpl.getIndexOfRound(this) != 0){
            for (int i = 0; i < 3; i++)
                players[currentPlayer - 1].setThrowFieldsByIndex(i, new String());
        }
        for (int i = 0; i < 3; i++){
            boardController.getThrowTextFieldArray()[i].setText(new String());
        }
    }

}
