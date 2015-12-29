package br.com.techschool.lunarkiller.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

import br.com.techschool.lunarkiller.util.Constant;

/*
 * Screen that appears after the game is over.
 */
public class EndScreen extends GenericScreen {

    // Manipulates a (bitmap) image
    private Texture background;

    // Responsible for drawing layers
    private SpriteBatch spriteBatch;

    // Matrix related to the standard orthogonal plane
    private Matrix4 viewMatrix;

    // Matrix that accumulates transformation coefficients
    private Matrix4 tranMatrix;

    // Background music played on this screen
    private Music soundTrack;

    /*
     * Creates an EndScreen object with the given name.
     */
    public EndScreen(String name) {
        super(name);

        // TODO: Define a background!
        // background  = new Texture(Gdx.files.internal("???"));

        spriteBatch = new SpriteBatch();

        viewMatrix = new Matrix4();
        tranMatrix = new Matrix4();

        // TODO: Define a music!
        // soundTrack = Gdx.audio.newMusic(Gdx.files.internal("???"));
        // soundTrack.play();
    }

    @Override
    public void update(float delta) {
        // End this screen on input
        if (Gdx.input.justTouched()) {
            // TODO: Music!
            // soundTrack.stop();
            setDone(true);
        }
    }

    @Override
    public void draw(float delta) {
        // Define area that can be drawn
        viewMatrix.setToOrtho2D(0, 0, Constant.GAME_WIDTH, Constant.GAME_HEIGHT);

        // Configure drawing area
        spriteBatch.setProjectionMatrix(viewMatrix);
        spriteBatch.setTransformMatrix(tranMatrix);

        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0);
        spriteBatch.end();
    }

    @Override
    public void hide() {
        spriteBatch.dispose();
        background.dispose();
        // TODO: Music!
        // soundTrack.dispose();
    }
}
