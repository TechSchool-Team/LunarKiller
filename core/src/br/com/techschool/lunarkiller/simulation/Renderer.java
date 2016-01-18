package br.com.techschool.lunarkiller.simulation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import br.com.techschool.lunarkiller.model.Bullet;
import br.com.techschool.lunarkiller.util.Constant;

/*
 * Responsible for drawing objects on the game loop screen.
 */
public class Renderer {

    // Contains objects to be rendered
    private GameAction gameAction;

    // Manipulates the screen's background image
    private Texture background;

    // Responsible for drawing 2D layer
    private SpriteBatch spriteBatch;

    // Matrix related to the standard orthogonal plane
    private Matrix4 viewMatrix;

    // Matrix that accumulates transformation coefficients
    private Matrix4 tranMatrix;

    // Where all models are rendered
    private Environment environment;

    // Used for drawing with normal shader
    private ModelBatch modelBatch;

    // Custom camera used during game
    private LunarCamera camera;
    
    // Debug camera
    private PerspectiveCamera debugCamera;

    // Directional light aimed at the Moon
    private DirectionalLight dirLight;

    // Debug camera movement
    private CameraInputController control;

    private int cameraNum;
    /*
     * Creates a Renderer object, initializing objects to be drawn
     * on the screen.
     */
    public Renderer(GameAction gameAction) {
        this.gameAction = gameAction;

        // Configure background
        background = new Texture(Gdx.files.internal("backgrounds/space.jpg"));

        spriteBatch = new SpriteBatch();
        viewMatrix = new Matrix4();
        tranMatrix = new Matrix4();

        // Create and set light on environment
        environment = new Environment();
        dirLight = new DirectionalLight();
        dirLight.setColor(0.4f, 0.4f, 0.4f, 1.0f);
        // Direction is diagonally down from viewer side
        dirLight.setDirection(0.0f, -2.0f, -2.0f);
        environment.add(dirLight);

        // Initialize modelBatch (with shaders)
        modelBatch = new ModelBatch(Gdx.files.internal("shaders/xoppa.vertex.shader"),
                                    Gdx.files.internal("shaders/xoppa.fragment.shader"));

        // Create and configure camera
        cameraNum = 1;
        debugCamera = new PerspectiveCamera(67.0f,
                                 Gdx.graphics.getWidth(),
                                 Gdx.graphics.getHeight());
        debugCamera.near = 0.1f;
        debugCamera.far = 1000f;
        debugCamera.position.set(0, 15.0f, 12.5f);
        debugCamera.lookAt(gameAction.character.origin);
        debugCamera.update();
        
        camera = new LunarCamera(67.0f,
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight(),
                gameAction.character);
        camera.offset = new Vector3(-0, 1.5f, 0.75f);
        camera.update();
        
        // DEBUG
        control = new CameraInputController(camera);
        Gdx.input.setInputProcessor(control);
        
        // Particles
        gameAction.character.pointSpriteBatch.setCamera(camera);
    }

    /*
     * Draws objects occurring on the game loop screen.
     */
    public void draw(float delta) {
        camera.update();

        // Clear screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        // Define size of screen
        Gdx.gl.glViewport(0, 0,
                          Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Define area that can be drawn
        viewMatrix.setToOrtho2D(0, 0, Constant.GAME_WIDTH, Constant.GAME_HEIGHT);

        // Configure drawing area
        spriteBatch.setProjectionMatrix(viewMatrix);
        spriteBatch.setTransformMatrix(tranMatrix);

        // Draw background
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0);
        spriteBatch.end();

        // Prepare camera
        if(cameraNum == 0) {
            modelBatch.begin(debugCamera);
        }
        else{
            modelBatch.begin(camera);
        }

        // Draw models
        modelBatch.render(gameAction.scenario.moon, environment);
        modelBatch.render(gameAction.boss.boss, environment);
        modelBatch.render(gameAction.character.player, environment);
        // Draw Debug positions
        // modelBatch.render(gameAction.character.gunPoint, environment);
        //modelBatch.render(gameAction.boss.handPoint, environment);
        
        // Draw bullets
        for (Bullet shot : gameAction.bullets) {
            modelBatch.render(shot.getMesh(), environment);
        }

        // Prepare and draw particles
        gameAction.character.particleSystem.update();
        gameAction.character.particleSystem.begin();
        gameAction.character.particleSystem.draw();
        gameAction.character.particleSystem.end();
        modelBatch.render(gameAction.character.particleSystem);

        modelBatch.end();
    }

    public void changeCamera() {
        if(cameraNum == 0) {
            cameraNum++;
        }
        else {
            cameraNum = 0;
        }
    }

    /*
     * Frees space used by this Renderer.
     */
    public void dispose() {
        spriteBatch.dispose();
        modelBatch.dispose();
    }
}
