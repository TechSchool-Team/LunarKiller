package br.com.techschool.lunarkiller.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

/*
 * Screen where the game action itself happens!
 */
public class GameLoopScreen extends GenericScreen {

    /*
     * Creates a GameLoopScreen object with the given name.
     */
    public GameLoopScreen(String name) {
        super(name);
        // TODO Check what to do here
    }

    @Override
    public void update(float delta) {
        // TODO Check what to do here
    }

    @Override
    public void draw(float delta) {
        // TODO Check what to do here

        // Clear screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}
