package br.com.techschool.lunarkiller.screen.start;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

import br.com.techschool.lunarkiller.screen.GenericScreen;
import br.com.techschool.lunarkiller.screen.start.StartMenu.Command;

/*
 * Initial screen, appears when game is started.
 */
public class StartScreen extends GenericScreen {

    // Contains all phases that can occur on this screen
    private enum Phase {
        BEGIN, SCROLL, SKIP_TO_MENU, MENU,
        START_CREDITS, CREDITS, STOP_CREDITS
    };

    // References the current background image
    private Texture background;

    // Comic introduction image
    private Texture comicBg;

    // Start menu image
    private Texture menuBg;

    // Responsible for drawing layers
    private SpriteBatch spriteBatch;

    // Matrix that accumulates transformation coefficients
    private Matrix4 tranMatrix;

    // Controls what action is currently happening on this screen
    private Phase phase;

    // Controls current layer transparency
    private float alpha;

    // Change in alpha per frame
    private final float deltaAlpha = 0.010f;

    // Identifies, on a flashing screen, if alpha is being raised or lowered
    private boolean alphaGoingDark;

    // Camera effect used on beginning of this screen
    private StartCamera startCamera;

    // Menu used on this screen
    private StartMenu startMenu;

    // Rolling credits effect
    private Credits credits;

    // Background music played on this screen
    private Music soundTrack;

    // Narration sound effect
    private Sound narration;

    // Volume that the soundtrack starts at
    private final float initialVolume = 0.2f;

    // Volume that the soundtrack gains per frame
    private final float deltaVolume = 0.05f;

    /*
     * Creates a StartScreen object with the given name.
     */
    public StartScreen(String name) {
        super(name);

        // TODO: Define comic background!
        comicBg = new Texture(Gdx.files.internal("backgrounds/debug.jpg"));
        menuBg  = new Texture(Gdx.files.internal("backgrounds/startMenu.jpg"));
        background = comicBg;

        spriteBatch  = new SpriteBatch();
        tranMatrix   = new Matrix4();

        startCamera = new StartCamera();

        // Configure initial phase
        phase = Phase.BEGIN;
        alpha = 0;
        alphaGoingDark = true;
        spriteBatch.setColor(1.0f, 1.0f, 1.0f, alpha);

        // Configure main soundtrack
        soundTrack = Gdx.audio.newMusic(Gdx.files.internal("sound/bgm/opening.mp3"));
        soundTrack.setLooping(true);

        // Music starts low because of narration
        soundTrack.setVolume(initialVolume);
        soundTrack.play();

        // TODO: Define narration!
        // narration = Gdx.audio.newSound(Gdx.files.internal("???"));
        // narration.play();
    }

    @Override
    public void update(float delta) {
        switch(phase) {
            case BEGIN:
                background = comicBg;
                startCamera.reset();
                startCamera.update(delta);
                startMenu = null;
                // Raise alpha to lighten screen
                alpha += deltaAlpha;
                if (alpha > 1.0f) {
                    alpha = 1.0f;
                    phase = Phase.SCROLL;
                }
                break;

            case SCROLL:
                startCamera.update(delta);
                // TODO: Define controls
                if (Gdx.input.justTouched() || startCamera.isFixed()) {
                    // Interrupt camera effect, making it fixed
                    startCamera.setFixed();
                    phase = Phase.SKIP_TO_MENU;
                }
                break;

            case SKIP_TO_MENU:
                // Change soundtrack volume and screen alpha
                soundTrack.setVolume(soundTrack.getVolume() + deltaVolume);
                alpha += (alphaGoingDark ? -4*deltaAlpha : 4*deltaAlpha);

                // Screen is totally dark
                if (alpha < 0.0f) {
                    alpha = 0.0f;
                    alphaGoingDark = false;
                    // Update camera to show it fixed after everything is dark
                    startCamera.update(delta);
                    // Update background to menu image
                    background = menuBg;
                }

                // Screen is back to normal
                if (alpha > 1.0f) {
                    alpha = 1.0f;
                    // Wait for volume to reach maximum
                    if (soundTrack.getVolume() > 1.0f) {
                        soundTrack.setVolume(1.0f);
                        startMenu = new StartMenu();
                        alphaGoingDark = true;
                        phase = Phase.MENU;
                    }
                }
                break;

            case MENU:
                startCamera.update(delta);
                startMenu.update(delta);
                // Check menu buttons
                if (startMenu.isButtonChecked(Command.START)) {
                    // Move on to next screen
                    soundTrack.stop();
                    // TODO: Narration!
                    // narration.stop();
                    setDone(true);
                }
                else if (startMenu.isButtonChecked(Command.CREDITS)) {
                    // Reset the button, and move to credits phase
                    startMenu.setButtonChecked(Command.CREDITS, false);
                    phase = Phase.START_CREDITS;
                }
                else if (startMenu.isButtonChecked(Command.QUIT)) {
                    // TODO: Find better way of exiting the game
                    soundTrack.stop();
                    dispose();
                    Gdx.app.exit();
                }
                break;

            case START_CREDITS:
                startMenu = null;
                alpha -= deltaAlpha;
                if (alpha <= 0.2f) {
                    alpha = 0.2f;
                    credits = new Credits(spriteBatch);
                    phase = Phase.CREDITS;
                }
                break;

            case CREDITS:
                credits.update(delta);
                // Move on if something is pressed or credits are finished
                if (Gdx.input.justTouched() || credits.isDone()) {
                    credits.dispose();
                    credits = null;
                    phase = Phase.STOP_CREDITS;
                }
                break;

            case STOP_CREDITS:
                alpha += deltaAlpha;
                if (alpha > 1.0f) {
                    startMenu = new StartMenu();
                    alpha = 1.0f;
                    phase = Phase.MENU;
                }
                break;
        }
    }

    @Override
    public void draw(float delta) {
        // Clear screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Configure drawing area
        spriteBatch.setProjectionMatrix(startCamera.camera.combined);
        spriteBatch.setTransformMatrix(tranMatrix);
        spriteBatch.setColor(1.0f, 1.0f, 1.0f, alpha);

        // Draw background according to current phase
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0);
        spriteBatch.end();

        // Draw menu, if possible
        if (startMenu != null) {
            startMenu.draw(delta);
        }

        // Draw credits, if possible
        if (credits != null) {
            credits.draw(delta);
        }
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        comicBg.dispose();
        menuBg.dispose();

        if (startMenu != null) {
            startMenu.dispose();
        }
        if (credits != null) {
            credits.dispose();
        }

        soundTrack.dispose();
        // TODO: Narration!
        // narration.dispose();
    }
}
