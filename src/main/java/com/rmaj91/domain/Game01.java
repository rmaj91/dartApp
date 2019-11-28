package com.rmaj91.domain;

import com.rmaj91.controller.BoardController;
import com.rmaj91.controller.Game01Controller;
import com.rmaj91.utility.Utility;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class Game01 implements Playable {

	// dependencies //
	private static BoardController boardController;
	private static Game01Controller game01Controller;
	// static variables //
	private static int subrounds;
	private static int playersQuantity;
	private static boolean doubleOut;

	// variables //
	private int[] playerPoints;
	private String[] playersNames;
	private String[] throwFieldsContent = new String[3];
	private int average=0;
	private int currentThrow=1;
	private int currentSubround =1;

	// Injection setters //
	public static void setBoardController(BoardController boardController) {
		Game01.boardController = boardController;
	}
	public static void setGame01Controller(Game01Controller game01Controller) {
		Game01.game01Controller = game01Controller;
	}
	// SETTERS/GETTERS //
	public static int getPlayersQuantity() {
		return playersQuantity;
	}
	public void setCurrentThrow(int currentThrow) {
		this.currentThrow = currentThrow;
	}
	public int getCurrentThrow() {
		return this.currentThrow;
	}
	public int getCurrentSubround() {
		return currentSubround;
	}
	public void setCurrentSubround(int currentSubround) {
		this.currentSubround = currentSubround;
	}
	public void setPlayersNames(String[] playersNames) {
		this.playersNames = playersNames;
	}
	public void setThrowFieldsContent(String[] throwFieldsContent) {
		this.throwFieldsContent = throwFieldsContent;
	}
	public void setAverage(int average) {
		this.average = average;
	}

	public void setPlayerPoints(int[] playerPoints) {
		this.playerPoints = playerPoints;
	}

	public static void setSubrounds(int subrounds) {
		Game01.subrounds = subrounds;
	}
	public static void setPlayersQuantity(int playersQuantity) {
		Game01.playersQuantity = playersQuantity;
	}
	public static void setDoubleOut(boolean doubleOut) {
		Game01.doubleOut = doubleOut;
	}


	public Game01(){
		Arrays.fill(throwFieldsContent,"");
	}

	///////////////////////////
	// METHODS FROM PLAYABLE //
	///////////////////////////

	@Override
	public void initGUI() {

	}

	@Override
	public Game01 cloneGame(){
		Game01 newGame = new Game01();
//		newGame.setCurrentPlayer(this.currentPlayer);
//		newGame.setCurrentThrow(this.currentThrow);
//		newGame.setCurrentSubround(this.currentSubround);
//
//		for (Player player : playerList) {
//			Player newPlayer = player.clonePlayer();
//			newGame.getPlayerList().add(newPlayer);
//		}
//
		return newGame;
	}

	@Override
	public void next() {
//		System.out.println("Next Button clicked!");
//		if(currentRound < Game01.rounds){
//			currentThrow = 1;
//			if(currentPlayer == playersQuantity){
//				currentPlayer = 1;
//				currentRound ++;
//			}
//			Main.gamesRepositoty.addRound(this.cloneGame());
//			display();
//			boardController.getThrowField1().requestFocus();
//		}

	}

	@Override
	public void back() {
//		System.out.println("Back Button clicked!");
//		this.currentThrow = 1;
	}

	@Override
	public void throwDart(MouseEvent event) {
		if(currentThrow == 4)
			return;
		double x = event.getX()-248;
		double y = -(event.getY()-248.5);
		System.out.print("Pressed: x="+x + "  y=" +y+"\t");
		// tODO do main
		Filters filters = new Filters();
		int radiusIndex = Utility.getRadiusIndex(x,y);
		int angleIndex = Utility.getAngleIndex(x,y);
		String key="";
		for (Filters.IndexMapper indexMapper : filters.getIndexMapperList()) {
			if(indexMapper.hasKey(radiusIndex,angleIndex)){
				key=indexMapper.getKey();
				System.out.println(key);
				break;
			}
		}


		if(currentThrow == 1){
			boardController.getThrowField1().setText(key);
			boardController.getThrowField2().requestFocus();
		}

		else if(currentThrow == 2){
			boardController.getThrowField2().setText(key);
			boardController.getThrowField3().requestFocus();
		}

		else if(currentThrow == 3)
			boardController.getThrowField3().setText(key);
		currentThrow++;

		System.out.println(Utility.readValues(key));

	}


	@Override
	public void display() {
		// Rounds //
		boardController.getRoundsLabel().setText("Round: "+ (1+currentSubround/playersQuantity) +"/"+Game01.subrounds/Game01.playersQuantity);

		// TextFields //
		boardController.getThrowField1().setText(throwFieldsContent[0]);
		boardController.getThrowField2().setText(throwFieldsContent[1]);
		boardController.getThrowField3().setText(throwFieldsContent[2]);

		// Big Player name,points,average //
		boardController.getPlayerNameLabel().setText(playersNames[currentSubround%playersQuantity-1]);
		boardController.getPlayerPointsLabel().setText(String.valueOf(playerPoints[currentSubround%playersQuantity-1]));
		boardController.getAverageLabel().setText("Average: "+ average);

		//highlight player
		Node node = boardController.getGame01PlayersTable().getChildren().get(currentSubround%playersQuantity-1);
		((VBox)node).getChildren().get(0).setStyle("-fx-background-color: #0388fc;");

	}

	///////////////////////////////////////
	// PRIVATE METHODS
	///////////////////////////////////////

}
