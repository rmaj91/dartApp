package com.rmaj91.domain;

import java.util.ArrayList;
import java.util.List;

public class Game01 implements Playable {
	
	// poczatkowe
	private static int rounds;
	private static int subrounds;
	private static int playersQuantity;
	private static boolean doubleOut;
	// zmienne
	private int currentSubround=1;
	List<Player> playerList = new ArrayList<>();

	public static void setRounds(int rounds) {
		Game01.rounds = rounds;
		Game01.subrounds = Game01.rounds*Game01.playersQuantity;
	}

	public static void setPlayersQuantity(int playersQuantity) {
		Game01.playersQuantity = playersQuantity;
		Game01.subrounds = Game01.rounds*Game01.playersQuantity;
	}

	public static void setDoubleOut(boolean doubleOut) {
		Game01.doubleOut = doubleOut;
	}

	public static class Player{

		private String name;
		private int points;
		private String[] fields = new String[3];

		public Player(String name,int points) {
			this.name = name;
			this.points = points;
		}
	}



	public void addPlayer(Player player){
		playerList.add(player);
	}


}
