package br.com.techschool.lunarkiller.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.DirectionalLightsAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.utils.GdxRuntimeException;

/*
 * Shader that activates normals on models.
 */
public class NormalShader implements Shader {

    // Compiled shader program
    private ShaderProgram shaderProgram;

    // Object being drawn
    private RenderContext context;

    // Used for calculating normals
    private Matrix3 tempMatrix = new Matrix3();

    @Override
    public void init() {
        // Create and try to compile program
        shaderProgram = new ShaderProgram(Gdx.files.internal("shaders/normalShader.vert"),
                                          Gdx.files.internal("shaders/normalShader.frag"));
        if (!shaderProgram.isCompiled()) {
            System.out.println("Vixe...");
            throw new GdxRuntimeException(shaderProgram.getLog());
        }
    }

    @Override
    public void begin(Camera camera, RenderContext context) {
        this.context = context;
        shaderProgram.begin();

        // Set context depth testing for depth triangles
        context.setDepthTest(GL20.GL_LEQUAL);
        // Which side of triangle should be culled
        context.setCullFace(GL20.GL_BACK);

        shaderProgram.setUniformMatrix("u_projViewTrans", camera.combined);
    }

    @Override
    public void render(Renderable renderable) {
        // Set the shader uniform to the modelInstance transform
        shaderProgram.setUniformMatrix("u_worldTrans", renderable.worldTransform);
        shaderProgram.setUniformMatrix("u_normalMatrix", tempMatrix.set(renderable.worldTransform).inv().transpose());

        // Grab the diffuse texture from the Material object
        Texture diffuse = ((TextureAttribute)renderable.material.get(TextureAttribute.Diffuse)).textureDescription.texture;
        Texture normal = ((TextureAttribute)renderable.material.get(TextureAttribute.Normal)).textureDescription.texture;

        // Bind the texture with TextureBinder, so it returns the unit
        int diffuseTextureUnit = context.textureBinder.bind(diffuse);
        int normalTextureUnit = context.textureBinder.bind(normal);

        // Pass correct diffuse texture unit
        shaderProgram.setUniformi("u_diffuseTexture", diffuseTextureUnit);
        shaderProgram.setUniformi("u_normalTexture", normalTextureUnit);
        renderable.meshPart.render(shaderProgram);
    }

    /*
     * Renders an object and adds a directional light on the
     * specified Environment.
     */
    public void render(Renderable renderable, Environment environment) {
        // Add additional uniforms from the environment
        // Only one directional light exists! :P
        DirectionalLight directionalLight = ((DirectionalLightsAttribute) environment.get(DirectionalLightsAttribute.Type)).lights.first();

        shaderProgram.setUniformf("u_directionalColour", directionalLight.color.r,
                                                         directionalLight.color.g,
                                                         directionalLight.color.b);
        shaderProgram.setUniformf("u_directionalDirection", directionalLight.direction.x,
                                                            directionalLight.direction.y,
                                                            directionalLight.direction.z);

        // Call render method
        render(renderable);
    }

    @Override
    public void end() {
        shaderProgram.end();
    }

    @Override
    public int compareTo(Shader other) {
        return 0;
    }

    @Override
    public boolean canRender(Renderable instance) {
        return true;
    }

    @Override
    public void dispose() {
        shaderProgram.dispose();
    }
}
