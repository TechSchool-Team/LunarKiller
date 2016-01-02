package br.com.techschool.lunarkiller.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

import br.com.techschool.lunarkiller.util.Credits;
import br.com.techschool.lunarkiller.util.ScrollEffect;

/*
 * Initial screen, appears when game is started.
 */
public class StartScreen extends GenericScreen {

    // Manipulates a (bitmap) image
    private Texture background;

    // Responsible for drawing layers
    private SpriteBatch spriteBatch;

    // Matrix that accumulates transformation coefficients
    private Matrix4 tranMatrix;

    // Camera effect used on beginning of this screen
    private ScrollEffect scrollEffect;

    // Rolling credits effect
    private Credits credits;

    // Background music played on this screen
    private Music soundTrack;

    // Narration sound effect
    private Sound narration;

    /*
     * Creates a StartScreen object with the given name.
     */
    public StartScreen(String name) {
        super(name);

        // TODO: Define background!
        background = new Texture(Gdx.files.internal("backgrounds/debug.jpg"));

        spriteBatch  = new SpriteBatch();
        tranMatrix   = new Matrix4();
        scrollEffect = new ScrollEffect();
        credits = new Credits(spriteBatch);

        // TODO: Define a music!
        // soundTrack = Gdx.audio.newMusic(Gdx.files.internal("???"));
        // soundTrack.play();

        // TODO: Define narration!
        // narration = Gdx.audio.newSound(Gdx.files.internal("???"));
        // narration.play();
    }

    @Override
    public void update(float delta) {
        scrollEffect.update(delta);

        if (Gdx.input.justTouched()) {
            // Stop camera effect
            if (!scrollEffect.isDone()) {
                scrollEffect.setDone();
            }
            else {
                // Move on to next screen
                // TODO: Music and narration!
                // soundTrack.stop();
                // narration.stop();
                setDone(true);
            }
        }

        credits.update(delta);
    }

    @Override
    public void draw(float delta) {
        // Configure drawing area
        spriteBatch.setProjectionMatrix(scrollEffect.camera.combined);
        spriteBatch.setTransformMatrix(tranMatrix);

        // Clear screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw background
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0);
        spriteBatch.end();

        // Draw credits
        credits.draw(delta);
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        background.dispose();
        // TODO: Music and narration!
        // soundTrack.dispose();
        // narration.dispose();
    }
}
