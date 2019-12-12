package com.rmaj91.domain;

import com.rmaj91.controller.BoardController;
import com.rmaj91.controller.Game01Controller;
import com.rmaj91.interfaces.Playable;
import com.rmaj91.repository.GamesRepositoryImpl;
import com.rmaj91.utility.IndexMapper;
import com.rmaj91.utility.SoundPlayer;
import com.rmaj91.utility.Utilities;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.rmaj91.utility.Utilities.getAngleIndex;
import static com.rmaj91.utility.Utilities.getRadiusIndex;


public class Game01 implements Playable, Serializable {

    //==================================================================================================
    // Static Dependencies
    //==================================================================================================
    private static SoundPlayer soundPlayer;
    private static BoardController boardController;
    private static Game01Controller game01Controller;
    private static GamesRepositoryImpl gamesRepository;


    //==================================================================================================
    // Static Properties
    //==================================================================================================
    private static boolean doubleOut;
    private static int numberOfPlayers;
    private static int maxNumberOfRounds;


    //==================================================================================================
    // Properties
    //==================================================================================================
    private ArrayList<Player01> players;
    private Player01 currentPlayer;


    //==================================================================================================
    // Constructors
    //==================================================================================================
    public Game01() {
    }

    public Game01(Game01 game01) {
        this.players = new ArrayList<>();
        for (int i = 0; i < numberOfPlayers; i++) {
            Player01 player01 = new Player01(game01.players.get(i));
            this.players.add(player01);
        }
        currentPlayer = this.players.get(0);
    }


    //region Getters/Setters @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    //==================================================================================================
    // Getters/Setters
    //==================================================================================================
    public static void setDoubleOut(boolean doubleOut) {
        Game01.doubleOut = doubleOut;
    }

    public static void setNumberOfPlayers(int numberOfPlayers) {
        Game01.numberOfPlayers = numberOfPlayers;
    }

    public static void setMaxNumberOfRounds(int maxNumberOfRounds) {
        Game01.maxNumberOfRounds = maxNumberOfRounds;
    }

    public static boolean isDoubleOut() {
        return doubleOut;
    }

    public static int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public static int getMaxNumberOfRounds() {
        return maxNumberOfRounds;
    }

    public static void setSoundPlayer(SoundPlayer soundPlayer) {
        Game01.soundPlayer = soundPlayer;
    }

    public static void setBoardController(BoardController boardController) {
        Game01.boardController = boardController;
    }

    public static void setGame01Controller(Game01Controller game01Controller) {
        Game01.game01Controller = game01Controller;
    }

    public static void setGamesRepository(GamesRepositoryImpl gamesRepository) {
        Game01.gamesRepository = gamesRepository;
    }

    public Player01 getCurrentPlayer() {
        return currentPlayer;
    }

    public List<Player01> getPlayers() {
        return players;
    }

    public void setCurrentPlayer(Player01 player01) {
        currentPlayer = player01;
    }

    public void setPlayers(ArrayList<Player01> players) {
        this.players = players;
    }
    //endregion


    //==================================================================================================
    // Static Custom Setter
    //==================================================================================================
    public static void setStaticVariables(boolean doubleOut, int numberOfPlayers, int numberOfRounds) {
        Game01.doubleOut = doubleOut;
        Game01.numberOfPlayers = numberOfPlayers;
        Game01.maxNumberOfRounds = numberOfRounds;
    }


    //==================================================================================================
    // Methods from Playable interface
    //==================================================================================================
    @Override
    public void BackButton() {
        clearThrowTextFields();
        saveThrowFields();
        calculatePoints();
        goToPreviousRoundOrPlayer();
    }

    @Override
    public void throwDart(MouseEvent event) {
        int currentThrow = currentPlayer.getCurrentThrow();
        if (currentThrow > 3)
            return;
        double xCartesian = event.getX() - 248;
        double yCartesian = -(event.getY() - 248.5);
        int radiusIndex = getRadiusIndex(xCartesian, yCartesian);
        int angleIndex = getAngleIndex(xCartesian, yCartesian);
        String fieldName = new String();

        if (radiusIndex != 7) {
            for (IndexMapper indexMapper : Utilities.filters.getIndexMapperList()) {
                if (indexMapper.hasFieldName(radiusIndex, angleIndex)) {
                    fieldName = indexMapper.getFieldName();
                    break;
                }
            }
            soundPlayer.playSound(fieldName);
            writeFieldNameAndSetFocus(currentThrow, fieldName);
            currentPlayer.setCurrentThrow(currentThrow + 1);
            saveThrowFields();
            calculatePoints();
            displayRoundState();
        }
    }

    @Override
    public void setBoardViewVisible() {
        game01Controller.removeAndCreatePlayersVBoxes();
        displayRoundState();
        boardController.getMainStackPane().setDisable(false);
        boardController.getCricketsScoreTable().toBack();
        boardController.toFront();
        boardController.getGame01PlayersTable().toFront();
        boardController.getThrowField1().requestFocus();
        boardController.getAverageLabel().setVisible(true);
    }

    @Override
    public void setCurrentThrowTo(int throwNumber) {
        currentPlayer.setCurrentThrow(throwNumber);
    }

    @Override
    public void saveThrowFields() {
        for (int i = 0; i < 3; i++) {
            String textFielContent = new String(boardController.getThrowTextField(i).getText());
            currentPlayer.setThrowFieldsByIndex(i, textFielContent);
        }
    }

    @Override
    public void calculatePoints() {
        int currentPlayerIndex = players.indexOf(currentPlayer);
        int previousRoundPoints = ((Game01) gamesRepository.getPreviousRound()).players.get(currentPlayerIndex).getPoints();
        currentPlayer.setPoints(previousRoundPoints);
        int currentRoundPoints = previousRoundPoints;
        int totalThrownValue = 0;

        for (int i = 0; i < 3; i++) {
            ThrowValues throwValue = parseThrowFieldContentIntoThrowValues(currentPlayer.getThrowFieldsContentByIndex(i));
            int thrownPoints = throwValue.getValue() * throwValue.getMulitplier();
            currentRoundPoints = currentRoundPoints - thrownPoints;
            totalThrownValue += thrownPoints;

            if (isWinner(currentRoundPoints, throwValue))
                return;
            if ((isOverThrow(currentRoundPoints, previousRoundPoints) || ifThrownOver180(totalThrownValue))) {
                goToNextPlayerOrRound();
                return;
            }
        }
        currentPlayer.setPoints(currentRoundPoints);
    }

    @Override
    public void NextButton() {
        saveThrowFields();
        calculatePoints();
        goToNextPlayerOrRound();
        boardController.getThrowField1().requestFocus();
    }

    @Override
    public void displayRoundState() {
        int currentRound = gamesRepository.getNumberOfRound(this);
        boardController.getRoundsLabel().setText("Round: " + currentRound + "/" + Game01.maxNumberOfRounds);
        if (doubleOut)
            boardController.getDoubleOut().setVisible(true);
        else
            boardController.getDoubleOut().setVisible(false);
        currentPlayer.display();
        displayAllPlayersPointsAndNames();
        displayPlayerNameHighLight();
    }


    //==================================================================================================
    // Private Methods
    //==================================================================================================
    private void displayPlayerNameHighLight() {
        for (int i = 0; i < numberOfPlayers; i++) {
            Node node = boardController.getGame01PlayersTable().getChildren().get(i);
            ((VBox) node).getChildren().get(0).setStyle(null);
        }
        Node node = boardController.getGame01PlayersTable().getChildren().get(players.indexOf(currentPlayer));
        ((VBox) node).getChildren().get(0).setStyle("-fx-background-color: #0388fc;");
    }

    private void displayAllPlayersPointsAndNames() {
        ObservableList<Node> playersVBoxes = boardController.getGame01PlayersTable().getChildren();
        for (int i = 0; i < numberOfPlayers; i++) {
            Node node1 = playersVBoxes.get(i);
            Node node2 = ((VBox) node1).getChildren().get(1);
            ((Label) node2).setText(String.valueOf(players.get(i).getPoints()));
        }
    }

    private void goToNextPlayerOrRound() {
        int currentPlayerNumber = players.indexOf(currentPlayer) + 1;
        if (gamesRepository.getNumberOfRound(this) < Game01.maxNumberOfRounds || currentPlayerNumber != numberOfPlayers) {
            currentPlayer.setCurrentThrow(1);

            if (currentPlayerNumber == Game01.numberOfPlayers)
                gamesRepository.pushRound(new Game01(this));
            else
                currentPlayer = players.get(players.indexOf(currentPlayer) + 1);
        }
        gamesRepository.getCurrentRound().displayRoundState();
    }


    private ThrowValues parseThrowFieldContentIntoThrowValues(String fieldContent) {

        String fieldContentWithoutDigits = fieldContent.trim().replaceAll("[0-9]", "");
        String fieldContentWithoutLetters = fieldContent.trim().replaceAll("[a-zA-Z]", "");
        int fieldValue;
        int multiplier;
        if(fieldContent.equals("")){
            fieldValue = 0;
            multiplier = 0;
        }
        else if(fieldContentWithoutDigits.equals("")) {
            fieldValue = Integer.parseInt(fieldContent);
            multiplier = 1;
        } else if (isFieldContentMapped(fieldContent)) {
            multiplier = getMultiplier(fieldContentWithoutDigits.trim());
            fieldValue = getFieldValue(fieldContentWithoutLetters.trim());
        } else {
            fieldValue = 0;
            multiplier = 0;
        }
        return new ThrowValues(fieldValue, multiplier);
    }

    private int getFieldValue(String fieldContentNoLetters) {
        int fieldValue;
        if (!fieldContentNoLetters.equals(""))
            fieldValue = Integer.parseInt(fieldContentNoLetters);
        else
            fieldValue = 25;
        return fieldValue;
    }

    private int getMultiplier(String fieldContentNoDigits) {
        int multiplier;
        if (fieldContentNoDigits.equalsIgnoreCase("single"))
            multiplier = 1;
        else if (fieldContentNoDigits.equalsIgnoreCase("double"))
            multiplier = 2;
        else if (fieldContentNoDigits.equalsIgnoreCase("barrel"))
            multiplier = 0;
        else
            multiplier = 3;
        return multiplier;
    }

    private boolean isFieldContentMapped(String fieldContent) {
        for (IndexMapper indexMapper : Utilities.filters.getIndexMapperList()) {
            if (fieldContent.equalsIgnoreCase(indexMapper.getFieldName()))
                return true;
        }
        return false;
    }

    private boolean isWinner(int newPoints, ThrowValues throwValue) {
        if ((doubleOut && newPoints == 0 && throwValue.getMulitplier() == 2)
                || newPoints == 0 && !doubleOut) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Winner");
            alert.setHeaderText(currentPlayer.getName() + " Won!!!" + "\nCongratulations!");
            alert.showAndWait();
            soundPlayer.playSound("win");
            boardController.getMainStackPane().setDisable(true);
            return true;
        } else
            return false;
    }

    private boolean isOverThrow(int newPoints, int oldPoints) {
        if (newPoints < 2) {
            playSoundAndDeleteThrows("Over Throw !!!");
            return true;
        }
        return false;
    }

    private void playSoundAndDeleteThrows(String s) {
        soundPlayer.playSound("overthrow");
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning Dialog");
        alert.setHeaderText(s);
        alert.showAndWait();
        clearThrowTextFields();
        currentPlayer.setCurrentThrow(1);
        boardController.getThrowField1().requestFocus();
    }

    private boolean ifThrownOver180(int totalThrownValue) {
        if (totalThrownValue > 180) {
            playSoundAndDeleteThrows("U cant get more than 180 by 3 throws !!!");
            return true;
        }
        return false;
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

    private void clearThrowTextFields() {
        for (int i = 0; i < 3; i++) {
            currentPlayer.setThrowFieldsByIndex(i, new String());
        }
        for (int i = 0; i < 3; i++) {
            boardController.getThrowTextField(i).setText(new String());
        }
    }

    private void goToPreviousRoundOrPlayer() {
        int currentPlayerIndex = players.indexOf(currentPlayer);
        System.out.println(currentPlayerIndex);
        if (gamesRepository.getNumberOfRound(this) > 1 || currentPlayerIndex != 0) {
            if (currentPlayerIndex == 0) {
                gamesRepository.pullRound();
                Player01 playerFromPreviousRound = ((Game01) gamesRepository.getCurrentRound()).getPlayers().get(numberOfPlayers - 1);
                ((Game01) gamesRepository.getCurrentRound()).setCurrentPlayer(playerFromPreviousRound);
            } else {
                //clearThrowTextFields();
                currentPlayer = players.get(currentPlayerIndex - 1);
            }
        }
        gamesRepository.getCurrentRound().displayRoundState();
    }


}
