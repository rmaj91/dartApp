package com.rmaj91.domain;

import com.rmaj91.controller.BoardController;
import com.rmaj91.controller.CricketController;
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

import static com.rmaj91.utility.Utilities.getAngleIndex;
import static com.rmaj91.utility.Utilities.getRadiusIndex;

public class MasterCricket implements Playable, Serializable {

    //==================================================================================================
    // Static Dependencies
    //==================================================================================================
    private static SoundPlayer soundPlayer;
    private static BoardController boardController;
    private static CricketController cricketController;
    private static GamesRepositoryImpl gamesRepository;


    //==================================================================================================
    // Static Properties
    //==================================================================================================
    private static int numberOfPlayers;
    private static int maxNumberOfRounds;
    private static int currentFieldToThrowIndex;
    private static ArrayList<Integer> fieldsToThrow;


    //==================================================================================================
    // Properties
    //==================================================================================================
    private ArrayList<PlayerMasterCricket> players;
    private PlayerMasterCricket currentPlayer;


    //==================================================================================================
    // Constructors
    //==================================================================================================
    public MasterCricket() {
    }

    public MasterCricket(MasterCricket masterCricket) {
        this.players = new ArrayList<>();
        for (int i = 0; i < numberOfPlayers; i++) {
            PlayerMasterCricket playerMasterCricket = new PlayerMasterCricket(masterCricket.players.get(i));
            this.players.add(playerMasterCricket);
        }
        this.currentPlayer = this.players.get(0);
    }


    //region Getters/Setters @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    //==================================================================================================
    // Getters/Setters
    //==================================================================================================
    public ArrayList<PlayerMasterCricket> getPlayers() {
        return players;
    }

    public static void setNumberOfPlayers(int numberOfPlayers) {
        MasterCricket.numberOfPlayers = numberOfPlayers;
    }

    public static void setMaxNumberOfRounds(int maxNumberOfRounds) {
        MasterCricket.maxNumberOfRounds = maxNumberOfRounds;
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
        MasterCricket.fieldsToThrow = fieldsToThrow;
    }

    public static void setCurrentFieldToThrowIndex(int currentFieldToThrowIndex) {
        MasterCricket.currentFieldToThrowIndex = currentFieldToThrowIndex;
    }

    public void setPlayers(ArrayList<PlayerMasterCricket> players) {
        this.players = players;
    }

    public void setCurrentPlayer(PlayerMasterCricket playerMasterCricket) {
        currentPlayer = playerMasterCricket;
    }

    public static ArrayList<Integer> getFieldsToThrow() {
        return fieldsToThrow;
    }

    public static void setSoundPlayer(SoundPlayer soundPlayer) {
        MasterCricket.soundPlayer = soundPlayer;
    }

    public static void setBoardController(BoardController boardController) {
        MasterCricket.boardController = boardController;
    }

    public static void setCricketController(CricketController cricketController) {
        MasterCricket.cricketController = cricketController;
    }

    public static void setGamesRepository(GamesRepositoryImpl gamesRepository) {
        MasterCricket.gamesRepository = gamesRepository;
    }
    //endregion


    //==================================================================================================
    // Static Assesor
    //==================================================================================================
    public static void setStaticVariables(int numberOfPlayers, int maxNumberOfRounds, int currentFieldToThrowIndex) {
        MasterCricket.numberOfPlayers = numberOfPlayers;
        MasterCricket.maxNumberOfRounds = maxNumberOfRounds;
        MasterCricket.currentFieldToThrowIndex = currentFieldToThrowIndex;
    }



    //==================================================================================================
    // Methods from Playable interface
    //==================================================================================================



    @Override
    public void displayRoundState() {
        int currentRound = gamesRepository.getNumberOfRound(this);
        boardController.getRoundsLabel().setText("Round: " + currentRound + "/" + MasterCricket.maxNumberOfRounds);
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
    public void BackButton() {
        clearThrowTextFields();
        restorePlayerFieldsHitsFromPreviousRound();
        goToPreviousRoundOrPlayer();
    }

    @Override
    public void saveThrowFields() {
        for (int i = 0; i < 3; i++)
            currentPlayer.setThrowFieldsByIndex(i, new String(boardController.getThrowTextField(i).getText()));
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
        boardController.getAverageLabel().setVisible(false);
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
    public void calculatePoints() {
        getHitsFromPreviousRound();
        getPointsFromPreviousPlayer();
        setNewStaticCurrentFieldToThrow();
        for (int i = 0; i < 3; i++) {
            ThrowValues throwValue = parseThrowFieldNameIntoThrowValues(currentPlayer.getThrowFieldContent(i));
            if (throwValue.getValue() == fieldsToThrow.get(currentFieldToThrowIndex) && throwValue.getMulitplier() != 0 ) {

                int currentThrowPointsToAdd = getThrowFieldsToAddPoints(throwValue) * throwValue.getValue();
                if (isFieldClosed()) {
                    currentThrowPointsToAdd = 0;
                    if (currentFieldToThrowIndex < 6) {
                        currentFieldToThrowIndex++;
                    }
                }
                addPointToOtherPlayers(currentThrowPointsToAdd);
                checkIfWinner();

            } else {
                addPointsToCurrentPlayer(throwValue);
            }
        }
    }




    //==================================================================================================
    // Private Methods
    //==================================================================================================
    private int getThrowFieldsToAddPoints(ThrowValues throwValue) {
        int currentThrowHitsToTargetField = throwValue.getMulitplier();
        int throwFieldsToAddPoints = getThrowFieldsToAddPoints(currentThrowHitsToTargetField);
        int currentHitsByIndex = currentPlayer.getHittedFieldsByIndex(currentFieldToThrowIndex);
        int throwHitsToAdd = currentThrowHitsToTargetField + currentHitsByIndex;
        currentPlayer.setHittedFieldsbyIndex(currentFieldToThrowIndex, throwHitsToAdd);
        return throwFieldsToAddPoints;
    }

    private void addPointsToCurrentPlayer(ThrowValues throwValue) {
        int currentPlayerIndex = players.indexOf(currentPlayer);
        int currentPoints = currentPlayer.getPointsByPlayerIndex(currentPlayerIndex);
        int newPoints = currentPoints + throwValue.getValue();
        currentPlayer.setPointsByPlayerIndex(currentPlayerIndex, newPoints);
    }

    private void restorePlayerFieldsHitsFromPreviousRound() {
        int indexOfCurrentPlayer = players.indexOf(currentPlayer);
        int[] previousRoundHittedFields = ((MasterCricket) gamesRepository.getPreviousRound())
                .players.get(indexOfCurrentPlayer).getHittedFields();
        int[] deepCopyOfHittedFields = new int[7];
        for (int i = 0; i < 7; i++) {
            deepCopyOfHittedFields[i] = previousRoundHittedFields[i];
        }
        currentPlayer.setHittedFields(deepCopyOfHittedFields);
        setNewStaticCurrentFieldToThrow();
    }

    private void setNewStaticCurrentFieldToThrow() {
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

    private void addPointToOtherPlayers(int currentThrowPointsToAdd) {
        for (int i = 0; i < numberOfPlayers; i++) {
            if (players.get(i).getHittedFieldsByIndex(currentFieldToThrowIndex) < 3) {
                int currentSubroundPoints = currentPlayer.getPointsByPlayerIndex(i);
                int newPoints = currentSubroundPoints + currentThrowPointsToAdd;
                currentPlayer.setPointsByPlayerIndex(i, newPoints);
            }
        }
    }

    private int getThrowFieldsToAddPoints(int currentThrowHitsToTargetField) {
        int throwFieldsToAddPoints;
        if (currentPlayer.getHittedFieldsByIndex(currentFieldToThrowIndex) >= 3) {
            throwFieldsToAddPoints = currentThrowHitsToTargetField;
        } else
            throwFieldsToAddPoints = currentPlayer.getHittedFieldsByIndex(currentFieldToThrowIndex)
                    + currentThrowHitsToTargetField - 3;
        if (throwFieldsToAddPoints < 0)
            throwFieldsToAddPoints = 0;
        return throwFieldsToAddPoints;
    }

    private void getHitsFromPreviousRound() {
        int currentPlayerIndex = players.indexOf(currentPlayer);
        int[] previousRoundThrownFieldsByIndex = ((MasterCricket) gamesRepository.getPreviousRound())
                .players.get(currentPlayerIndex).getHittedFields();
        for (int i = 0; i < 7; i++)
            currentPlayer.setHittedFieldsbyIndex(i, previousRoundThrownFieldsByIndex[i]);
    }

    private void getPointsFromPreviousPlayer() {
        int currentPlayerIndex = players.indexOf(currentPlayer);
        int[] deepCopyOfPointsFromPreviousPlayer = new int[numberOfPlayers];

        if (currentPlayerIndex == 0) {
            for (int i = 0; i < numberOfPlayers; i++) {
                int previousPlayerPoints = ((MasterCricket) gamesRepository.getPreviousRound())
                        .players.get(numberOfPlayers - 1).getPointsByPlayerIndex(i);
                deepCopyOfPointsFromPreviousPlayer[i] = previousPlayerPoints;
            }
        } else {
            for (int i = 0; i < numberOfPlayers; i++) {
                int previousPlayerPoints = players.get(currentPlayerIndex - 1).getPointsByPlayerIndex(i);
                deepCopyOfPointsFromPreviousPlayer[i] = previousPlayerPoints;
            }
        }
        currentPlayer.setPoints(deepCopyOfPointsFromPreviousPlayer);
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
    private ThrowValues parseThrowFieldNameIntoThrowValues(String fieldContent) {

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
            multiplier = 0;
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


    private void checkIfWinner() {
        for (int i = 0; i < 7; i++) {
            if (currentPlayer.getHittedFieldsByIndex(i) < 3)
                return;
        }

        int currentPlayerIndex = players.indexOf(currentPlayer);
        for (int i = 0; i < numberOfPlayers; i++) {
            if (currentPlayer.getPointsByPlayerIndex(currentPlayerIndex) > currentPlayer.getPointsByPlayerIndex(i) && currentPlayerIndex != i)
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
        if (gamesRepository.getNumberOfRound(this) < MasterCricket.maxNumberOfRounds || currentPlayerNumber != numberOfPlayers) {
            currentPlayer.setCurrentThrow(1);
            if (currentPlayerNumber == MasterCricket.numberOfPlayers)
                gamesRepository.pushRound(new MasterCricket(this));
            else
                currentPlayer = players.get(players.indexOf(currentPlayer) + 1);

            ((MasterCricket) gamesRepository.getCurrentRound()).currentPlayer.setCurrentThrow(1);
        }
        ((MasterCricket) gamesRepository.getCurrentRound()).getPointsFromPreviousPlayer();
        gamesRepository.getCurrentRound().displayRoundState();
    }

    private void goToPreviousRoundOrPlayer() {
        int currentPlayerIndex = players.indexOf(currentPlayer);
        if (gamesRepository.getNumberOfRound(this) > 1 || currentPlayerIndex != 0) {
            if (currentPlayerIndex == 0) {
                gamesRepository.pullRound();
                PlayerMasterCricket playerFromPreviousRound = ((MasterCricket) gamesRepository.getCurrentRound()).getPlayers().get(numberOfPlayers - 1);
                ((MasterCricket) gamesRepository.getCurrentRound()).setCurrentPlayer(playerFromPreviousRound);
            } else {
                //clearThrowTextFields();
                currentPlayer = players.get(currentPlayerIndex - 1);
            }
        }
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
        for (int i = 1; i <= MasterCricket.numberOfPlayers; i++) {
            Node node = boardController.getCricketsScoreTable().getChildren().get(i);
            ((VBox) node).getChildren().get(7).setStyle(null);
        }
    }


    private void displayPlayersPoints() {
        ObservableList<Node> playerVBoxes = boardController.getCricketsScoreTable().getChildren();
        for (int i = 1; i <= MasterCricket.numberOfPlayers; i++) {
            Node node1 = playerVBoxes.get(i);
            Node node2 = ((VBox) node1).getChildren().get(8);
            ((Label) node2).setText(String.valueOf(currentPlayer.getPointsByPlayerIndex(i - 1)));
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