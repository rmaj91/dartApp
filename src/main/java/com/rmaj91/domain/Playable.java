package com.rmaj91.domain;


import javafx.scene.input.MouseEvent;

public interface Playable {
	void next();
	void back();
    void throwDart(MouseEvent event);
    void display(int index);
}
