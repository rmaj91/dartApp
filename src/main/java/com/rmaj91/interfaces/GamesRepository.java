package com.rmaj91.interfaces;

import com.rmaj91.interfaces.Playable;

public interface GamesRepository {
    void pushRound(Playable round);
    Playable getCurrentRound();
    boolean pullRound();
    void createNewGame(Playable round);
    boolean saveGame();
    boolean loadGame();
    int getIndexOfRound(Playable round);
}
