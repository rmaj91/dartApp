package com.rmaj91.interfaces;


import javafx.scene.input.MouseEvent;

public interface Playable {

	void next();
	void back();
    void throwDart(MouseEvent event);
    void display();
    void setCurrentThrow(int throwNumber);
    Playable cloneRound();
//    boolean ifWin();

}
