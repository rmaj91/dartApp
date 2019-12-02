package com.rmaj91.interfaces;


import com.rmaj91.domain.Player01;
import javafx.scene.input.MouseEvent;

public interface Playable {

	void next();
	void back();
    void throwDart(MouseEvent event);
    void display();
    void setCurrentThrow(int throwNumber);
    Playable cloneRound();
    void saveThrowFields();
    void calculatePoints();
    int getCurrentPlayer();
    public Player01[] getPlayer01s();

}
