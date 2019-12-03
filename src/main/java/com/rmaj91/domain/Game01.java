package com.rmaj91.domain;

import com.rmaj91.controller.BoardController;
import com.rmaj91.controller.Game01Controller;
import com.rmaj91.controller.MainController;
import com.rmaj91.interfaces.Playable;
import com.rmaj91.interfaces.PlayerInterface;
import com.rmaj91.repository.GamesRepositoryImpl;
import com.rmaj91.utility.Filters;
import com.rmaj91.utility.SoundPlayer;
import com.rmaj91.utility.Utilities;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


import java.io.Serializable;
import java.util.Arrays;

import static com.rmaj91.utility.Utilities.*;


public class Game01 implements Playable, Serializable {

	/*Static Dependencies*/
	private static SoundPlayer soundPlayer;
	private static BoardController boardController;
	private static Game01Controller game01Controller;
	private static GamesRepositoryImpl gamesRepositoryImpl;
	private static MainController mainController;

	/*Static Variables*/
	private static boolean doubleOut;
	private static int playersQuantity;
	private static int roundsMaxNumber;
	private static int startingPoints;

	/*Variables*/
	Player01[] players;
	private int currentPlayer;

	/*Constructor*/
	public Game01() {
		currentPlayer=1;
	}

	/*Setters & Getters*/
	public static void setSoundPlayer(SoundPlayer soundPlayer) {
		Game01.soundPlayer = soundPlayer;
	}

	public void setPlayers(Player01[] players) {
		this.players = players;
	}

	public static void setBoardController(BoardController boardController) {
		Game01.boardController = boardController;
	}

	public static void setGame01Controller(Game01Controller game01Controller) {
		Game01.game01Controller = game01Controller;
	}

	public static void setGamesRepositoryImpl(GamesRepositoryImpl gamesRepositoryImpl) {
		Game01.gamesRepositoryImpl = gamesRepositoryImpl;
	}

	public static void setRoundsMaxNumber(int roundsMaxNumber) {
		Game01.roundsMaxNumber = roundsMaxNumber;
	}

	public static void setPlayersQuantity(int playersQuantity) {
		Game01.playersQuantity = playersQuantity;
	}

	public static void setDoubleOut(boolean doubleOut) {
		Game01.doubleOut = doubleOut;
	}

	public static void setStartingPoints(int startingPoints) {
		Game01.startingPoints = startingPoints;
	}

	public static void setMainController(MainController mainController) {
		Game01.mainController = mainController;
	}



	public static boolean isDoubleOut() {
		return doubleOut;
	}

	public static int getPlayersQuantity() {
		return playersQuantity;
	}

	public static int getRoundsMaxNumber() {
		return roundsMaxNumber;

	}

	public static int getStartingPoints() {
		return startingPoints;
	}


	/*METHODS FROM PLAYABLE*/
	@Override
	public PlayerInterface[] getPlayers() {
		return players;
	}

	@Override
	public int getCurrentPlayer() {
		return currentPlayer;
	}

	@Override
	public Playable cloneRound() {
		Game01 game01 = new Game01();

		Player01[] playersArray = new Player01[playersQuantity];
		Arrays.fill(playersArray,new Player01());
		game01.players = playersArray;
		// Copying players
		for(int i = 0; i<this.players.length; i++){
			game01.players[i] = this.players[i].clonePlayer();
		}
		return game01;
	}

	@Override
	public void next() {
		gamesRepositoryImpl.getCurrentRound().saveThrowFields();
		goToNextRoundOrPlayer();
	}

	@Override
	public void back() {
		gamesRepositoryImpl.getCurrentRound().saveThrowFields();
		// if there's not 1st player in 1s round
		if(gamesRepositoryImpl.getIndexOfRound(this) > 0 || currentPlayer != 1){
			// if 1st player, pull round fro mrepository
			if(currentPlayer == 1)
				gamesRepositoryImpl.pullRound();
			// inf not set current player to previous
			else
				currentPlayer--;
		}
		else
			return;
		gamesRepositoryImpl.getCurrentRound().displayRound();
	}

	@Override
	public void throwDart(MouseEvent event) {
		int currentThrow = players[currentPlayer - 1].getCurrentThrow();

		// Geting cartesian coordinates of cursor over dartboard
		double xCartesian = event.getX() - 248;
		double yCartesian = -(event.getY() - 248.5);
		int radiusIndex = getRadiusIndex(xCartesian, yCartesian);
		int angleIndex = getAngleIndex(xCartesian, yCartesian);
		String fieldName = new String();

		if (currentThrow <= 3 || radiusIndex != 7){
			for (Filters.IndexMapper indexMapper : Utilities.filters.getIndexMapperList()) {
				if (indexMapper.hasFieldName(radiusIndex, angleIndex)) {
					fieldName = indexMapper.getFieldName();
					break;
				}
			}
			soundPlayer.playSound(fieldName);
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
		boardController.getRoundsLabel().setText("Round: "+ currentRound +"/"+Game01.roundsMaxNumber);
		displayPlayerInfo();
		displayFieldsHighlight();
	}


	@Override
	public void setCurrentThrow(int throwNumber) {
		players[currentPlayer-1].setCurrentThrow(throwNumber);
	}

	@Override
	public void saveThrowFields() {
		for (int i = 0; i < 3; i++)
			this.players[currentPlayer-1].setThrowFieldsByIndex(i,new String(boardController.getThrowTextFieldArray()[i].getText()));
		calculatePoints();
	}

	@Override
	public void calculatePoints() {
		
		int previousRoundPoints;
		if(gamesRepositoryImpl.getIndexOfRound(this) == 0)
			previousRoundPoints = Game01.startingPoints;
		else
			previousRoundPoints = gamesRepositoryImpl.getPreviousRound().getPlayers()[currentPlayer-1].getPoints();
		int currentRoundPoints = previousRoundPoints;
		int totalThrownValue = 0;
		// Each loop step for throw
		for (int i = 0; i < 3; i++) {
			ThrowValues throwValue = parsethrowFieldNameIntoThrowValues(players[currentPlayer-1].getThrowFieldsValues()[i]);
			// Calulating thrown points value
			int thrownPoints = throwValue.getValue() * throwValue.getMulitplier();
			currentRoundPoints = currentRoundPoints - thrownPoints;
			totalThrownValue += thrownPoints;

			if(isWinner(currentRoundPoints,throwValue))
				return;
			if(( isOverThrow(currentRoundPoints, previousRoundPoints) || ifThrownOver180(totalThrownValue))){
				goToNextRoundOrPlayer();
				return;
			}
		}
		// If everything went well, save new points
		players[currentPlayer-1].setPoints(currentRoundPoints);
	}


	@Override
	public void initAndDisplay() {

        for (int i = 0; i < playersQuantity; i++) {
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.BOTTOM_CENTER);
            vBox.setMinWidth(100);

            vBox.getChildren().addAll(createPlayerLabel( this.players[i].getName(),false),
                    createPlayerLabel(String.valueOf(startingPoints),true));
            boardController.getGame01PlayersTable().getChildren().add(vBox);

        }
        this.displayRound();
        boardController.toFront();
		boardController.getMainStackPane().setDisable(false);
		boardController.getGame01PlayersTable().toFront();
        if(Game01.isDoubleOut())
            boardController.getDoubleOut().setVisible(true);
        else
			boardController.getDoubleOut().setVisible(false);
    }


	///////////////////////////////////////
	// PRIVATE METHODS				   	//
	///////////////////////////////////////

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

	private Label createPlayerLabel(String text,boolean ifBold){
        Label playerLabel = new Label();
        playerLabel.setText(text);
        playerLabel.setFont(new Font("System",22));
        playerLabel.setTextFill(Color.WHITE);
        if(ifBold)
            playerLabel.setStyle("-fx-font-weight: bold");
        return playerLabel;
    }

	private boolean isWinner(int newPoints, ThrowValues throwValue) {
		if((doubleOut && newPoints == 0 && throwValue.getMulitplier() == 2)
		|| newPoints == 0 && !doubleOut){

			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Winner");
			alert.setHeaderText(players[currentPlayer-1].getName()+ " Won!!!"+"\nCongratulations!");
			alert.showAndWait();
			soundPlayer.playSound("win");
			boardController.getMainStackPane().setDisable(true);
			return true;
		}
		else
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

	private void displayFieldsHighlight() {
		for(int i = 0; i< Game01.playersQuantity; i++){
			Node node = boardController.getGame01PlayersTable().getChildren().get(i);
			((VBox)node).getChildren().get(0).setStyle(null);
		}

		Node node = boardController.getGame01PlayersTable().getChildren().get(currentPlayer-1);
		((VBox)node).getChildren().get(0).setStyle("-fx-background-color: #0388fc;");
	}

	private void displayPlayerInfo() {
		// Displaying throwTextFields
		for (int i = 0; i < 3; i++) {
			boardController.getThrowTextFieldArray()[i].setText(players[currentPlayer-1].getThrowFieldsValues()[i]);
		}

		// Displaying current player name,points and average
		boardController.getPlayerNameLabel().setText(players[currentPlayer-1].getName());
		boardController.getPlayerPointsLabel().setText(String.valueOf(players[currentPlayer-1].getPoints()));
		boardController.getAverageLabel().setText("Average: "+ String.format("%.1f", players[currentPlayer-1].getAverage()));

		// Displaying all players points
		ObservableList<Node> playerVBoxes = boardController.getGame01PlayersTable().getChildren();
		for(int i = 0; i< Game01.playersQuantity; i++) {
			Node node1 = playerVBoxes.get(i);
			Node node2 = ((VBox) node1).getChildren().get(1);
			((Label)node2).setText(String.valueOf(players[i].getPoints()));
		}
	}

	private void goToNextRoundOrPlayer() {
		if(gamesRepositoryImpl.getIndexOfRound(this)+1 < Game01.roundsMaxNumber || currentPlayer != playersQuantity){
			// setting current throw to one to start from 1st throw after back round
			players[currentPlayer - 1].setCurrentThrow(1);
			// if last player in round
			if (currentPlayer == Game01.playersQuantity)
				gamesRepositoryImpl.pushRound(this.cloneRound());
			else
				currentPlayer++;

			boardController.getThrowField1().requestFocus();
			gamesRepositoryImpl.getCurrentRound().displayRound();
		}
	}

	private boolean ifThrownOver180(int totalThrownValue) {
		if(totalThrownValue >180){
			playSoundAndDeleteThrows("U cant get more than 180 by 3 throws !!!");
			return true;
		}
		return false;
	}

	private boolean isOverThrow(int newPoints, int oldPoints) {
		if(newPoints < 2){
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
			players[currentPlayer - 1].setThrowFieldsByIndex(i, new String());
		}
		players[currentPlayer-1].setCurrentThrow(1);
		boardController.getThrowField1().requestFocus();
	}
}
