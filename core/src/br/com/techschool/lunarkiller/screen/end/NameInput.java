package br.com.techschool.lunarkiller.screen.end;

import com.badlogic.gdx.Input.TextInputListener;

/*
 * Reads player's name input for the hall of fame.
 */
public class NameInput implements TextInputListener {

    // Text entered by the player
    public String name;

    /*
     * Initializes stored name as null.
     */
    public NameInput() {
        this.name = null;
    }

    @Override
    public void input(String text) {
        this.name = text;
    }

    @Override
    public void canceled() {
        this.name = "Hervog";
    }
}
