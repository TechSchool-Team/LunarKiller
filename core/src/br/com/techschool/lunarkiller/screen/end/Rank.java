package br.com.techschool.lunarkiller.screen.end;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import br.com.techschool.lunarkiller.util.Constant;

/*
 * Stores game ranks for future searches.
 */
public class Rank {

    // Contains all phases that can occur on this screen
    private enum Phase {
        SHOW_SCORE, SHOW_RANK, END
    };

    // Player's score
    private int score;

    // Player's rank
    private String rankName;

   // Stores existing ranks
    private LinkedList<Tag> ranks;

    // Main layer from end screen
    private SpriteBatch spriteBatch;

    // Font used for drawing score and rank
    private BitmapFont font;

    // Centralizes drawn score and rank
    GlyphLayout scoreGlyph, rankGlyph;

    // Controls current font transparency for score and rank drawing
    private float scoreAlpha, rankAlpha;

    // Change in alpha per frame
    private final float deltaAlpha = 0.010f;

    // Controls what action is currently happening
    private Phase phase;

    // Name of text file containing ranks
    private static final String RANK_FILE = "Rank.txt";

    /*
     * Reads and stores all available ranks from a file.
     * 'score' is the player's points after playing the game.
     */
    public Rank(int score, SpriteBatch spriteBatch) {
        this.score = score;
        this.spriteBatch = spriteBatch;

        // Read font
        // TODO: Define font!
        font = new BitmapFont(Gdx.files.internal("fonts/debug.fnt"));
        scoreGlyph = new GlyphLayout();
        rankGlyph  = new GlyphLayout();

        ranks = new LinkedList<Tag>();
        phase = Phase.SHOW_SCORE;
        scoreAlpha = 0;
        rankAlpha  = 0;

        readFile();
        rankName = getRank(score);
    }

    /*
     * Updates alpha of the font used to draw rank information.
     */
    public void update(float delta) {
        switch(phase) {
            case SHOW_SCORE:
                // Update font transparency
                scoreAlpha += deltaAlpha;
                if (scoreAlpha > 1.0f) {
                    scoreAlpha = 1.0f;
                    phase = Phase.SHOW_RANK;
                }
                break;

            case SHOW_RANK:
                // Update font transparency
                rankAlpha += deltaAlpha;
                if (rankAlpha > 1.0f) {
                    rankAlpha = 1.0f;
                    phase = Phase.END;
                }
                break;

            case END:
                break;
        }
    }

    public void draw(float delta) {
        spriteBatch.begin();

        // Draw score
        font.setColor(1.0f, 1.0f, 1.0f, scoreAlpha);
        scoreGlyph.setText(font, "Final Score: " + score);
        font.draw(spriteBatch, scoreGlyph,
                 (Constant.GAME_WIDTH - scoreGlyph.width)/2,
                 (Constant.GAME_HEIGHT + scoreGlyph.height)/2);

        // Draw rank
        font.setColor(1.0f, 1.0f, 1.0f, rankAlpha);
        rankGlyph.setText(font, "Rank: " + rankName);
        font.draw(spriteBatch, rankGlyph,
                 (Constant.GAME_WIDTH - rankGlyph.width)/2,
                  Constant.GAME_HEIGHT/2 - 2*rankGlyph.height);

        spriteBatch.end();
    }

    /*
     * Returns true if score and rank are drawn.
     */
    public boolean isDone() {
        return phase == Phase.END;
    }

    /*
     * Reads ranks from existing file.
     */
    private void readFile() {
        // Get file in same directory as this class
        URL url = getClass().getResource(RANK_FILE);
        File file = new File(url.getPath());
        Scanner scanner;

        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: Rank file not found!");
            return;
        }

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            // Split line into points and rank name
            String[] parts = line.split("\\s+", 2);
            // Create new rank and add to list
            Tag rank = new Tag(parts[1], Integer.parseInt(parts[0]));
            ranks.add(rank);
        }

        scanner.close();
    }

    /*
     * Returns the rank attained for the given score.
     * If no ranks exist, returns an empty String.
     */
    private String getRank(int score) {
        String currentRank = "";

        for (int i = 0; i < ranks.size(); i++) {
            Tag rank = ranks.get(i);
            currentRank = rank.name;
            if (score > rank.points) {
                // The player's rank is stored in 'currentRank'!
                break;
            }
        }

        return currentRank;
    }

    /*
     * Clears memory used by Rank class.
     */
    public void dispose() {
        font.dispose();
    }
}
