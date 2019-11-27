package com.rmaj91.domain;

public interface Playable {
	void nextRound();
	void previousRound();
	void calculatePoints();
    void initPlayers();


    void displayGameRound(int currentPlayer);

    Playable clone();
}
