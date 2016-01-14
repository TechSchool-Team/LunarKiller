package br.com.techschool.lunarkiller.screen.end;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import br.com.techschool.lunarkiller.util.Constant;

/*
 * Reads, stores and updates players with highest achieved scores.
 */
public class HallFame {

    // Contains all phases that can occur on this screen
    private enum Phase {
        CHECK_SCORE, ASK_NAME, UPDATE_HALLFAME, SHOW_HALLFAME, END
    };

    // Player's score
    private int score;

    // Player's position on the hall of fame, with 0 as highest.
    // Is -1 if the player isn't in the hall of fame.
    private int playerPosition;

    // Centered title of the hall of fame
    private GlyphLayout title;

    // Text to be drawn on screen
    private GlyphLayout[] drawNames, drawScores;

    // Stores top players
    private LinkedList<Tag> topPlayers;

    // Main layer from end screen
    private SpriteBatch spriteBatch;

    // Font used for drawing top scores
    private BitmapFont font;

    // Controls what action is currently happening
    private Phase phase;

    // Reads player's name from input
    private NameInput nameInput;

    // Credits position on the y axis
    private float height;

    // Speed at which the text moves upwards
    private final float speed = 75.0f;

    // Number of players in ranking
    private static final int MAX_SIZE = 10;

    // File containing top players and their scores
    private static final String HALLFAME_FILE = "HallFame.txt";

    /*
     * Initializes variables and reads hall of fame from file.
     */
    public HallFame(int score, SpriteBatch spriteBatch) {
        this.score = score;
        this.spriteBatch = spriteBatch;

        // Read font
        // TODO: Define a font!
        font = new BitmapFont(Gdx.files.internal("fonts/debug.fnt"));

        // Create glyph
        title = new GlyphLayout();
        title.setText(font, "HALL OF FAME");

        // Initialize variables
        topPlayers = new LinkedList<Tag>();
        phase = Phase.CHECK_SCORE;
        nameInput = new NameInput();

        readFile();

        // Text starts below screen
        height = -font.getLineHeight();
    }

    /*
     * Manages hall of fame phases.
     */
    public void update(float delta) {
        switch(phase) {
            case CHECK_SCORE:
                playerPosition = checkNewRecord(score);
                if (playerPosition >= 0) {
                    // New hall of fame member!
                    phase = Phase.ASK_NAME;
                }
                else {
                    // Proceed normally to show hall of fame
                    prepareDrawText();
                    phase = Phase.SHOW_HALLFAME;
                }
            break;

            case ASK_NAME:
                // Reads player name from input
                Gdx.input.getTextInput(nameInput,
                                       "Type in your name (max 8 chars)",
                                       "", "Hervog");
                phase = Phase.UPDATE_HALLFAME;
                break;

            case UPDATE_HALLFAME:
                if (nameInput.name == null) {
                    // Do nothing while player doesn't input anything
                    return;
                }
                // Add player to hall of fame
                addPlayer(nameInput.name, score, playerPosition);
                writeFile();
                // Get hall of fame String and advance phase
                prepareDrawText();
                phase = Phase.SHOW_HALLFAME;
                break;
            
            case SHOW_HALLFAME:
                // Update height
                height += speed*delta;
                // Check if all text is above the bottom of the screen
                float minHeight = height - (MAX_SIZE + 1)*font.getLineHeight();
                if (minHeight > font.getLineHeight()) {
                    phase = Phase.END;
                }
                break;

            case END:
                // Do nothing
                break;
        }
    }

    /*
     * Draws hall of fame info on screen.
     */
    public void draw(float delta) {
        if (drawNames == null || drawScores == null) {
            // Nothing to draw, then!
            return;
        }

        spriteBatch.begin();

        // Draw title
        font.draw(spriteBatch, title,
                  (Constant.GAME_WIDTH - title.width)/2,
                  height + title.height);

        // Draw each hall of fame line
        for (int i = 0; i < drawNames.length; i++) {
            // i+1 takes into account that the title
            // is drawn before everything
            float y = height - (i+1)*font.getLineHeight();

            font.draw(spriteBatch, drawNames[i],
                      (Constant.GAME_WIDTH - title.width)/2 - drawNames[i].width, y);
            font.draw(spriteBatch, drawScores[i],
                      (Constant.GAME_WIDTH + title.width)/2, y);
        }
        spriteBatch.end();
    }

    /*
     * Returns true if all actions in the hall of fame are finished.
     */
    public boolean isDone() {
        return phase == Phase.END;
    }

    /*
     * Checks if the player with the specified name and score has
     * entered the hall of fame, returning their new conquered position
     * there or -1 if they didn't make it.
     */
    private int checkNewRecord(int score) {
        if (topPlayers.size() < MAX_SIZE) {
            // If there is space, add player
            return topPlayers.size();
        }

        for (int i = 0; i < topPlayers.size(); i++) {
            if (topPlayers.get(i).points < score) {
                return i;
            }
        }

        return -1;
    }

    /*
     * Adds player with specified name and score to the given position
     * in the hall of fame.
     */
    private void addPlayer(String name, int score, int position) {
        topPlayers.add(position, new Tag(name, score));
        if (topPlayers.size() > MAX_SIZE) {
            // Control list size by removing final player
            topPlayers.removeLast();
        }
    }

    /*
     * Prepares containing a player name and their
     * respective score per line.
     */
    private void prepareDrawText() {
        drawNames  = new GlyphLayout[MAX_SIZE];
        drawScores = new GlyphLayout[MAX_SIZE];

        // Store top players
        for (int i = 0; i < topPlayers.size(); i++) {
            Tag player = topPlayers.get(i);
            // Change color if player is on hall of fame
            if (i == playerPosition) font.setColor(Color.YELLOW);

            drawNames[i] = new GlyphLayout();
            drawNames[i].setText(font, player.name);
            drawScores[i] = new GlyphLayout();
            drawScores[i].setText(font, String.valueOf(player.points));

            // Revert to normal color
            if (i == playerPosition) font.setColor(Color.WHITE);
        }

        // Store unique Strings if not all positions
        // in hall of fame are occupied
        for (int i = topPlayers.size(); i < MAX_SIZE; i++) {
            drawNames[i] = new GlyphLayout();
            drawNames[i].setText(font, "------");
            drawScores[i] = new GlyphLayout();
            drawScores[i].setText(font, "----");
        }
    }

    /*
     * Reads hall of fame from file.
     */
    private void readFile() {
        // Get file in same directory as this class
        URL url = getClass().getResource(HALLFAME_FILE);
        File file = new File(url.getFile());
        Scanner scanner;

        // Create empty file if it doesn't exist
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error when creating empty hall of fame file!");
                return;
            }
        }

        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("Error when reading hall of fame file!");
            return;
        }

        // Read each line from the file
        for (int i = 0; scanner.hasNextLine() && i < MAX_SIZE; i++) {
            String line = scanner.nextLine();
            // Separate name from points according to last space character
            int spacePos = line.lastIndexOf(' ');
            String name = line.substring(0, spacePos);
            int points = Integer.parseInt(line.substring(spacePos + 1));
            // Update top players list
            Tag player = new Tag(name, points);
            topPlayers.add(player);
        }

        scanner.close();
    }

    /*
     * Writes the current hall of fame in a text file.
     */
    private void writeFile() {
        // Get file in same directory as this class
        URL url = getClass().getResource(HALLFAME_FILE);
        File file = new File(url.getPath());

        try {
            // Overwrite existing file
            FileWriter writer = new FileWriter(file, false);
            for (Tag player : topPlayers) {
                writer.write(player.name + " " + player.points + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error when updating hall of fame file!");
            return;
        }
    }

    /*
     * Clears memory used by HallFame class.
     */
    public void dispose() {
        font.dispose();
    }
}
