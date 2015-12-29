package br.com.techschool.lunarkiller.screen;

import com.badlogic.gdx.Screen;

/*
 * Base abstract class for all of this game's screens.
 */
public abstract class GenericScreen implements Screen {

    // This screen's name
    private String name;

    // If true, then the screen has finished its tasks
    private boolean done;

    /*
     * Creates a Screen object with the specified name.
     */
    public GenericScreen(String name) {
        this.name = name;
        this.done = false;
    }

    /*
     * Returns this screen's name.
     */
    public String getName() {
        return this.name;
    }

    /*
     * Sets if the screen has finished its tasks with the specified boolean.
     */
    public void setDone(boolean done) {
        this.done = done;
    }

    /*
     * Returns true if this screen has finished its tasks, or
     * false otherwise.
     */
    public boolean isDone() {
        return this.done;
    }

    @Override
    public void show() {
        // Implemented on derived classes
    }

    /*
     * Updates all actions happening on this screen.
     * Delta is the time between frames, in seconds.
     */
    public abstract void update(float delta);

    /*
     * Draws objects that occur on this screen.
     * Delta is the time between frames, in seconds.
     */
    public abstract void draw(float delta);

    @Override
    public void render(float delta) {
        update(delta);
        draw(delta);
    }

    @Override
    public void resize(int width, int height) {
        // Implemented on derived classes
    }

    @Override
    public void pause() {
        // Implemented on derived classes
    }

    @Override
    public void resume() {
        // Implemented on derived classes
    }

    @Override
    public void hide() {
        // Implemented on derived classes
    }

    @Override
    public void dispose() {
        // Implemented on derived classes
    }

}
