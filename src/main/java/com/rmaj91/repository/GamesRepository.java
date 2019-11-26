package com.rmaj91.repository;

import com.rmaj91.domain.Playable;

import java.util.LinkedList;
import java.util.List;

public class GamesRepository {
	
	private List<Playable> roundsList;
	
	public void addGameRound() {
		
	}
	

	public void createNew(Playable game) {
		roundsList = new LinkedList<>();
		roundsList.add(game);
	}
	public void deleteAll() {
		roundsList = null;
	}

}
