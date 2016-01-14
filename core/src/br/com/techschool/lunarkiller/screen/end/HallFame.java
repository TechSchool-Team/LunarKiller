package br.com.techschool.lunarkiller.screen.end;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import br.com.techschool.lunarkiller.util.Constant;

/*
 * Reads, stores and updates players with highest achieved scores.
 */
public class HallFame {

    // Contains all phases that can occur on this screen
    private enum Phase {
        CHECK_SCORE, ASK_NAME, SHOW_HALLFAME, END
    };

    // Player's score
    private int score;

    // Player's position on the hall of fame, with 0 as highest.
    // Is -1 if the player isn't in the hall of fame.
    private int position;

    // Text to be drawn on screen
    private String drawText;

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

    // Number of players in ranking
    private final int MAX_SIZE = 10;

    // File containing top players and their scores
    private final String HALLFAME_FILE = "HallFame.txt";

    public HallFame(int score, SpriteBatch spriteBatch) {
        this.score = score;
        this.spriteBatch = spriteBatch;

        // Read font
        // TODO: Define a font!
        font = new BitmapFont(Gdx.files.internal("fonts/debug.fnt"));

        topPlayers = new LinkedList<Tag>();
        phase = Phase.CHECK_SCORE;
        nameInput = new NameInput();

        readFile();
    }

    public void update(float delta) {
        switch(phase) {
            case CHECK_SCORE:
                position = newRecord(score);
                if (position >= 0) {
                    // New hall of fame member!
                    phase = Phase.ASK_NAME;
                }
                else {
                    // Proceed normally to show hall of fame
                    drawText = getTopPlayers();
                    phase = Phase.SHOW_HALLFAME;
                }
            break;

            case ASK_NAME:
                // Reads player name and adds them to hall of fame
                Gdx.input.getTextInput(nameInput, "Type in your name", "", "Hervog");
                while (nameInput.name == null);
                addPlayer(nameInput.name, score, position);
                writeFile();
                // Get hall of fame String and advance phase
                drawText = getTopPlayers();
                phase = Phase.SHOW_HALLFAME;
                break;

            case SHOW_HALLFAME:
                // Update
                break;

            case END:
                // Do nothing
                break;
        }
    }

    public void draw(float delta) {
        if (drawText == null) {
            // Nothing to draw, then!
            return;
        }

        spriteBatch.begin();
        font.draw(spriteBatch, drawText, 0, Constant.GAME_HEIGHT - 20);
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
    private int newRecord(int score) {
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
     * Returns a big String containing a player name and their
     * respective score per line.
     */
    private String getTopPlayers() {
        String str = "";

        for (int i = 0; i < topPlayers.size(); i++) {
            Tag player = topPlayers.get(i);
            str += player.name + " " + player.points;
        }

        return str;
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
            String name = line.substring(0, spacePos - 1);
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
                writer.write(player.name + " " + player.points);
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
