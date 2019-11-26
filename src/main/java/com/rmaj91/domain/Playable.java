package com.rmaj91.domain;

public interface Playable {
	void nextSubRound();
	void previousSubRound();
	void calculatePoints();
	void initUI();
	Playable clone();
}
