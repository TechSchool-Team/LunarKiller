package br.com.techschool.lunarkiller.screen.start;

import com.badlogic.gdx.graphics.OrthographicCamera;

import br.com.techschool.lunarkiller.util.Constant;

/*
 * Contains camera effects used on start screen.
 */
public class StartCamera {

    // Contains all effects used on this camera
    private enum Phase {
        BEGIN, PREPARE_UPPER_LEFT_ZOOM, UPPER_LEFT_ZOOM,
        SCROLL, UPDATE_SCROLL, PREPARE_UNZOOM, UNZOOM, FIXED
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

    // Final x/y position used when moving the camera
    private float finalX;
    private float finalY;

    // How much the camera is unzoomed per frame
    private float zoomRate;

    // x coordinates where a line in a comic ends.
    // These values are relative to the background itself!
    private final float[] lineDivisors = new float[] {1060, 699, 343, 0};

    // Speed at which the camera scrolls for each line.
    // Values were calculated through many tests!
    private final float[] lineSpeed = new float[] {51, 46, 52, 47};

    // Indicates which line the camera is currently showing
    int lineCount;

    // Controls what action the camera is currently doing
    private Phase phase;

    // Time delay between phases
    private float delay;

    // Stores passed time until it reaches the delay variable
    private float timePassed;

    // Start and end of comic on the x axis
    private final float COMIC_INIT_BORDER = 464;
    private final float COMIC_END_BORDER  = 1664;

    /*
     * Initializes camera attributes and phase.
     */
    public StartCamera() {
        // Create camera with default resolution
        camera = new OrthographicCamera(Constant.COMIC_WIDTH, Constant.COMIC_HEIGHT);
        lineCount = 0;

        // Configure initial phase (no delay)
        phase = Phase.BEGIN;
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
            case BEGIN:
                // Set initial values
                camera.zoom = 1.00f;
                camera.position.x = Constant.COMIC_WIDTH/2;
                camera.position.y = Constant.COMIC_HEIGHT/2;
                setNewDelay(1.0f);
                phase = Phase.PREPARE_UPPER_LEFT_ZOOM;
                break;

            case PREPARE_UPPER_LEFT_ZOOM:
                // Configure camera to obtain zoom and final position on middle
                // of first line
                configCamera(lineDivisors[lineCount], Constant.COMIC_HEIGHT);
                float finalZoom = camera.zoom;

                // Set initial values again
                camera.zoom = 1.00f;
                camera.position.x = Constant.COMIC_WIDTH/2;
                camera.position.y = Constant.COMIC_HEIGHT/2;

                calculateZoomSpeed(COMIC_INIT_BORDER + rx, Constant.COMIC_HEIGHT - ry,
                                   finalZoom, 0.0019f);
                phase = Phase.UPPER_LEFT_ZOOM;
                break;

            case UPPER_LEFT_ZOOM:
                // Zoom onto the upper left corner of the comic
                if (zoomAndMove()) {
                    // Zooming is finished!
                    setNewDelay(1.0f);
                    phase = Phase.SCROLL;
                }
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
                        setNewDelay(3.3f);
                        phase = Phase.PREPARE_UNZOOM;
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

            case PREPARE_UNZOOM:
                // Configure camera speed for an unzoom effect
                calculateZoomSpeed(Constant.COMIC_WIDTH/2, Constant.COMIC_HEIGHT/2,
                                   1.00f, 0.0019f);
                phase = Phase.UNZOOM;
                break;

            case UNZOOM:
                // Slowly unzoom and move camera to the
                // center of the background
                if (zoomAndMove()) {
                    // Unzooming is finished!
                    setNewDelay(1.0f);
                    phase = Phase.FIXED;
                }
                break;

            case FIXED:
                // Fix camera on center of background
                camera.viewportWidth  = Constant.GAME_WIDTH;
                camera.viewportHeight = Constant.GAME_HEIGHT;
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
        phase = Phase.BEGIN;
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
        camera.zoom = (yi - yf)/Constant.COMIC_HEIGHT;

        // Recalculate border distances
        rx = camera.zoom*Constant.COMIC_WIDTH/2;
        ry = camera.zoom*Constant.COMIC_HEIGHT/2;

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
        if (camera.position.x + rx >= COMIC_END_BORDER) {
            camera.position.x = COMIC_END_BORDER - rx;
            return true;
        }

        return false;
    }

    /*
     * Calculate (un)zoom speed according to camera's bounds, current zoom
     * and the (un)zoom rate. 'zoomSpeed' must be positive!
     */
    private void calculateZoomSpeed(float xf, float yf, float zoomFinal, float zoomSpeed) {
        // Prevent zoomSpeed from being negative
        zoomSpeed = Math.abs(zoomSpeed);

        // Update final position attributes
        this.finalX = xf;
        this.finalY = yf;

        float deltaX = xf - camera.position.x;
        float deltaY = yf - camera.position.y;

        // Get absolute value for zoom variables; what determines
        // speed direction are initial and final positions
        float deltaZoom = zoomFinal - camera.zoom;

        if (deltaZoom != 0) {
            // zoomRate gains same sign as deltaZoom
            this.zoomRate = zoomSpeed * (Math.abs(deltaZoom)/deltaZoom);

            // Speed is uniform and is related to zoom variables
            float zoomTime = Math.abs(deltaZoom)/zoomSpeed;

            unzoomVx = deltaX/zoomTime;
            unzoomVy = deltaY/zoomTime;
        }
        else {
            // No variation in zoom; zoomSpeed is considered a uniform speed
            this.zoomRate = Math.abs(zoomSpeed);
            unzoomVx = deltaX/zoomSpeed;
            unzoomVy = deltaY/zoomSpeed;
        }
    }

    /*
     * Moves camera according to the (un)zoom speed previously calculated.
     * Returns true if the camera has reached its final position.
     */
    private boolean zoomAndMove() {
        // Move and (un)zoom camera
        camera.zoom += zoomRate;
        camera.translate(unzoomVx, unzoomVy);

        // Check if camera reached its final destination
        if ((unzoomVx >= 0 && camera.position.x >= finalX) ||
           (unzoomVx < 0 && camera.position.x <= finalX)) {
            if ((unzoomVy >= 0 && camera.position.y >= finalY) ||
               (unzoomVy < 0 && camera.position.y <= finalY)) {
                camera.position.x = finalX;
                camera.position.y = finalY;
                return true;
            }
        }

        return false;
    }

    /*
     * Set new delay, in seconds, for updating the camera.
     */
    private void setNewDelay(float delay) {
        this.delay = delay;
        timePassed = 0;
    }
}
