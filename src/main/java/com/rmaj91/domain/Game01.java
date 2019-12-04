package com.rmaj91.domain;

import com.rmaj91.controller.BoardController;
import com.rmaj91.controller.Game01Controller;
import com.rmaj91.controller.MainController;
import com.rmaj91.interfaces.Playable;
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


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.rmaj91.utility.Utilities.getAngleIndex;
import static com.rmaj91.utility.Utilities.getRadiusIndex;


public class Game01 implements Playable, Serializable {

    /*Static Dependencies*/
    private static SoundPlayer soundPlayer;
    private static BoardController boardController;
    private static Game01Controller game01Controller;
    private static GamesRepositoryImpl gamesRepository;
    private static MainController mainController;

    /*Static Variables*/
    private static boolean doubleOut;
    private static int numberOfPlayers;
    private static int maxNumberOfRounds;

    /*Variables*/
    private ArrayList<Player01> players;
    private Player01 currentPlayer;

    /*Constructor*/
    public Game01() {

    }

    /*Copying constructor*/
    public Game01(Game01 game01) {
        this.players = new ArrayList<>();
        for (int i = 0; i < numberOfPlayers; i++) {
            Player01 player01 = new Player01(game01.players.get(i));
            this.players.add(player01);
        }
        currentPlayer = this.players.get(0);

    }





    /*Getters & Setter*/
    ////////////////////


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

    public static void setMainController(MainController mainController) {
        Game01.mainController = mainController;
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







    public static void setStaticVariables(boolean doubleOut, int numberOfPlayers, int numberOfRounds) {
        Game01.doubleOut = doubleOut;
        Game01.numberOfPlayers = numberOfPlayers;
        Game01.maxNumberOfRounds = numberOfRounds;
    }


    /*Methods from Playable*/
    /////////////////////////
    @Override
    public void goToPreviousPlayerOrRound() {
        clearThrowTextFields();
        saveThrowFields();
        goToPreviousRoundOrPlayer();
    }


    @Override
    public void throwDart(MouseEvent event) {
        int currentThrow = currentPlayer.getCurrentThrow();
        if (currentThrow > 3)
            return;
        // Geting cartesian coordinates of cursor over dartboard
        double xCartesian = event.getX() - 248;
        double yCartesian = -(event.getY() - 248.5);
        int radiusIndex = getRadiusIndex(xCartesian, yCartesian);
        int angleIndex = getAngleIndex(xCartesian, yCartesian);
        String fieldName = new String();

        if (radiusIndex != 7) {
            for (Filters.IndexMapper indexMapper : Utilities.filters.getIndexMapperList()) {
                if (indexMapper.hasFieldName(radiusIndex, angleIndex)) {
                    fieldName = indexMapper.getFieldName();
                    break;
                }
            }
            soundPlayer.playSound(fieldName);
            writeFieldNameAndSetFocus(currentThrow, fieldName);
            currentPlayer.setCurrentThrow(currentThrow + 1);
            saveThrowFields();
            displayRoundState();
        }
    }


    @Override
    public void setBoardViewVisible() {
        game01Controller.removeAndCreatePlayersVBoxes(game01Controller.getNumberOfPlayers());

        this.displayRoundState();
        boardController.toFront();
        boardController.getMainStackPane().setDisable(false);
        boardController.getGame01PlayersTable().toFront();
        boardController.getThrowField1().requestFocus();
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
        calculatePoints();
    }


    @Override
    public void calculatePoints() {
        int currentPlayerIndex = players.indexOf(currentPlayer);
        int previousRoundPoints = ((Game01) gamesRepository.getPreviousRound()).players.get(currentPlayerIndex).getPoints();
        int currentRoundPoints = previousRoundPoints;
        int totalThrownValue = 0;

        for (int i = 0; i < 3; i++) {
            ThrowValues throwValue = parseThrowFieldContentIntoThrowValues(currentPlayer.getThrowFieldsContentByIndex(i));
            // Calulating thrown points value
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
        // If everything went well, save new points
        currentPlayer.setPoints(currentRoundPoints);
    }

    @Override
    public void NextButton() {
        saveThrowFields();
        goToNextPlayerOrRound();
        boardController.getThrowField1().requestFocus();
        gamesRepository.getCurrentRound().displayRoundState();

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


    private void displayPlayerNameHighLight() {
        for (int i = 0; i < numberOfPlayers; i++) {
            Node node = boardController.getGame01PlayersTable().getChildren().get(i);
            ((VBox) node).getChildren().get(0).setStyle(null);
        }
        Node node = boardController.getGame01PlayersTable().getChildren().get(players.indexOf(currentPlayer));
        ((VBox) node).getChildren().get(0).setStyle("-fx-background-color: #0388fc;");
    }


    private void displayAllPlayersPointsAndNames() {
        ObservableList<Node> playerVBoxes = boardController.getGame01PlayersTable().getChildren();
        for (int i = 0; i < numberOfPlayers; i++) {
            Node node1 = playerVBoxes.get(i);
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
    }


    private ThrowValues parseThrowFieldContentIntoThrowValues(String fieldContent) {

        // If regular integer
        int value;
        try {
            value = Integer.parseInt(fieldContent);
            if (value < 0)
                return new ThrowValues(0, 1);
            else
                return new ThrowValues(value, 1);
        } catch (Exception e) {
        }

        // Deleting digits and letters
        String stringValue = fieldContent.replaceAll("[^0-9]", "");
        String multiplierString = fieldContent.replaceAll("[^a-zA-Z]", "");

        // If stringValue has digits, parse to int
        if (!stringValue.equals("")) {
            value = Integer.parseInt(stringValue);
        } else
            return new ThrowValues(0, 0);

        // Defining multiplier
        int multiplier;
        if (multiplierString.equalsIgnoreCase("single"))
            multiplier = 1;
        else if (multiplierString.equalsIgnoreCase("double"))
            multiplier = 2;
        else if (multiplierString.equalsIgnoreCase("triple"))
            multiplier = 3;
            // if there's another word but not multipier nam
        else
            return new ThrowValues(0, 0);

        return new ThrowValues(value, multiplier);
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
        // Deleting throwTextFields content
        for (int i = 0; i < 3; i++) {
            currentPlayer.setThrowFieldsByIndex(i, new String());
        }
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
        int currentPlayerNumber = players.indexOf(currentPlayer) + 1;
        if (!(currentPlayerNumber == 1 && gamesRepository.getNumberOfRound(this) == 1)) {
            for (int i = 0; i < 3; i++) {
                currentPlayer.setThrowFieldsByIndex(i, new String());
            }
            for (int i = 0; i < 3; i++) {
                boardController.getThrowTextField(i).setText(new String());
            }
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
                clearThrowTextFields();
                currentPlayer = players.get(currentPlayerIndex - 1);
            }
        } else
            return;
        gamesRepository.getCurrentRound().displayRoundState();
        System.out.println(players.indexOf(currentPlayer));
    }


}
