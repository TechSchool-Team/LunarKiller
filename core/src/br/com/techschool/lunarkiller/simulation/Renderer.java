package br.com.techschool.lunarkiller.simulation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;

import br.com.techschool.lunarkiller.model.NormalShader;
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

    // Renders objects on the environment
    private RenderContext renderContext;

    // Used as a temporary object for rendering
    private Renderable tempRenderable;

    // Static camera used during game
    private PerspectiveCamera camera;

    // Directional light aimed at the Moon
    private DirectionalLight dirLight;

    // Shader that activates normals
    private NormalShader normalShader;

    // Debug camera movement
    private CameraInputController control;

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

        // Render context just handles some gl calls
        // Texture binder gives a nice way to manage binding of textures
        renderContext = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.WEIGHTED));
        tempRenderable = new Renderable();

        // Initialize shader
        ShaderProgram.pedantic = false;
        normalShader = new NormalShader();
        normalShader.init();

        // Create and configure camera
        camera = new PerspectiveCamera(67.0f,
                                       Gdx.graphics.getWidth(),
                                       Gdx.graphics.getHeight());
        camera.near = 0.1f;
        camera.far = 3000f;
        camera.position.set(0.0f, 10.0f, 30.0f);
        camera.lookAt(0.0f, 0.0f, 0.0f);
        camera.update();

        // DEBUG
        control = new CameraInputController(camera);
        Gdx.input.setInputProcessor(control);
    }

    /*
     * Draws objects occurring on the game loop screen.
     */
    public void draw(float delta) {
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

        // Draw models
        renderContext.begin();
        normalShader.begin(camera, renderContext);
        normalShader.render(gameAction.scenario.moon.getRenderable(tempRenderable), environment);
        normalShader.end();
        renderContext.end();
    }

    /*
     * Frees space used by this Renderer.
     */
    public void dispose() {
        spriteBatch.dispose();
        normalShader.dispose();
    }
}
