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
	Player[] players;
	private int currentPlayer;

	/*Constructor*/
	public Game01() {
		currentPlayer=1;
	}

	/*Setters & Getters*/
	public static void setSoundPlayer(SoundPlayer soundPlayer) {
		Game01.soundPlayer = soundPlayer;
	}

	public void setPlayers(Player[] players) {
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

	public Player[] getPlayer() {
		return players;
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


	/*METHODS FROM PLAYABLE*/

	@Override
	public int getCurrentPlayer() {
		return currentPlayer;
	}

	@Override
	public Playable cloneRound() {
		Game01 game01 = new Game01();

		Player[] playersArray = new Player[playersQuantity];
		Arrays.fill(playersArray,new Player());
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
		// if there's not last player in last round
		if(gamesRepositoryImpl.getIndexOfRound(this)+1 < Game01.roundsMaxNumber || currentPlayer != playersQuantity){

			// setting current throw to one to start from 1st throw after back round
			players[currentPlayer-1].setCurrentThrow(1);
			// if last player in round
			if(currentPlayer  == Game01.playersQuantity)
				gamesRepositoryImpl.pushRound(this.cloneRound());
			else
				currentPlayer++;

			boardController.getThrowField1().requestFocus();
			gamesRepositoryImpl.getCurrentRound().displayRound();
		}
		else
			return;
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
		// Defining dart board field scope indexes
		int radiusIndex = Utilities.getRadiusIndex(xCartesian, yCartesian);
		int angleIndex = Utilities.getAngleIndex(xCartesian, yCartesian);
		String fieldName = new String();

		// If current throw > 3 or mouse clicked out of dart board
		if (currentThrow > 3 || radiusIndex == 7)
			return;
		else {
			for (Filters.IndexMapper indexMapper : Utilities.filters.getIndexMapperList()) {
				if (indexMapper.hasFieldName(radiusIndex, angleIndex)) {
					fieldName = indexMapper.getFieldName();
					break;
				}
			}
			// Playing sound of clicked field
			soundPlayer.playSound(fieldName);

			// todo, dont delete INFO STUFF
			////////////////////////////////////////////
			System.out.print("Rzut nr: " + currentThrow + " Pressed: x = " + xCartesian + "  y = " + yCartesian + "\t");
			System.out.print(fieldName + "\t");
			System.out.println(Game01.parsethrowFieldName(fieldName));
			//////////////////////////////////////////////////

			// Writing fieldName into throwTextFields and setting focus on the next textField
			if (currentThrow == 1) {
				boardController.getThrowField1().setText(new String(fieldName));
				boardController.getThrowField2().requestFocus();
			} else if (currentThrow == 2) {
				boardController.getThrowField2().setText(new String(fieldName));
				boardController.getThrowField3().requestFocus();
			} else if (currentThrow == 3)
				boardController.getThrowField3().setText(new String(fieldName));

			players[currentPlayer - 1].setCurrentThrow(currentThrow+1);
			saveThrowFields();
			displayRound();
		}
	}


	@Override
	public void displayRound() {
		// Displaying current round
		int currentRound = gamesRepositoryImpl.getIndexOfRound(this)+1;
		boardController.getRoundsLabel().setText("Round: "+ currentRound +"/"+Game01.roundsMaxNumber);

		// Displaying throwTextFields
		for (int i = 0; i < 3; i++) {
			boardController.getThrowTextFieldArray()[i].setText(players[currentPlayer-1].getThrowFieldsValues()[i]);
		}

		// Displaying current player name,points and average
		boardController.getPlayerNameLabel().setText(players[currentPlayer-1].getName());
		boardController.getPlayerPointsLabel().setText(String.valueOf(players[currentPlayer-1].getPoints()));
		boardController.getAverageLabel().setText("Average: "+ String.format("%.1f", players[currentPlayer-1].getAverage()));

		// Displaying all players points
		//todo most probable dont needed for this type of game, check later implementation
		ObservableList<Node> playerVBoxes = boardController.getGame01PlayersTable().getChildren();
		for(int i=0;i<Game01.playersQuantity;i++) {
			Node node1 = playerVBoxes.get(i);
			Node node2 = ((VBox) node1).getChildren().get(1);
			((Label)node2).setText(String.valueOf(players[i].getPoints()));
		}

		// Reset players highlight
		for(int i=0;i<Game01.playersQuantity;i++){
			Node node = boardController.getGame01PlayersTable().getChildren().get(i);
			((VBox)node).getChildren().get(0).setStyle(null);
		}

		// Highlight current player name
		Node node = boardController.getGame01PlayersTable().getChildren().get(currentPlayer-1);
		((VBox)node).getChildren().get(0).setStyle("-fx-background-color: #0388fc;");

		// Display throwtextFields content
		for (int i = 0; i < 3; i++)
			boardController.getThrowTextFieldArray()[i].setText(players[currentPlayer-1].getThrowFieldsValues()[i]);

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
		
		int oldPoints;
		if(gamesRepositoryImpl.getIndexOfRound(this) == 0)
			oldPoints = Game01.startingPoints;
		else
			// Point from previous round
			oldPoints = gamesRepositoryImpl.getPreviousRound().getPlayer()[currentPlayer-1].getPoints();
		
		int newPoints = oldPoints;
		int totalThrownValue = 0;
		// Each loop step for throw
		for (int i = 0; i < 3; i++) {
			// Parsing throw field name into ThrowValues object
			ThrowValues throwValue = parsethrowFieldName(players[currentPlayer-1].getThrowFieldsValues()[i]);
			// Calulating thrown points value
			int thrownPoints = throwValue.getValue() * throwValue.getMulitplier();
			newPoints = newPoints - thrownPoints;
			totalThrownValue += thrownPoints;

			// Checking winning conditions
			if(isWinner(newPoints,throwValue)){
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setTitle("Winner");
				alert.setHeaderText(players[currentPlayer-1].getName()+ " Won!!!"+"\nCongratulations!");
				alert.showAndWait();
				soundPlayer.playSound("win");
				boardController.getMainStackPane().setDisable(true);
				return;
			}
			// Checking if over thrown
			if(newPoints < 2){
				players[currentPlayer-1].setPoints(oldPoints);
				soundPlayer.playSound("overthrow");
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setTitle("Warning Dialog");
				alert.setHeaderText("Over Throw !!!");
				alert.showAndWait();

				// Deleting throwTextFields content
				for(int j=0;j<3;j++){
					players[currentPlayer-1].setThrowFieldsByIndex(j,new String());
				}
				players[currentPlayer-1].setCurrentThrow(1);
				boardController.getThrowField1().requestFocus();
				return;
			}
		}
		// Checking if total thrown value > 180 TIP: in dart game player cant throw more than 180 in one round
		if(totalThrownValue > 180){
			soundPlayer.playSound("overthrow");
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Warning Dialog");
			alert.setHeaderText("U cant get more than 180 by 3 throws !!!");
			alert.showAndWait();

			// Deleting throwTextFields content
			for(int i=0;i<3;i++){
				players[currentPlayer-1].setThrowFieldsByIndex(i,new String());
			}
			return;
		}
		// If everything went good, save new points
		players[currentPlayer-1].setPoints(newPoints);

	}

	/**
	 * This method parses dart boards field name/content eg. "SIGNLE 2" or even "44"(theres not such field but
	 * player can enter sum of all thrown fields instead of clicking on board), into ThrowValues object.
	 * @param fieldContent name of the dart board field
	 * @return ThrowValues object, which containt field value and multiplier
	 */
	public static ThrowValues parsethrowFieldName(String fieldContent){

		// If regular
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

    public static int getStartingPoints() {
        return startingPoints;
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
		boardController.getGame01PlayersTable().toFront();
        if(Game01.isDoubleOut())
            boardController.getDoubleOut().setVisible(true);
        else
			boardController.getDoubleOut().setVisible(false);
    }


	///////////////////////////////////////
	// PRIVATE METHODS				   	//
	///////////////////////////////////////

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
		if(doubleOut && newPoints == 0 && throwValue.getMulitplier() == 2){
			return true;
		}
		else if( newPoints == 0 && !doubleOut){
			return true;
		}
		else
			return false;
	}

}
