package br.com.techschool.lunarkiller.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

import br.com.techschool.lunarkiller.util.Constant;

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

    // Camera used to zoom and scroll through the background
    private OrthographicCamera camera;

    // Initial zoom used on camera for scrolling effect
    private final float initialZoom = 0.25f;

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
        background  = new Texture(Gdx.files.internal("debug.jpg"));

        spriteBatch = new SpriteBatch();
        tranMatrix = new Matrix4();

        // Set initial camera on middle of the screen
        camera = new OrthographicCamera(Constant.GAME_WIDTH, Constant.GAME_HEIGHT);
        camera.translate(Constant.GAME_WIDTH/2, Constant.GAME_HEIGHT/2);

        // Calculate camera coordinates to position camera on upper left
        // border of the zoomed background (formulas below)
        camera.zoom = initialZoom;
        float dx = (Constant.GAME_WIDTH/2) * (1 - initialZoom);
        float dy = (Constant.GAME_HEIGHT/2) * (1 - initialZoom);
        camera.translate(-dx, dy);

        // TODO: Define a music!
        // soundTrack = Gdx.audio.newMusic(Gdx.files.internal("???"));
        // soundTrack.play();

        // TODO: Define narration!
        // narration = Gdx.audio.newSound(Gdx.files.internal("???"));
        // narration.play();
    }

    @Override
    public void update(float delta) {
        // Camera manipulation
        moveCamera(delta);
        camera.update();

        // End this screen on input
        if (Gdx.input.justTouched()) {
            // TODO: Music and narration!
            // soundTrack.stop();
            // narration.stop();
            setDone(true);
        }

        // DEBUG
        System.out.printf(">> Camera: (%g, %g), ZOOM = %g\n",
        camera.position.x, camera.position.y, camera.zoom);
    }

    @Override
    public void draw(float delta) {
        // Configure drawing area
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.setTransformMatrix(tranMatrix);

        // Clear screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw background
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0);
        spriteBatch.end();
    }

    /*
     * Moves camera through the background while narration
     * is played.
     * Delta is the time between frames.
     */
    private void moveCamera(float delta) {
        // TODO: Implement movement
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
