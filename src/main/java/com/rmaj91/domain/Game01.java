package com.rmaj91.domain;

import com.rmaj91.controller.BoardController;
import com.rmaj91.controller.Game01Controller;
import javafx.scene.input.MouseEvent;


import java.util.ArrayList;
import java.util.Arrays;
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
			Arrays.fill(fields,new String());
		}


	}

	public void addPlayer(Player player){
		playerList.add(player);
	}

	///////////////////////////////////////////
	////////////////////////////////////////////
	// METHODS - PLAYABLE //////////////////////


	@Override
	public void next() {
		System.out.println("Next Button clicked!");
	}

	@Override
	public void back() {
		System.out.println("Back Button clicked!");
	}

	@Override
	public void throwDart(MouseEvent event) {
		double x = event.getX()-248;
		double y = event.getY()-248.5;
		System.out.print("Pressed: x="+x + "  y=" +y+"\t");

		// TODO przeniesc do main
		//System.out.println(getRadiusIndex(x, y));
		if(getRadiusIndex(x,y)==6)
			System.out.println("Lipny rzut: 0!");
		else if(getRadiusIndex(x,y)==0)
			System.out.println("Rzuciles: 2x25!");
		else if(getRadiusIndex(x,y)==1)
			System.out.println("Rzuciles: 1x25!");
		else
			System.out.println("Rzuciles cos z, radiusIndex= "+getRadiusIndex(x, y)+"  i z angleIndex= "+getAngleIndex(x,y));
	}

	public int getRadiusIndex(double x,double y){
		// tODO
		Filters filters = new Filters();
		for (Filters.RadiusScope radiusScope : filters.getRadiusList()) {
			if(radiusScope.isInRange(getRadius(x,y)))
				return filters.getRadiusList().indexOf(radiusScope);
		}
			return 6;
	}
	// TODO statyczne
	public int getAngleIndex(double x,double y){
		// TODO w main
		Filters filters = new Filters();
		for (Filters.AngleScope angleScope : filters.getAngleList()) {
			if(angleScope.isInRange(getAngle(x,y)))
				return filters.getAngleList().indexOf(angleScope);
		}
		return 20;
	}
	// TODO static zrobic
	public int getRadius(double x, double y){
		return (int)Math.sqrt(x*x+y*y);
	}
	// TODO static zrobic
	public double getAngle(double x, double y){
		double angle=Math.atan2(y,x);
		angle = Math.toDegrees(angle);
		if(angle<0)
			angle+=360;
		return angle;
	}

	// TODO dodac init do players

	public void display(int current) {
		// double out
		if(doubleOut)
			boardController.getDoubleOut().setVisible(true);
		else
			boardController.getDoubleOut().setVisible(false);
		// Rounds
		boardController.getRoundsLabel().setText("Round: "+currentRound+"/"+Game01.rounds);
		// TextFields
		boardController.getThrowField1().setText(playerList.get(current-1).fields[0]);
		boardController.getThrowField2().setText(playerList.get(current-1).fields[1]);
		boardController.getThrowField3().setText(playerList.get(current-1).fields[2]);
		// Player name
		boardController.getPlayerNameLabel().setText(playerList.get(current-1).name);
		boardController.getPlayerPointsLabel().setText(String.valueOf(playerList.get(current-1).points));
		//highlight player
		//boardController.get
		// player points
	}

}
