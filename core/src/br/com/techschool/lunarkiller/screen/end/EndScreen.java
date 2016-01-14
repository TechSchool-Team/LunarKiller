package br.com.techschool.lunarkiller.screen.end;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

import br.com.techschool.lunarkiller.screen.GenericScreen;
import br.com.techschool.lunarkiller.util.Constant;

/*
 * Screen that appears after the game is over.
 */
public class EndScreen extends GenericScreen {

    // Contains all phases that can occur on this screen
    private enum Phase {
        BEGIN, RANK, FAME, END
    };

    // Manipulates a (bitmap) image
    private Texture background;

    // Responsible for drawing layers
    private SpriteBatch spriteBatch;

    // Matrix related to the standard orthogonal plane
    private Matrix4 viewMatrix;

    // Matrix that accumulates transformation coefficients
    private Matrix4 tranMatrix;

    // Controls what action is currently happening on this screen
    private Phase phase;

    // Controls current layer and font transparency
    private float screenAlpha;

    private final float deltaAlpha = 0.010f;

    // Score gained by playing the game
    private int score;

    // Stores available game ranks
    private Rank rank;

    // Stores local players with top scores
    private HallFame hallFame;

    // Background music played on this screen
    private Music soundTrack;

    /*
     * Creates an EndScreen object with the given name.
     */
    public EndScreen(String name, int score) {
        super(name);
        this.score = score;

        // TODO: Define a background!
        background  = new Texture(Gdx.files.internal("backgrounds/startMenu.jpg"));

        spriteBatch = new SpriteBatch();

        viewMatrix = new Matrix4();
        tranMatrix = new Matrix4();

        phase = Phase.BEGIN;
        screenAlpha = 0;

        soundTrack = Gdx.audio.newMusic(Gdx.files.internal("sound/bgm/opening.mp3"));
        soundTrack.setLooping(true);
        soundTrack.play();
    }

    @Override
    public void update(float delta) {
        switch(phase) {
            case BEGIN:
                screenAlpha += deltaAlpha;
                if (screenAlpha > 1.0f) {
                    screenAlpha = 1.0f;
                    rank = new Rank(score, spriteBatch);
                    phase = Phase.RANK;
                }
                break;

            case RANK:
                rank.update(delta);
                if (rank.isDone() && Gdx.input.isTouched()) {
                    rank = null;
                    hallFame = new HallFame(score, spriteBatch);
                    phase = Phase.FAME;
                }
                break;

            case FAME:
                hallFame.update(delta);
                if (hallFame.isDone() && Gdx.input.isTouched()) {
                    hallFame = null;
                    phase = Phase.END;
                }
                break;

            case END:
                screenAlpha -= deltaAlpha;
                if (screenAlpha < 0.0f) {
                    screenAlpha = 0.0f;
                    soundTrack.stop();
                    setDone(true);
                }
                break;
        }
    }

    @Override
    public void draw(float delta) {
        // Clear screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Define area that can be drawn
        viewMatrix.setToOrtho2D(0, 0, Constant.GAME_WIDTH, Constant.GAME_HEIGHT);

        // Configure drawing area
        spriteBatch.setProjectionMatrix(viewMatrix);
        spriteBatch.setTransformMatrix(tranMatrix);
        spriteBatch.setColor(1.0f, 1.0f, 1.0f, screenAlpha);

        // Draw background
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0);
        spriteBatch.end();

        // Draw other classes, if possible
        if (rank != null) {
            rank.draw(delta);
        }
        else if (hallFame != null) {
            hallFame.draw(delta);
        }
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        background.dispose();

        if (rank != null) {
            rank.dispose();
        }
        if (hallFame != null) {
            hallFame.dispose();
        }

        soundTrack.dispose();
    }
}
