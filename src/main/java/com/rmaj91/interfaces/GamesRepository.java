package com.rmaj91.interfaces;

/**
 * The GamesRepository interface provides methods for GamesRepositoryImpl
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

    int getNumberOfRound(Playable round);

    Playable getPreviousRound();

    Playable getZeroRound();
}
