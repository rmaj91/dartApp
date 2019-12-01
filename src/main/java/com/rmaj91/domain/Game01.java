package com.rmaj91.domain;

import com.rmaj91.controller.BoardController;
import com.rmaj91.controller.Game01Controller;
import com.rmaj91.interfaces.Playable;
import com.rmaj91.repository.GamesRepositoryImpl;
import com.rmaj91.utility.Filters;
import com.rmaj91.utility.Utility;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;


import javax.swing.*;
import java.util.Arrays;


public class Game01 implements Playable {

	// dependencies //
	private static BoardController boardController;
	private static Game01Controller game01Controller;
	private static GamesRepositoryImpl gamesRepositoryImpl;
	// static variables //
	private static int roundsMaxNumber;
	private static int playersQuantity;
	private static boolean doubleOut;
	private static int startingPoints;
	// variables //
	Player[] players;
	private int currentPlayer;

	// SETTERS //
	/////////////


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

	// GETTERS //
	/////////////

	public Player[] getPlayers() {
		return players;
	}


	// Constructor //
	public Game01() {
		currentPlayer=1;
	}


	///////////////////////////
	// METHODS FROM PLAYABLE //
	///////////////////////////

	@Override
	public Playable cloneRound() {
		Game01 game01 = new Game01();

		Player[] playersArray = new Player[playersQuantity];
		Arrays.fill(playersArray,new Player());
		game01.players = playersArray;

		for(int i=0;i<this.players.length;i++){
			game01.players[i] = this.players[i].clonePlayer();
		}
		return game01;
	}

	@Override
	public void next() {

		gamesRepositoryImpl.getCurrentRound().saveThrowFields();

		if(gamesRepositoryImpl.getIndexOfRound(this)+1 < Game01.roundsMaxNumber || currentPlayer != playersQuantity){
			if(currentPlayer  == Game01.playersQuantity){
				((Game01)gamesRepositoryImpl.getCurrentRound()).getPlayers()[currentPlayer-1].setCurrentThrow(1);
				gamesRepositoryImpl.pushRound(this.cloneRound());
			}
			else{
				((Game01)gamesRepositoryImpl.getCurrentRound()).getPlayers()[currentPlayer-1].setCurrentThrow(1);
				currentPlayer++;
			}

			boardController.getThrowField1().requestFocus();
			gamesRepositoryImpl.getCurrentRound().display();
		}
		else
			return;
	}

	@Override
	public void back() {

		gamesRepositoryImpl.getCurrentRound().saveThrowFields();
		if(gamesRepositoryImpl.getIndexOfRound(this) > 0 || currentPlayer != 1){
			if(currentPlayer == 1)
				gamesRepositoryImpl.pullRound();
			else
				currentPlayer--;
		}
		else
			return;
		gamesRepositoryImpl.getCurrentRound().display();
	}

	@Override
	public void throwDart(MouseEvent event) {
		int currentThrow = players[currentPlayer - 1].getCurrentThrow();

		if (currentThrow == 4)
			return;
		else {

			double xCartesian = event.getX() - 248;
			double yCartesian = -(event.getY() - 248.5);
			int radiusIndex = Utility.getRadiusIndex(xCartesian, yCartesian);
			int angleIndex = Utility.getAngleIndex(xCartesian, yCartesian);

			String key = "";
			for (Filters.IndexMapper indexMapper : Utility.filters.getIndexMapperList()) {
				if (indexMapper.hasKey(radiusIndex, angleIndex)) {
					key = indexMapper.getKey();
					break;
				}
			}

			////////////////////////////////////////////
			System.out.print("Rzut nr: " + currentThrow + " Pressed: x = " + xCartesian + "  y = " + yCartesian + "\t");
			System.out.print(key + "\t");
			System.out.println(Game01.readValues(key));
			//////////////////////////////////////////////////

			if (currentThrow == 1) {
				boardController.getThrowField1().setText(new String(key));
				boardController.getThrowField2().requestFocus();
			} else if (currentThrow == 2) {
				boardController.getThrowField2().setText(new String(key));
				boardController.getThrowField3().requestFocus();
			} else if (currentThrow == 3)
				boardController.getThrowField3().setText(new String(key));

			players[currentPlayer - 1].setCurrentThrow(currentThrow+1);
			saveThrowFields();
			display();
		}
	}


	@Override
	public void display() {
		// Rounds //
		int currentRound = gamesRepositoryImpl.getIndexOfRound(this)+1;
		boardController.getRoundsLabel().setText("Round: "+ currentRound +"/"+Game01.roundsMaxNumber);

		// TextFields //
		boardController.getThrowField1().setText(players[currentPlayer-1].getThrowFields()[0]);
		boardController.getThrowField2().setText(players[currentPlayer-1].getThrowFields()[1]);
		boardController.getThrowField3().setText(players[currentPlayer-1].getThrowFields()[2]);

		// Big Player name,points,average //
		boardController.getPlayerNameLabel().setText(players[currentPlayer-1].getName());
		boardController.getPlayerPointsLabel().setText(String.valueOf(players[currentPlayer-1].getPoints()));
		boardController.getAverageLabel().setText("Average: "+ String.format("%.1f",players[currentPlayer-1].getAverage()));

		// display Players points //

			ObservableList<Node> playerVBoxes = boardController.getGame01PlayersTable().getChildren();
			for(int i=0;i<Game01.playersQuantity;i++) {
				Node node1 = playerVBoxes.get(i);
				Node node2 = ((VBox) node1).getChildren().get(1);
				((Label)node2).setText(String.valueOf(players[i].getPoints()));
			}

		// reset player name highlight //
		for(int i=0;i<Game01.playersQuantity;i++){
			Node node = boardController.getGame01PlayersTable().getChildren().get(i);
			((VBox)node).getChildren().get(0).setStyle(null);
		}

		// highlight player name //
		Node node = boardController.getGame01PlayersTable().getChildren().get(currentPlayer-1);
		((VBox)node).getChildren().get(0).setStyle("-fx-background-color: #0388fc;");

		// display throwfields //
		for (int i = 0; i < 3; i++)
			boardController.getThrowTextFieldArray()[i].setText(players[currentPlayer-1].getThrowFields()[i]);

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

		int newPoints;
		int difference = 0;
		int oldPoints;
		if(gamesRepositoryImpl.getIndexOfRound(this) == 0)
			oldPoints = Game01.startingPoints;
		else
			// z poprzedniej rundy
			oldPoints = ((Game01)gamesRepositoryImpl.getPreviousRound()).getPlayers()[currentPlayer-1].getPoints();

		newPoints = oldPoints;
		// obliczenia
		for (int i = 0; i < 3; i++) {
			ThrowValues throwValue = readValues(players[currentPlayer-1].getThrowFields()[i]);
			int points = throwValue.getValue() * throwValue.getMulitplier();
			newPoints = newPoints - points;
			difference += points;

			// todo wygrana //
			if(isWinner(newPoints,throwValue)){
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setTitle("Winner");
				alert.setHeaderText(players[currentPlayer-1].getName()+ " Won!!!"+"\nCongratulations!");
				alert.showAndWait();
				boardController.getMainStackPane().setDisable(true);
				return;
			}



			if(newPoints < 2){
				players[currentPlayer-1].setPoints(oldPoints);
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setTitle("Warning Dialog");
				alert.setHeaderText("Over Throw !!!");
				alert.showAndWait();
				for(int j=0;j<3;j++){
					players[currentPlayer-1].setThrowFieldsByIndex(j,new String());
				}
				return;
			}
			// sprawdzenie wygranej !!!
		}
		if(difference > 180){
			// todo wysw alert ze nie mozna 180
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Warning Dialog");
			alert.setHeaderText("U cant get more than 180 by 3 throws !!!");
			alert.showAndWait();
			for(int i=0;i<3;i++){
				players[currentPlayer-1].setThrowFieldsByIndex(i,new String());
			}
			return;
		}
		players[currentPlayer-1].setPoints(newPoints);

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


	public static ThrowValues readValues(String string){

		// jezeli wpisany int to zwraca value //
		int value;
		try{
			value = Integer.parseInt(string);
			if(value < 0)
				return new ThrowValues(0,1);
			else
				return new ThrowValues(value,1);
		}catch (Exception e){
		}

		// usuwanie znakow //
		String stringValue = string.replaceAll("[^0-9]","");
		String multiplierString = string.replaceAll("[^a-zA-Z]","");

		// jezeli stringValue ma cyfry to parsuje // jezeli nie zwraca 0
		if(!stringValue.equals("")){
			value = Integer.parseInt(stringValue);
		}
		else
			return new ThrowValues(0,0);

		// wyciaga multiplayer // jezeli nie zwraca 0
		int multiplier;
		if(multiplierString.equalsIgnoreCase("single"))
			multiplier=1;
		else if(multiplierString.equalsIgnoreCase("double"))
			multiplier=2;
		else if(multiplierString.equalsIgnoreCase("triple"))
			multiplier=3;
		else
			return new ThrowValues(0,0);

		return new ThrowValues(value,multiplier);
	}

    public static int getStartingPoints() {
        return startingPoints;
    }


	///////////////////////////////////////
	// PRIVATE METHODS				   	//
	///////////////////////////////////////

}
