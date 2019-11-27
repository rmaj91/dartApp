package com.rmaj91.domain;

import com.rmaj91.controller.BoardController;
import com.rmaj91.controller.Game01Controller;

import java.util.ArrayList;
import java.util.List;

public class Game01 implements Playable {


	// static
	private static Game01Controller game01Controller;
	private static BoardController boardController;
	private static int rounds;
	private static int playersQuantity;
	private static boolean doubleOut;
	// variables
	private int currentRound=1;
	private int currentPlayer=1;
	List<Player> playerList = new ArrayList<>();

	public static void setBoardController(BoardController boardController) {
		Game01.boardController = boardController;
	}

	public static void setGame01Controller(Game01Controller game01Controller) {
		Game01.game01Controller = game01Controller;
	}

	public int getCurrentRound() {
		return currentRound;
	}

	public void setCurrentRound(int currentRound) {
		this.currentRound = currentRound;
	}

	public int getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(int currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public static void setRounds(int rounds) {
		Game01.rounds = rounds;
	}

	public static void setPlayersQuantity(int playersQuantity) {
		Game01.playersQuantity = playersQuantity;
	}

	public static void setDoubleOut(boolean doubleOut) {
		Game01.doubleOut = doubleOut;
	}

	public static class Player{

		private String name;
		private int points;
		private String[] fields = new String[3];
		private int average;
		private int currentThrow = 1;

		public Player(String name,int points) {
			this.name = name;
			this.points = points;
		}


	}



	public void addPlayer(Player player){
		playerList.add(player);
	}


	///////////////////////////////////////////
	////////////////////////////////////////////
	// METHODS - PLAYABLE //////////////////////


	@Override
	public void nextRound() {

	}

	@Override
	public void previousRound() {

	}

	@Override
	public void calculatePoints() {

	}

	@Override
	public void initPlayers() {

	}

	// TODO dodac init do players

	@Override
	public void displayGameRound(int currentPlayer) {
		// double out
		if(doubleOut)
			boardController.getDoubleOut().setVisible(true);
		else
			boardController.getDoubleOut().setVisible(false);
		// Rounds
		boardController.getRoundsLabel().setText("Round: "+currentRound+"/"+Game01.rounds);
		// TextFields
		boardController.getThrowField1().setText(playerList.get(currentPlayer-1).fields[0]);
		boardController.getThrowField2().setText(playerList.get(currentPlayer-1).fields[1]);
		boardController.getThrowField3().setText(playerList.get(currentPlayer-1).fields[2]);
		// Player name
		boardController.getPlayerNameLabel().setText(playerList.get(currentPlayer-1).name);
		boardController.getPlayerPointsLabel().setText(String.valueOf(playerList.get(currentPlayer-1).points));
		//highlight player
		//boardController.get
		// player points
	}

	@Override
	public Playable clone() {
		return null;
	}
}
