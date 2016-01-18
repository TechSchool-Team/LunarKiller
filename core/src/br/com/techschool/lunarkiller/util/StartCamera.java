package br.com.techschool.lunarkiller.util;

import com.badlogic.gdx.graphics.OrthographicCamera;

/*
 * Contains camera effects used on start screen.
 */
public class StartCamera {

 // Contains all effects used on this camera
    private enum Phase {
        UPPER_LEFT_ZOOM, SCROLL, UNZOOM, FIXED
    };

    // Camera used to zoom and scroll through the background
    public OrthographicCamera camera;

    // Constant speed at which the camera scrolls
    private final float scrollSpeed = 100.0f;

    // Initial zoom used on camera for scrolling effect
    private final float initialZoom = 0.25f;

    // How much the camera is later unzoomed per frame
    private final float unzoomRate = 0.002f;

    // Distance between zoomed camera's center and its horizontal/vertical bounds.
    // Formulas were calculated on paper.
    private final float rx = initialZoom*Constant.GAME_WIDTH/2;
    private final float ry = initialZoom*Constant.GAME_HEIGHT/2;

    // x/y speed at which the camera moves back to the background's center
    // while unzooming.
    private final float unzoomVx = (Constant.GAME_WIDTH/2 - rx)/(1 - initialZoom) * unzoomRate;
    private final float unzoomVy = (Constant.GAME_HEIGHT/2 - ry)/(1 - initialZoom) * unzoomRate;

    // Controls what action the camera is currently doing
    private Phase phase;

    // Time delay between phases
    private float delay;

    // Stores passed time until it reaches the delay variable
    private float timePassed;

    public StartCamera() {
        // Create camera with default resolution
        camera = new OrthographicCamera(Constant.GAME_WIDTH, Constant.GAME_HEIGHT);

        // Configure initial phase
        phase = Phase.UPPER_LEFT_ZOOM;

        // No delay initially
        delay = timePassed = 0;
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
            case FIXED:
                // Fix camera on center of background
                camera.zoom = 1.00f;
                camera.position.x = Constant.GAME_WIDTH/2;
                camera.position.y = Constant.GAME_HEIGHT/2;
                break;

            case UNZOOM:
                camera.zoom += unzoomRate;
                camera.translate(-unzoomVx, unzoomVy);
                if (camera.zoom >= 1.00f) {
                    phase = Phase.FIXED;
                }
                break;

            case SCROLL:
                camera.translate(scrollSpeed*delta, 0);
                if (camera.position.x + rx >= Constant.GAME_WIDTH) {
                    // Camera rewinds to start of x axis, but lowers an amount
                    camera.position.x = rx;
                    camera.position.y -= 2*ry;
                    if (camera.position.y -ry < 0) {
                        // End of y axis; return camera to lower right position
                        camera.position.x = Constant.GAME_WIDTH - rx;
                        camera.position.y += 2*ry;
                        // Configure delay and advance phase
                        delay = 3;
                        timePassed = 0;
                        phase = Phase.UNZOOM;
                    }
                }
                break;

            case UPPER_LEFT_ZOOM:
                // Zoom and move camera to upper left border of the background
                camera.zoom = initialZoom;
                camera.position.x = rx;
                camera.position.y = Constant.GAME_HEIGHT - ry;
                // Configure delay and advance phase
                delay = 2;
                timePassed = 0;
                phase = Phase.SCROLL;
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
}
