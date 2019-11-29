package com.rmaj91.repository;

import com.rmaj91.domain.Playable;

import java.util.LinkedList;
import java.util.List;

public class GamesRepository {
	
	private List<Playable> gamesList = new LinkedList<>();
	Playable currentRound;

	public Playable getGame(int index){
		return gamesList.get(index);
	}
	public Playable getfirstRound(){
		return gamesList.get(0);
	}

	public  Playable getCurrentRound(){ return currentRound;}

	public void createNew(Playable game) {
		this.gamesList.clear();
		gamesList.add(game);
		currentRound = game;
		System.out.println("Game created, index: 0");
	}

	public Playable getForwardRound(int index){

		Playable clone = gamesList.get(index).cloneGame();
		currentRound = clone;

		if(gamesList.size() < index+2) {
			gamesList.add(clone);
			System.out.println("Game added, index: "+(index+1));
			// lub return clone
			return gamesList.get(index+1);
		}
		else{
			gamesList.set(index,clone);
			System.out.println("Game setted, index: "+(index+1));
			// lub return clone
			return gamesList.get(index+1);
		}
	}


// jak istnieje to nie robic i orzesuwac po istniejacyh do tylu


}
