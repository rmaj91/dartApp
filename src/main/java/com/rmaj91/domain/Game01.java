package com.rmaj91.domain;

import java.util.List;

public class Game01 implements Playable {
	
	// poczatkowe
	private static int subrounds;
	private static int playersQuantity;
	// zmienne
	private int currentSubround=1;
	
	List<Player> players;
	
	
	
	private class Player{
		
		private int points;
		private String name;
		private String[] fields = new String[3];
	}



	public static void setPlayersQuantity(int playersQuantity) {
		Game01.subrounds = Game01.subrounds/Game01.playersQuantity*playersQuantity;
		Game01.playersQuantity = playersQuantity;
	}
	
	



	public static int getSubrounds() {
		return subrounds;
	}





	public static void setSubrounds(int rounds) {
		Game01.subrounds = rounds*Game01.playersQuantity;
	}





	@Override
	public void nextSubRound() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void previousSubRound() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void calculatePoints() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void initUI() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public Playable clone() {
		// TODO Auto-generated method stub
		return null;
	}
}
