package br.com.techschool.lunarkiller.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/*
 * Rolls credits on the start screen.
 */
public class Credits {

    // Same main layer used on start screen
    private SpriteBatch spriteBatch;

    // Font used for text
    private BitmapFont font;

    // Used to center text on screen
    GlyphLayout glyphLayout;

    // Text used for the credits
    private String text;

    // Credits position on the y axis
    private float height;

    // Speed at which the credits roll
    private final float speed = 50.0f;

    /*
     * Initializes a Credits object's attributes.
     */
    public Credits(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;

        // TODO: Define a font!
        font = new BitmapFont(Gdx.files.internal("fonts/debug.fnt"));

        glyphLayout = new GlyphLayout();
        prepareText();

        // Text starts below screen
        height = -font.getLineHeight();
    }

    /*
     * Updates the credits position.
     */
    public void update(float delta) {
        // Moves top of credits text
        height += speed*delta;
    }

    /*
     * Draws credits on the screen.
     */
    public void draw(float delta) {
        // Darken screen by reducing layer's alpha
        spriteBatch.setColor(1.0f, 1.0f, 1.0f, 0.1f);

        spriteBatch.begin();
        String[] lines = text.split("\\n");
        float lineHeight = height;

        // Draws each line while calculating y coordinates
        for (String line : lines) {
            drawCenteredLine(line, lineHeight);
            lineHeight -= font.getLineHeight();
        }

        spriteBatch.end();
    }

    /*
     * Draws the specified line, centered according to the screen width.
     * 'y' indicates the line's position on the y axis.
     */
    private void drawCenteredLine(String line, float y) {
        glyphLayout.setText(font, line);
        float x = (Constant.GAME_WIDTH - glyphLayout.width)/2;
        // TODO: 'font.getData().setScale(2.0f)' resizes font (use with caution)!
        font.draw(spriteBatch, glyphLayout, x, y);
    }

    /*
     * DEBUG METHOD. Prepares the String that contains all credits text.
     */
    private void prepareText() {
        text = "LUNAR KILLER\n\n";

        text += "\nCOORDINATORS\n";
        text += "Francisco Isidro Massetto\n";
        text += "Yucif Kandratavicius\n";

        text += "\n3D MODELERS & PROGRAMMERS\n";
        text += "Caio Halbert\n";
        text += "Mauricio David\n";

        text += "\nPROGRAMMERS\n";
        text += "Kaique Queiroz\n";
        text += "Leonardo Macedo\n";
        text += "Marcelo Ferreira\n";

        text += "\nSPECIAL THANKS\n";
        text += "???\n";
    }

    /*
     * Clears memory used by credits effect.
     */
    public void dispose() {
        font.dispose();
    }
}
