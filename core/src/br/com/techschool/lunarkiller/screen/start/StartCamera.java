package br.com.techschool.lunarkiller.screen.start;

import com.badlogic.gdx.graphics.OrthographicCamera;

import br.com.techschool.lunarkiller.util.Constant;

/*
 * Contains camera effects used on start screen.
 */
public class StartCamera {

    // Contains all effects used on this camera
    private enum Phase {
        UPPER_LEFT_ZOOM, SCROLL, UPDATE_SCROLL, UNZOOM, FIXED
    };

    // Camera used to zoom and scroll through the background
    public OrthographicCamera camera;

    // Distance between zoomed camera's center and its horizontal/vertical bounds.
    // Formulas were calculated on paper.
    private float rx;
    private float ry;

    // x/y speed at which the camera moves back to the background's center
    // while unzooming.
    private float unzoomVx;
    private float unzoomVy;

    // How much the camera is later unzoomed per frame
    private final float unzoomRate = 0.0016f;

    // x coordinates where a line in a comic ends.
    // These values are relative to the background itself!
    private final float[] lineDivisors = new float[] {510, 335, 165, 0};

    // Speed at which the camera scrolls for each line.
    // Values were calculated through many tests!
    private final float[] lineSpeed = new float[] {30, 30, 30, 30};

    // Indicates which line the camera is currently showing
    int lineCount;

    // Controls what action the camera is currently doing
    private Phase phase;

    // Time delay between phases
    private float delay;

    // Stores passed time until it reaches the delay variable
    private float timePassed;

    // Start and end of comic on the x axis
    private final float COMIC_INIT_BORDER = 223;
    private final float COMIC_END_BORDER  = 801;

    /*
     * Initializes camera attributes and phase.
     */
    public StartCamera() {
        // Create camera with default resolution
        camera = new OrthographicCamera(Constant.GAME_WIDTH, Constant.GAME_HEIGHT);

        lineCount = 0;

        // Configure initial phase (no delay)
        phase = Phase.UPPER_LEFT_ZOOM;
        setNewDelay(0);
    }

    /*
     * Moves camera through the background.
     * Delta is the time between frames.
     */
    public void update(float delta) {
        // Check delay time
        timePassed += delta;
        if (timePassed < delay)
            return;

        switch(phase) {
            case UPPER_LEFT_ZOOM:
                // Zoom and move camera to upper left border of the background
                configCamera(lineDivisors[lineCount], Constant.GAME_HEIGHT);
                // For the first line, start outside the comic
                camera.position.x = 40.0f;
                // Advance phase
                phase = Phase.SCROLL;
                break;

            case SCROLL:
                // Scroll through background, line per line
                if (scrollLine(delta, lineSpeed[lineCount])) {
                    lineCount++;
                    if (lineCount < lineDivisors.length) {
                        setNewDelay(1.0f);
                        phase = Phase.UPDATE_SCROLL;
                    }
                    else {
                        // All lines were scrolled through
                        calculateUnzoomSpeed();
                        setNewDelay(3.0f);
                        phase = Phase.UNZOOM;
                    }
                }
                break;

            case UPDATE_SCROLL:
                // Configure camera attributes for next line
                configCamera(lineDivisors[lineCount],
                             lineDivisors[lineCount - 1]);
                setNewDelay(0.5f);
                phase = Phase.SCROLL;
                break;

            case UNZOOM:
                // Slowly unzoom and move camera to the
                // center of the background
                camera.zoom += unzoomRate;
                camera.translate(-unzoomVx, unzoomVy);
                if (camera.zoom >= 1.00f) {
                    // Unzooming is finished!
                    setNewDelay(1.0f);
                    phase = Phase.FIXED;
                }
                break;

            case FIXED:
                // Fix camera on center of background
                camera.zoom = 1.00f;
                camera.position.x = Constant.GAME_WIDTH/2;
                camera.position.y = Constant.GAME_HEIGHT/2;
                break;
        }

        camera.update();
    }

    /*
     * Resets camera effects to the first one.
     */
    public void reset() {
        phase = Phase.UPPER_LEFT_ZOOM;
        delay = timePassed = 0;
    }

    /*
     * Ends all camera effects.
     */
    public void setFixed() {
        phase = Phase.FIXED;
        delay = 0;
    }

    /*
     * Returns true if all camera effects are finished.
     */
    public boolean isFixed() {
        return phase == Phase.FIXED;
    }

    /*
     * Reconfigure camera zoom based on next line's y coordinate (yf)
     * and current line's y value (yi).
     */
    private void configCamera(float yf, float yi) {
        // Calculate camera's new zoom based on dy
        camera.zoom = (yi - yf)/Constant.GAME_HEIGHT;

        // Recalculate border distances
        rx = camera.zoom*Constant.GAME_WIDTH/2;
        ry = camera.zoom*Constant.GAME_HEIGHT/2;

        // Move zoomed camera's center position to beginning
        // of next line
        camera.position.x = rx + COMIC_INIT_BORDER;
        camera.position.y = (yi + yf)/2;
    }

    /*
     * Moves the camera's x position along the current background line,
     * according to the specified speed.
     * Delta indicates the time between frames, in seconds.
     */
    private boolean scrollLine(float delta, float speed) {
        camera.translate(speed*delta, 0);
        if (lineCount != lineDivisors.length - 1)
            return (camera.position.x + rx >= COMIC_END_BORDER);
        else
            return (camera.position.x + rx >= Constant.GAME_WIDTH);
    }

    /*
     * Calculate unzoom speed according to camera's bounds, zoom
     * and the unzoom rate.
     */
    private void calculateUnzoomSpeed() {
        unzoomVx = (Constant.GAME_WIDTH/2 - rx)/(1 - camera.zoom) * unzoomRate;
        unzoomVy = (Constant.GAME_HEIGHT/2 - ry)/(1 - camera.zoom) * unzoomRate;
    }

    /*
     * Set new delay, in seconds, for updating the camera.
     */
    private void setNewDelay(float delay) {
        this.delay = delay;
        timePassed = 0;
    }
}
