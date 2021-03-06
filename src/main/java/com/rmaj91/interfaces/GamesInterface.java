package com.rmaj91.interfaces;

import com.rmaj91.domain.Player01;
import javafx.scene.input.MouseEvent;

/**
 * The Playable interface provides methods for dart games
 */

public interface GamesInterface {

    /**
     * The next() method moves game into next player/round (it depends on which player actually have turn).
     */
    void NextButton();

    /**
     * The back() method moves game backwards to the previous player/round (it depends on which player actually have turn)
     */
    void BackButton();

    /**
     * The throwDart() method is invoke when player clicks on a dart board. It fills the textField for the current throw
     * with the field signature.
     *
     * @param event This is event from mouseClick action is used for reading the coordinates of cursor.
     */
    void throwDart(MouseEvent event);

    /**
     * The displayRound() method displays state of the current round and player on javafx view.
     */
    void displayRoundState();

    /**
     * The setCurrentThrow() method sets the current number of throw for current player.
     *
     * @param throwNumber This is the number of a throw
     */
    void setCurrentThrowTo(int throwNumber);


    /**
     * The saveThrowFields() method saves text from throwTextFields.
     */
    void saveThrowFields();

    /**
     * This method calculates player/players points
     */
    void calculatePoints();


    /**
     * Inits board view and display current round, used after loading game
     */
    void setBoardViewVisible();


}
