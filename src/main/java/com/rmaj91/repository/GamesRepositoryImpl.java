package com.rmaj91.repository;

import com.rmaj91.interfaces.GamesRepository;
import com.rmaj91.interfaces.Playable;

import java.util.LinkedList;
import java.util.List;

public class GamesRepositoryImpl implements GamesRepository {
	
	private List<Playable> gamesList;

	public GamesRepositoryImpl() {
		gamesList = new LinkedList<>();
	}


	@Override
	public void pushRound(Playable round) {
		gamesList.add(round);
	}

	@Override
	public Playable getCurrentRound() {
		return gamesList.get(gamesList.size()-1);
	}

	@Override
	public boolean pullRound() {
		if(gamesList.size()>1){
			Playable round = gamesList.get(gamesList.size()-1);
			gamesList.remove(round);
			return true;
		}
		else
			return false;
	}

	@Override
	public void createNewGame(Playable round) {
		gamesList = new LinkedList<>();
		gamesList.add(round);
	}

	@Override
	public boolean saveGame() {
		return false;
	}

	@Override
	public boolean loadGame() {
		return false;
	}

	@Override
	public int getIndexOfRound(Playable round) {
		return gamesList.indexOf(round);
	}
}
