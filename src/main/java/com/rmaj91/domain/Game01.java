package com.rmaj91.domain;

import com.rmaj91.Main;
import com.rmaj91.controller.BoardController;
import com.rmaj91.controller.Game01Controller;
import com.rmaj91.utility.Utility;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;


import java.util.Arrays;


public class Game01 implements Playable {

	// dependencies //
	private static BoardController boardController;
	private static Game01Controller game01Controller;
	// static variables //
	private static int subrounds;
	private static int playersQuantity;
	private static boolean doubleOut;
	private static String[] playersNames;




	// variables //
	private int[] playerPoints;
	private int[] playersAverages;
	private String[] throwFieldsContent = new String[3];
	private int currentThrow;
	private int currentSubround;

	// Injection setters //
	public static void setBoardController(BoardController boardController) {
		Game01.boardController = boardController;
	}
	public static void setGame01Controller(Game01Controller game01Controller) {
		Game01.game01Controller = game01Controller;
	}

	// SETTERS/GETTERS //
	public int getCurrentSubround() {
		return currentSubround;
	}
	public void setThrowFieldsContent(String[] throwFieldsContent) {
		this.throwFieldsContent = throwFieldsContent;
	}
	public void setPlayersAverages(int[] playersAverages) {
		this.playersAverages = playersAverages;
	}
	public void setPlayerPoints(int[] playerPoints) {
		this.playerPoints = playerPoints;
	}
	public void setCurrentThrow(int currentThrow) {
		this.currentThrow = currentThrow;
	}
	public int getCurrentThrow() {
		return this.currentThrow;
	}

	public static void setPlayersNames(String[] playersNames) {
		Game01.playersNames = playersNames;
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
	public void setCurrentSubround(int currentSubround) {
		this.currentSubround = currentSubround;
	}
	public static int getPlayersQuantity() {
		return playersQuantity;
	}


	public Game01(){
		Arrays.fill(throwFieldsContent,"");
		currentThrow = 1;
		currentSubround = 1;
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
		newGame.setCurrentSubround(this.currentSubround+1);

		// deep copy of averages //
		int[] newPlayersAverages= new int[playersAverages.length];
		for(int i = 0; i< playersAverages.length; i++){
			// TODO sprawdzic jak z glebokimi kopiami //
			newPlayersAverages[i] = playersAverages[i];
		}
		newGame.setPlayersAverages(newPlayersAverages);

		// depp copy of players points //
		int[] newPlayersPoints= new int[playerPoints.length];
		for(int i=0;i<playerPoints.length;i++){
			// TODO sprawdzic jak z glebokimi kopiami //
			newPlayersPoints[i] = playerPoints[i];
		}
		newGame.setPlayerPoints(newPlayersPoints);

		// deep copy of throwfields //
		String[] newFieldsContent = new String[3];
		for(int i=0;i<3;i++){
			newFieldsContent[i] = new String(throwFieldsContent[i]);
		}
		newGame.setThrowFieldsContent(newFieldsContent);

		return newGame;
	}

	@Override
	public void next() {


		System.out.println("Next Button clicked!");
		if(currentSubround <= Game01.subrounds){
			// TODO do poprawy ->ustawia rzut zeby po cofnieciu w dobre miejsce wskoczyl)
			currentThrow = 1;

			Main.gamesRepositoty.getForwardRound(currentSubround-1).display();

			boardController.getThrowField1().requestFocus();


		}

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
				System.out.print(key+"\t");
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
		boardController.getRoundsLabel().setText("Round: "+ (1+currentSubround/playersQuantity) +"/"+Game01.subrounds /Game01.playersQuantity);

		// TextFields //
		boardController.getThrowField1().setText(throwFieldsContent[0]);
		boardController.getThrowField2().setText(throwFieldsContent[1]);
		boardController.getThrowField3().setText(throwFieldsContent[2]);

		// Big Player name,points,average //
		boardController.getPlayerNameLabel().setText(playersNames[(currentSubround-1)%playersQuantity]);
		boardController.getPlayerPointsLabel().setText(String.valueOf(playerPoints[(currentSubround-1)%playersQuantity]));
		boardController.getAverageLabel().setText("Average: "+ playersAverages[(currentSubround-1)%playersQuantity]);

		// turn off highlighting in previous subround
		if(currentSubround >1){
			Node node2 = boardController.getGame01PlayersTable().getChildren().get((currentSubround-2)%playersQuantity);;
			((VBox)node2).getChildren().get(0).getStyleClass().removeIf(style -> style.equals("-fx-background-color: #0388fc;"));
		}

		//highlight player
		Node node = boardController.getGame01PlayersTable().getChildren().get((currentSubround-1)%playersQuantity);
		((VBox)node).getChildren().get(0).setStyle("-fx-background-color: #0388fc;");






	}

	///////////////////////////////////////
	// PRIVATE METHODS
	///////////////////////////////////////

}
