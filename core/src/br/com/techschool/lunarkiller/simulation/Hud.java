package br.com.techschool.lunarkiller.simulation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import br.com.techschool.lunarkiller.util.Constant;

/*
 * Displays a HUD containing player information during
 * the game loop screen.
 */
public class Hud {

    // Same spriteBatch used by the Renderer class
    private SpriteBatch spriteBatch;

    // Font used for drawing information
    private BitmapFont font;

    // Draws current score with better control over position
    private GlyphLayout scoreGlyph;

    /*
     * Initializes HUD attributes.
     */
    public Hud(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;

        // Load font and glyph
        font = new BitmapFont(Gdx.files.internal("fonts/cyber.fnt"));
        scoreGlyph = new GlyphLayout();
   }

    /*
     * Draws player information on the screen.
     */
    public void draw(float delta, float score) {
        scoreGlyph.setText(font, String.format("%07.0f", score));

        // Calculate positions to draw glyph
        float scoreX = Constant.GAME_WIDTH - 1.1f*scoreGlyph.width;
        float scoreY = Constant.GAME_HEIGHT - 2*scoreGlyph.height;

        // Draw glyph
        spriteBatch.begin();
        font.draw(spriteBatch, scoreGlyph, scoreX, scoreY);
        spriteBatch.end();
    }

    /*
     * Clears memory used by Hud class.
     */
    public void dispose() {
        font.dispose();
    }
}
