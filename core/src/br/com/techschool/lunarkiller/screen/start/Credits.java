package br.com.techschool.lunarkiller.screen.start;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import br.com.techschool.lunarkiller.util.Constant;

/*
 * Rolls credits on the start screen.
 */
public class Credits {

    // Name of text file containing credits
    private static final String CREDITS_FILE = "Credits.txt";

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

    // If true, then the whole credits have been shown
    private boolean done;

    /*
     * Initializes a Credits object's attributes.
     */
    public Credits(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;

        font = new BitmapFont(Gdx.files.internal("fonts/cyber.fnt"));
        glyphLayout = new GlyphLayout();
        readFile();

        // Text starts below screen
        height = -font.getLineHeight();

        done = false;
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
        spriteBatch.begin();

        String[] lines = text.split("\\n");
        float lineHeight = height;

        // Draws each line while calculating y coordinates
        for (String line : lines) {
            drawCenteredLine(line, lineHeight);
            lineHeight -= font.getLineHeight();
        }

        // Checks if whole credits are above screen height
        if (lineHeight > Constant.GAME_HEIGHT) {
            done = true;
        }

        spriteBatch.end();
    }

    /*
     * Returns true if the credits scene has finished, or false otherwise.
     */
    public boolean isDone() {
        return done;
    }

    /*
     * Draws the specified line, centered according to the screen width.
     * 'y' indicates the line's position on the y axis.
     */
    private void drawCenteredLine(String line, float y) {
        glyphLayout.setText(font, line);
        float x = (Constant.GAME_WIDTH - glyphLayout.width)/2;
        font.draw(spriteBatch, glyphLayout, x, y);
    }

    /*
     * Reads credits from a file.
     */
    private void readFile() {
        // Get file in same directory as this class
        URL url = getClass().getResource(CREDITS_FILE);
        File file = new File(url.getPath());

        try {
            // Read entire credits file into String
            Scanner scanner = new Scanner(file);
            text = scanner.useDelimiter("\\Z").next();
            scanner.close();
        } catch (FileNotFoundException e) {
            // Use empty String if file not found
            System.out.println("ERROR: Credits file not found!");
            text = "";
        }
    }

    /*
     * Clears memory used by credits effect.
     */
    public void dispose() {
        font.dispose();
    }
}
