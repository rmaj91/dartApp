package com.rmaj91.repository;

import com.rmaj91.domain.Cricket;
import com.rmaj91.domain.Game01;
import com.rmaj91.domain.MasterCricket;
import com.rmaj91.interfaces.GamesInterface;

/**
 * Implements Factory design pattern.
 */
public class GameFactory {

    private GameFactory() {
    }

    public static GamesInterface getGame(String gameName) {
        if (gameName == "'01 Game")
            return new Game01();
        else if (gameName == "Cricket")
            return new Cricket();
        else if (gameName == "Master Cricket")
            return new MasterCricket();
        else
            return null;
    }
}
