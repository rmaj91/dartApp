package com.rmaj91.repository;

import com.rmaj91.domain.Playable;

public interface GamesRepository {
    void pushRound(Playable round);
    Playable getCurrentRound();
    boolean pullRound();
    void createNewGame(Playable round);
    boolean saveGame();
    boolean loadGame();
    int getIndexOfRound(Playable round);
}
