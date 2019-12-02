package com.rmaj91.domain;

import com.rmaj91.interfaces.Playable;
import javafx.scene.input.MouseEvent;

public class MasterCricket implements Playable {

    @Override
    public void next() {

    }

    @Override
    public void back() {

    }

    @Override
    public void throwDart(MouseEvent event) {

    }

    @Override
    public void displayRound() {

    }

    @Override
    public void setCurrentThrow(int throwNumber) {

    }

    @Override
    public Playable cloneRound() {
        return null;
    }

    @Override
    public void saveThrowFields() {

    }

    @Override
    public void calculatePoints() {

    }

    @Override
    public int getCurrentPlayer() {
        return 0;
    }

    @Override
    public Player[] getPlayer() {
        return new Player[0];
    }
}