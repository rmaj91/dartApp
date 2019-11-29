package com.rmaj91.domain;

import com.rmaj91.controller.BoardController;
import com.rmaj91.controller.Game01Controller;
import com.rmaj91.repository.GameFactory;
import com.rmaj91.repository.GamesRepositoryImpl;
import com.rmaj91.utility.Utility;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;


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

		if(gamesRepositoryImpl.getIndexOfRound(this)+1 < Game01.roundsMaxNumber || currentPlayer != playersQuantity){
			if(currentPlayer  == Game01.playersQuantity){
				((Game01)gamesRepositoryImpl.getCurrentRound()).getPlayers()[currentPlayer-1].setCurrentThrow(1);
				gamesRepositoryImpl.pushRound(this.cloneRound());
			}
			else{
				((Game01)gamesRepositoryImpl.getCurrentRound()).getPlayers()[currentPlayer-1].setCurrentThrow(1);
				currentPlayer++;
			}
			gamesRepositoryImpl.getCurrentRound().display();
		}
		else
			return;
	}

	@Override
	public void back() {

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
			System.out.print("Rzut nr: " + currentThrow + "Pressed: x=" + xCartesian + "  y=" + yCartesian + "\t");
			System.out.print(key + "\t");
			System.out.println(Utility.readValues(key));
			//////////////////////////////////////////////////

			if (currentThrow == 1) {
				boardController.getThrowField1().setText(key);
				boardController.getThrowField2().requestFocus();
			} else if (currentThrow == 2) {
				boardController.getThrowField2().setText(key);
				boardController.getThrowField3().requestFocus();
			} else if (currentThrow == 3)
				boardController.getThrowField3().setText(key);

			players[currentPlayer - 1].setCurrentThrow(currentThrow+1);

		}

	}


	@Override
	public void display() {
		boardController.getThrowField1().requestFocus();
		// Rounds //
		int currentRound = gamesRepositoryImpl.getIndexOfRound(this)+1;
		boardController.getRoundsLabel().setText("Round: "+ currentRound +"/"+Game01.roundsMaxNumber);

		// TextFields //
		boardController.getThrowField1().setText(players[currentPlayer-1].getThrowFields()[0]);
		boardController.getThrowField2().setText(players[currentPlayer-1].getThrowFields()[1]);
		boardController.getThrowField3().setText(players[currentPlayer-1].getThrowFields()[2]);

		// Big Player name,points,average //
		boardController.getPlayerNameLabel().setText(players[currentPlayer-1].getName());
//		for (Player player : players) {
//			System.out.println(player.getName());
//		}
		boardController.getPlayerPointsLabel().setText(String.valueOf(players[currentPlayer-1].getPoints()));
		boardController.getAverageLabel().setText("Average: "+ players[currentPlayer-1].getAverage());

		// reset player name highlight //
		for(int i=0;i<Game01.playersQuantity;i++){
			Node node = boardController.getGame01PlayersTable().getChildren().get(i);
			((VBox)node).getChildren().get(0).setStyle(null);
		}

		// highlight player name //
		Node node = boardController.getGame01PlayersTable().getChildren().get(currentPlayer-1);
		((VBox)node).getChildren().get(0).setStyle("-fx-background-color: #0388fc;");

	}

	@Override
	public void setCurrentThrow(int throwNumber) {
		players[currentPlayer-1].setCurrentThrow(throwNumber);
	}

	//	@Override
//	public void throwDart(MouseEvent event) {
//		if(currentThrow == 4)
//			return;
//		double x = event.getX()-248;
//		double y = -(event.getY()-248.5);
//		System.out.print("Pressed: x="+x + "  y=" +y+"\t");
//		// tODO do main
//		Filters filters = new Filters();
//		int radiusIndex = Utility.getRadiusIndex(x,y);
//		int angleIndex = Utility.getAngleIndex(x,y);
//		String key="";
//		for (Filters.IndexMapper indexMapper : filters.getIndexMapperList()) {
//			if(indexMapper.hasKey(radiusIndex,angleIndex)){
//				key=indexMapper.getKey();
//				System.out.print(key+"\t");
//				break;
//			}
//		}
//
//
//		if(currentThrow == 1){
//			boardController.getThrowField1().setText(key);
//			boardController.getThrowField2().requestFocus();
//		}
//
//		else if(currentThrow == 2){
//			boardController.getThrowField2().setText(key);
//			boardController.getThrowField3().requestFocus();
//		}
//
//		else if(currentThrow == 3)
//			boardController.getThrowField3().setText(key);
//		currentThrow++;
//
//		System.out.println(Utility.readValues(key));
//
//	}


//	@Override
//	public void display() {
//		// Rounds //
//		boardController.getRoundsLabel().setText("Round: "+ (1+currentSubround/playersQuantity) +"/"+Game01.subrounds /Game01.playersQuantity);
//
//		// TextFields //
//		boardController.getThrowField1().setText(throwFieldsContent[0]);
//		boardController.getThrowField2().setText(throwFieldsContent[1]);
//		boardController.getThrowField3().setText(throwFieldsContent[2]);
//
//		// Big Player name,points,average //
//		boardController.getPlayerNameLabel().setText(playersNames[(currentSubround-1)%playersQuantity]);
//		boardController.getPlayerPointsLabel().setText(String.valueOf(playerPoints[(currentSubround-1)%playersQuantity]));
//		boardController.getAverageLabel().setText("Average: "+ playersAverages[(currentSubround-1)%playersQuantity]);
//
//		// turn off highlighting in previous subround
//		if(currentSubround >1){
//			Node node2 = boardController.getGame01PlayersTable().getChildren().get((currentSubround-2)%playersQuantity);;
//			((VBox)node2).getChildren().get(0).getStyleClass().removeIf(style -> style.equals("-fx-background-color: #0388fc;"));
//		}
//
//		//highlight player
//		Node node = boardController.getGame01PlayersTable().getChildren().get((currentSubround-1)%playersQuantity);
//		((VBox)node).getChildren().get(0).setStyle("-fx-background-color: #0388fc;");
//
//
//boardController.getThrowField1().requestFocus();
//
//
//
//	}
//
//	///////////////////////////////////////
//	// PRIVATE METHODS
//	///////////////////////////////////////

}
