package com.rmaj91.interfaces;

/**
 * The GamesRepository interface provides methods for GamesRepositoryImpl
 */
public interface GamesRepository {
    void pushRound(GamesInterface round);

    GamesInterface getCurrentRound();

    boolean pullRound();

    void createNewGame(GamesInterface round);

    boolean saveGame();

    boolean loadGame();

    void clear();

    boolean isEmpty();

    int getNumberOfRound(GamesInterface round);

    GamesInterface getPreviousRound();

    GamesInterface getZeroRound();
}
