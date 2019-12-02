package com.rmaj91.interfaces;

/**
 * The GamesRepository interface provides methods for GamesRepository
 */
public interface GamesRepository {
    void pushRound(Playable round);
    Playable getCurrentRound();
    boolean pullRound();
    void createNewGame(Playable round);
    boolean saveGame();
    boolean loadGame();
    void clear();
    boolean isEmpty();
    int getIndexOfRound(Playable round);
    Playable getPreviousRound();
}
