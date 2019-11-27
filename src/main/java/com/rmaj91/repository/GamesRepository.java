package com.rmaj91.repository;

import com.rmaj91.domain.Playable;

import java.util.LinkedList;
import java.util.List;

public class GamesRepository {
	
	private List<Playable> gamesList = new LinkedList<>();

	public Playable getGameRound(int index){
		return gamesList.get(index);
	}

	public void createNew(Playable game) {
		this.gamesList.clear();
		gamesList.add(game);
	}


}
