package br.com.techschool.lunarkiller.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;

import br.com.techschool.lunarkiller.simulation.GameAction;
import br.com.techschool.lunarkiller.simulation.Renderer;

/*
 * Screen where the game itself is played!
 */
public class GameLoopScreen extends GenericScreen {

    // Updates objects on this screen
    private GameAction gameAction;

    // Draws objects on this screen
    private Renderer renderer;

    // Background music played on this screen
    private Music soundTrack;

    /*
     * Creates a GameLoopScreen object with the given name.
     */
    public GameLoopScreen(String name) {
        super(name);

        gameAction = new GameAction();
        renderer = new Renderer(gameAction);

        // Configure main soundtrack
        soundTrack = Gdx.audio.newMusic(Gdx.files.internal("sound/bgm/battle.mp3"));
        soundTrack.setLooping(true);
        soundTrack.play();
        
        Gdx.input.setInputProcessor(new br.com.techschool.lunarkiller.util.CustomInput());
    }

    @Override
    public void update(float delta) {
        gameAction.update(delta);
        // DEBUG below!
        if (Gdx.input.isKeyPressed(Keys.Q)) {
            soundTrack.stop();
            setDone(true);
        }
        if (Gdx.input.isKeyJustPressed(Keys.C)){
        	renderer.changeCamera();
        }
    }

    @Override
    public void draw(float delta) {
        renderer.draw(delta);
    }

    /*
     * Returns the player's current score
     */
    public int getScore() {
        return gameAction.score;
    }

    @Override
    public void dispose() {
        renderer.dispose();
        soundTrack.dispose();
    }
}
