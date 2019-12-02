package com.rmaj91.interfaces;

import com.rmaj91.domain.Player;
import javafx.scene.input.MouseEvent;

/**
 * The Playable interface provides methods for dart games
 */

public interface Playable {

    /**
     * The next() method moves game into next player/round (it depends on which player actually have turn)
     */
	void next();

    /**
     * The back() method moves game backwards to the previous player/round (it depends on which player actually have turn)
     */
	void back();

    /**
     * The throwDart() method is invoke when player clicks on a dart board. It fills the textField for the current throw
     * with the field signature.
     * @param event This is event from mouseClick action is used for reading the coordinates of cursor.
     */
    void throwDart(MouseEvent event);

    /**
     * The displayRound() method displays state of the current round and player.
     */
    void displayRound();

    /**
     * The setCurrentThrow() method is invoking while clicking on a throwTextFields. Method sets the current number of throw
     * for current player
     * @param throwNumber This is the number of a throw
     */
    void setCurrentThrow(int throwNumber);

    /**
     * The cloneRound() method clones the current round.
     * @return Method returns cloned current round
     */
    Playable cloneRound();

    /**
     * The saveThrowFields() method saves text from throwTextFields.
     */
    void saveThrowFields();

    /**
     * This method calculates players points
     */
    void calculatePoints();

    /**
     * This method returns number of current player.
     */
    int getCurrentPlayer();

    /**
     * This method returns players array of current round
     */
    public Player[] getPlayer();

}
