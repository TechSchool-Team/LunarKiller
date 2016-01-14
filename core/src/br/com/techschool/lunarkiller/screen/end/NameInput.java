package br.com.techschool.lunarkiller.screen.end;

import com.badlogic.gdx.Input.TextInputListener;

/*
 * Reads player's name input for the hall of fame.
 */
public class NameInput implements TextInputListener {

    // Text entered by the player
    public String name;

    // Maximum length for a name
    public static final int MAX_LENGTH = 8;

    /*
     * Initializes stored name as null.
     */
    public NameInput() {
        this.name = null;
    }

    @Override
    public void input(String text) {
        System.out.println(text);
        if (text.length() > MAX_LENGTH) {
            text = text.substring(0, MAX_LENGTH);
        }

        this.name = (text == "" ? "Hervog" : text);
    }

    @Override
    public void canceled() {
        this.name = "Hervog";
    }
}
