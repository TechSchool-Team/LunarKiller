package br.com.techschool.lunarkiller.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

import br.com.techschool.lunarkiller.util.Credits;
import br.com.techschool.lunarkiller.util.ScrollEffect;
import br.com.techschool.lunarkiller.util.StartMenu;

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

    // Menu used on this screen
    private StartMenu startMenu;

    // Rolling credits effect
    private Credits credits;

    // Background music played on this screen
    private Music soundTrack;

    // Narration sound effect
    private Sound narration;

    /*
     * Controls what action the camera is currently doing:
     * 0 = Start scroll effect
     * 1 = During scroll effect
     * 2 = Main menu
     * 3 = Start credits
     * 4 = During credits
     */
    private int phase;

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
        startMenu    = new StartMenu();

        // Start scroll effect
        phase = 0;

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

        switch(phase) {
            case 4:
                // During credits
                credits.update(delta);
                if (Gdx.input.justTouched()) {
                    credits.dispose();
                    credits = null;
                    phase = 2;
                }
                break;

            case 3:
                // Start credits
                credits = new Credits(spriteBatch);
                phase = 4;
                break;

            case 2:
                // Main menu, choose option
                startMenu.update(delta);
                if (Gdx.input.isKeyPressed(Input.Keys.C))
                    phase = 3;
                // Temporary
                if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
                 // Move on to next screen
                    // TODO: Music and narration!
                    // soundTrack.stop();
                    // narration.stop();
                    setDone(true);
                }
                break;

            case 1:
                // During scroll camera
                if (Gdx.input.justTouched() || scrollEffect.isDone()) {
                    scrollEffect.setDone();
                    phase = 2;
                }
                break;

            case 0:
                // Begin scroll camera
                scrollEffect = new ScrollEffect();
                phase = 1;
                break;
        }
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

        if (phase == 2)
            startMenu.draw(delta);

        // Draw credits, if possible
        if (credits != null) {
            credits.draw(delta);
        }
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        background.dispose();
        if (credits != null)
            credits.dispose();
        // TODO: Music and narration!
        // soundTrack.dispose();
        // narration.dispose();
    }
}
