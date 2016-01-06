package br.com.techschool.lunarkiller.simulation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

/*
 * Responsible for drawing objects on the game loop screen.
 */
public class Renderer {

    // Contains objects to be rendered
    private GameAction gameAction;

    // Where all models are rendered
    private Environment environment;

    // Renders objects on the environment
    private ModelBatch modelBatch;

    // Static camera used during game
    private PerspectiveCamera camera;

    /*
     * Creates a Renderer object, initializing objects to be drawn
     * on the screen.
     */
    public Renderer(GameAction gameAction) {
        this.gameAction = gameAction;

        // Create and set ambient light on environment
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, Color.WHITE));
        modelBatch = new ModelBatch();

        // Create and configure camera
        camera = new PerspectiveCamera(67.0f,
                                       Gdx.graphics.getWidth(),
                                       Gdx.graphics.getHeight());
        camera.near = 0.1f;
        camera.far = 1000f;
        camera.position.set(-10.0f, 0.0f, 30.0f);
        camera.lookAt(0.0f, 0.0f, 0.0f);
        camera.update();
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

        // DEBUG: Draws green cube on (0, 0, 0)
        ModelBuilder modelBuilder = new ModelBuilder();
        Model model = modelBuilder.createBox(5f, 5f, 5f,
            new Material(ColorAttribute.createDiffuse(Color.GREEN)),
            Usage.Position | Usage.Normal);
        ModelInstance instance = new ModelInstance(model);

        // Draw models
        modelBatch.begin(camera);
        modelBatch.render(gameAction.scenario.moon, environment);
        modelBatch.render(instance, environment);
        modelBatch.end();

        // DEBUG: Moves camera on x and z axis
        if (Gdx.input.isKeyPressed(Keys.A))
            camera.translate(-1.0f, 0.0f, 0.0f);
        if (Gdx.input.isKeyPressed(Keys.D))
            camera.translate(1.0f, 0.0f, 0.0f);
        if (Gdx.input.isKeyPressed(Keys.W))
            camera.translate(0.0f, 0.0f, -1.0f);
        if (Gdx.input.isKeyPressed(Keys.S))
            camera.translate(0.0f, 0.0f, 1.0f);

        camera.update();
    }

    /*
     * Frees space used by model renderers.
     */
    public void dispose() {
        modelBatch.dispose();
    }
}
