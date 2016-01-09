package br.com.techschool.lunarkiller.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

/*
 * Shader that activates normals on models.
 */
public class NormalShader implements Shader {

    // Compiled shader program
    ShaderProgram shaderProgram;

    // Camera used during rendering
    Camera camera;

    // Object being drawn
    RenderContext context;

    @Override
    public void init() {
        // Read shader files
        String vertex   = Gdx.files.internal("shaders/normal.verter.shader").toString();
        String fragment = Gdx.files.internal("shaders/normal.fragment.shader").toString();

        // Create and try to compile program
        shaderProgram = new ShaderProgram(vertex, fragment);
        if (!shaderProgram.isCompiled()) {
            System.out.println("Vixe...");
            throw new GdxRuntimeException(shaderProgram.getLog());
        }
    }

    @Override
    public void begin(Camera camera, RenderContext context) {
        this.camera  = camera;
        this.context = context;
        shaderProgram.begin();
        shaderProgram.setUniformMatrix("u_projViewTrans", camera.combined);
    }

    @Override
    public void render(Renderable renderable) {
        shaderProgram.setUniformMatrix("u_worldTrans", renderable.worldTransform);

        // Set sampler2D uniform for normals
        int textureIndex = ((TextureAttribute) renderable.material.get(TextureAttribute.Normal)).uvIndex;
        shaderProgram.setUniformi("u_normalTexture", textureIndex);

        renderable.meshPart.render(shaderProgram);
    }

    @Override
    public void end() {
        shaderProgram.end();
    }

    @Override
    public int compareTo(Shader other) {
        // TODO: See what this does
        return 0;
    }

    @Override
    public boolean canRender(Renderable instance) {
        // TODO: See what this does
        return false;
    }

    @Override
    public void dispose() {
        shaderProgram.dispose();
    }
}
