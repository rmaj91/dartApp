package com.rmaj91.domain;

import com.rmaj91.controller.BoardController;
import com.rmaj91.controller.CricketController;
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

import static com.rmaj91.utility.Utilities.getAngleIndex;
import static com.rmaj91.utility.Utilities.getRadiusIndex;

public class Cricket implements Playable, Serializable {

    /*Static Dependencies*/
    private static SoundPlayer soundPlayer;
    private static BoardController boardController;
    private static CricketController cricketController;
    private static GamesRepositoryImpl gamesRepository;
    private static MainController mainController;

    /*Static Variables*/
    private static int numberOfPlayers;
    private static int maxNumberOfRounds;
    private static int currentFieldToThrowIndex;
    private static ArrayList<Integer> fieldsToThrow;

    /*Variables*/
    private ArrayList<PlayerCricket> players;
    private PlayerCricket currentPlayer;

    /*Constructor*/
    public Cricket() {
    }

    /*Copying constructor*/
    public Cricket(Cricket cricket) {
        this.players = new ArrayList<>();
        for (int i = 0; i < numberOfPlayers; i++) {
            PlayerCricket playerCricket = new PlayerCricket(cricket.players.get(i));
            this.players.add(playerCricket);
        }
        this.currentPlayer = this.players.get(0);
    }




    /*Getters & Setter*/
    ////////////////////

    public ArrayList<PlayerCricket> getPlayers() {
        return players;
    }

    public static void setNumberOfPlayers(int numberOfPlayers) {
        Cricket.numberOfPlayers = numberOfPlayers;
    }

    public static void setMaxNumberOfRounds(int maxNumberOfRounds) {
        Cricket.maxNumberOfRounds = maxNumberOfRounds;
    }

    public static int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public static int getMaxNumberOfRounds() {
        return maxNumberOfRounds;
    }

    public static int getCurrentFieldToThrowIndex() {
        return currentFieldToThrowIndex;
    }

    public static void setFieldsToThrow(ArrayList<Integer> fieldsToThrow) {
        Cricket.fieldsToThrow = fieldsToThrow;
    }

    public static void setCurrentFieldToThrowIndex(int currentFieldToThrowIndex) {
        Cricket.currentFieldToThrowIndex = currentFieldToThrowIndex;
    }

    public void setPlayers(ArrayList<PlayerCricket> players) {
        this.players = players;
    }

    public void setCurrentPlayer(PlayerCricket playerCricket) {
        currentPlayer = playerCricket;
    }

    public static ArrayList<Integer> getFieldsToThrow() {
        return fieldsToThrow;
    }

    public static void setSoundPlayer(SoundPlayer soundPlayer) {
        Cricket.soundPlayer = soundPlayer;
    }

    public static void setBoardController(BoardController boardController) {
        Cricket.boardController = boardController;
    }

    public static void setCricketController(CricketController cricketController) {
        Cricket.cricketController = cricketController;
    }

    public static void setGamesRepository(GamesRepositoryImpl gamesRepository) {
        Cricket.gamesRepository = gamesRepository;
    }

    public static void setMainController(MainController mainController) {
        Cricket.mainController = mainController;
    }


    public static void setStaticVariables(int numberOfPlayers, int maxNumberOfRounds, int currentFieldToThrowIndex) {
        Cricket.numberOfPlayers = numberOfPlayers;
        Cricket.maxNumberOfRounds = maxNumberOfRounds;
        Cricket.currentFieldToThrowIndex = currentFieldToThrowIndex;
    }



    /*Methods from Playable*/
    /////////////////////////

    @Override
    public void displayRoundState() {
        int currentRound = gamesRepository.getNumberOfRound(this);
        boardController.getRoundsLabel().setText("Round: " + currentRound + "/" + Cricket.maxNumberOfRounds);
        displayPlayersInfo();
        displayFieldsHighlight();
    }

    @Override
    public void NextButton() {
        saveThrowFields();
        calculatePoints();
        goToNextRoundOrPlayer();
        boardController.getThrowField1().requestFocus();

    }

    @Override
    public void saveThrowFields() {
        for (int i = 0; i < 3; i++)
            currentPlayer.setThrowFieldsByIndex(i, new String(boardController.getThrowTextField(i).getText()));
    }

    @Override
    public void BackButton() {
        clearThrowTextFields();
        restorePlayerFieldsHitsFromPreviousRound();
        saveThrowFields();
        calculatePoints();
        goToPreviousRoundOrPlayer();
    }

    @Override
    public void setCurrentThrowTo(int throwNumber) {
        currentPlayer.setCurrentThrow(throwNumber);
    }

    @Override
    public void setBoardViewVisible() {
        cricketController.removeAndCreatePlayersVBoxes();

        this.displayRoundState();
        boardController.getMainStackPane().setDisable(false);
        boardController.getGame01ScoreTable().toBack();
        boardController.toFront();
        boardController.getCricketsScoreTable().toFront();
        boardController.getThrowField1().requestFocus();
    }


    private void restorePlayerFieldsHitsFromPreviousRound() {
        int[] previousRoundHittedFields = ((Cricket) gamesRepository.getPreviousRound()).currentPlayer.getHittedFields();
        int[] deepCopyOfHittedFields = new int[7];
        for (int i = 0; i < 7; i++) {
            deepCopyOfHittedFields[i] = previousRoundHittedFields[i];
        }
        currentPlayer.setHittedFields(deepCopyOfHittedFields);

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < numberOfPlayers; j++) {
                if (players.get(j).getHittedFieldsByIndex(i) < 3) {
                    currentFieldToThrowIndex = i;
                    i = 7;
                    break;
                }
            }
        }
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
            calculatePoints();
            displayRoundState();
        }
    }


    @Override
    public void calculatePoints() {
        int currentPlayerIndex = players.indexOf(currentPlayer);

        int previousRoundThrownFieldsByIndex = ((Cricket) gamesRepository.getPreviousRound())
                .players.get(currentPlayerIndex).getHittedFieldsByIndex(currentFieldToThrowIndex);
        int previousRoundPoints = ((Cricket) gamesRepository.getPreviousRound()).players.get(currentPlayerIndex).getPoints();
        currentPlayer.setHittedFieldsbyIndex(currentFieldToThrowIndex, previousRoundThrownFieldsByIndex);
        currentPlayer.setPoints(previousRoundPoints);

        for (int i = 0; i < 3; i++) {
            int currentThrowHitsToTargetField = 0;
            int currentThrowPointsToAdd = 0;
            ThrowValues throwValue = parsethrowFieldNameIntoThrowValues(currentPlayer.getThrowFieldContent(i));

            if (throwValue.getValue() == fieldsToThrow.get(currentFieldToThrowIndex)) {

                currentThrowHitsToTargetField = throwValue.getMulitplier();
                int throwFieldsToAddPoints = 0;
                if (currentPlayer.getHittedFieldsByIndex(currentFieldToThrowIndex) >= 3) {
                    throwFieldsToAddPoints = currentThrowHitsToTargetField;
                } else
                    throwFieldsToAddPoints = currentPlayer.getHittedFieldsByIndex(currentFieldToThrowIndex) + currentThrowHitsToTargetField - 3;
                if (throwFieldsToAddPoints < 0)
                    throwFieldsToAddPoints = 0;

                int currentRoundHitsByIndex = currentPlayer.getHittedFieldsByIndex(currentFieldToThrowIndex);

                int throwHitsToAdd = currentThrowHitsToTargetField + currentRoundHitsByIndex;
                currentPlayer.setHittedFieldsbyIndex(currentFieldToThrowIndex, throwHitsToAdd);
                currentThrowPointsToAdd = throwFieldsToAddPoints * throwValue.getValue();
                checkIfWinner();

                if (isFieldClosed()) {
                    if(currentFieldToThrowIndex < 6)
                        currentFieldToThrowIndex++;
                    currentThrowPointsToAdd = 0;
                }
                int currentPlayerPoints = currentPlayer.getPoints();
                currentPlayer.setPoints(currentPlayerPoints + currentThrowPointsToAdd);
            }
        }
    }


    private boolean isFieldClosed() {
        for (int i = 0; i < numberOfPlayers; i++) {
            if (players.get(i).getHittedFieldsByIndex(currentFieldToThrowIndex) < 3)
                return false;
        }
        return true;
    }


    /**
     * This method parses dart boards field name/content eg. "SIGNLE 2" or even "44"(theres not such field but
     * player can enter sum of all thrown fields instead of clicking on board), into ThrowValues object.
     *
     * @param fieldContent name of the dart board field
     * @return ThrowValues object, which containt field value and multiplier
     */
    private ThrowValues parsethrowFieldNameIntoThrowValues(String fieldContent) {

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

    private void checkIfWinner() {
        for (int i = 0; i < 7; i++) {
            if (currentPlayer.getHittedFieldsByIndex(i) < 3)
                return;
        }

        int currentPlayerIndex = players.indexOf(currentPlayer);
        for (int i = 0; i < numberOfPlayers; i++) {
            if (currentPlayer.getPoints() < players.get(i).getPoints() && currentPlayerIndex != i)
                return;
        }

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Winner");
        alert.setHeaderText(currentPlayer.getName() + " Won!!!" + "\nCongratulations!");
        alert.showAndWait();
        soundPlayer.playSound("win");
        boardController.getMainStackPane().setDisable(true);
    }


    private void displayPlayersInfo() {
        displayThrowFieldsContent();
        displayPlayersHits();
        displayPlayersPoints();
    }


    private void displayFieldsHighlight() {
        removePlayerHighlight();
        removeCricketFieldHighligh();
        highlightCurrentPlayer();
        highlightCurrentCricketField();

    }


    private void goToNextRoundOrPlayer() {
        int currentPlayerNumber = players.indexOf(currentPlayer) + 1;
        if (gamesRepository.getNumberOfRound(this) < Cricket.maxNumberOfRounds || currentPlayerNumber != numberOfPlayers) {
            currentPlayer.setCurrentThrow(1);
            if (currentPlayerNumber == Cricket.numberOfPlayers) {
                gamesRepository.pushRound(new Cricket(this));

//                PlayerCricket newCurrentPlayer = ((Cricket) gamesRepository.getCurrentRound()).players.get(0);
//                ((Cricket)gamesRepository.getCurrentRound()).currentPlayer = newCurrentPlayer;
            } else
                currentPlayer = players.get(players.indexOf(currentPlayer) + 1);
        }
        gamesRepository.getCurrentRound().displayRoundState();
    }

    private void goToPreviousRoundOrPlayer() {
        int currentPlayerIndex = players.indexOf(currentPlayer);
        if (gamesRepository.getNumberOfRound(this) > 1 || currentPlayerIndex != 0) {
            if (currentPlayerIndex == 0) {
                gamesRepository.pullRound();
                PlayerCricket playerFromPreviousRound = ((Cricket) gamesRepository.getCurrentRound()).getPlayers().get(numberOfPlayers - 1);
                ((Cricket) gamesRepository.getCurrentRound()).setCurrentPlayer(playerFromPreviousRound);
            } else {
                //clearThrowTextFields();
                currentPlayer = players.get(currentPlayerIndex - 1);
            }
        } else
            return;
        gamesRepository.getCurrentRound().displayRoundState();
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

    private void highlightCurrentCricketField() {
        for (int i = 0; i < 7; i++) {
            Label label = (Label) boardController.getCricketsFields().getChildren().get(i);
            if (i == currentFieldToThrowIndex) {
                label.setStyle("-fx-background-color: #0388fc;");
                break;
            }
        }
    }

    private void highlightCurrentPlayer() {
        int currentPlayerNumber = players.indexOf(currentPlayer) + 1;
        Node node = boardController.getCricketsScoreTable().getChildren().get(currentPlayerNumber);
        ((VBox) node).getChildren().get(7).setStyle("-fx-background-color: #0388fc;");
    }

    private void removeCricketFieldHighligh() {
        for (int i = 0; i < 7; i++) {
            Label label = (Label) boardController.getCricketsFields().getChildren().get(i);
            label.setStyle(null);
        }
    }

    private void removePlayerHighlight() {
        for (int i = 1; i <= Cricket.numberOfPlayers; i++) {
            Node node = boardController.getCricketsScoreTable().getChildren().get(i);
            ((VBox) node).getChildren().get(7).setStyle(null);
        }
    }


    private void displayPlayersPoints() {
        ObservableList<Node> playerVBoxes = boardController.getCricketsScoreTable().getChildren();
        for (int i = 1; i <= Cricket.numberOfPlayers; i++) {
            Node node1 = playerVBoxes.get(i);
            Node node2 = ((VBox) node1).getChildren().get(8);
            ((Label) node2).setText(String.valueOf(players.get(i - 1).getPoints()));
        }
    }

    private void displayPlayersHits() {
        ObservableList<Node> playerVBoxes = boardController.getCricketsScoreTable().getChildren();
        for (int p = 1; p <= numberOfPlayers; p++) {
            for (int i = 0; i < 7; i++) {
                Node node1 = playerVBoxes.get(p);
                Node node2 = ((VBox) node1).getChildren().get(i);
                if (players.get(p - 1).getHittedFieldsByIndex(i) == 0)
                    ((Label) node2).setText("");
                else if (players.get(p - 1).getHittedFieldsByIndex(i) == 1)
                    ((Label) node2).setText("I");
                else if (players.get(p - 1).getHittedFieldsByIndex(i) == 2)
                    ((Label) node2).setText("II");
                else
                    ((Label) node2).setText("III");
            }
        }
    }

    private void displayThrowFieldsContent() {
        for (int i = 0; i < 3; i++) {
            boardController.getThrowTextField(i).setText(currentPlayer.getThrowFieldContent(i));
        }
    }

}
