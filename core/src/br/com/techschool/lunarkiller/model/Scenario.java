package br.com.techschool.lunarkiller.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.utils.UBJsonReader;

/*
 * Contains the scenario's model and anything related to it.
 */
public class Scenario {

    // Instance of the moon's model
    public ModelInstance moon;

    /*
     * Initializes all models related to the scenario.
     */
    public Scenario() {
        G3dModelLoader loader = new G3dModelLoader(new UBJsonReader());

        // Load and store main scenario model from file
        Model moonModel = loader.loadModel(Gdx.files.internal(
                          "models/moon/moon.g3db"));

        moon = new ModelInstance(moonModel);

        // Light of the material itself
        moon.materials.get(0).set(ColorAttribute.createDiffuse(Color.WHITE));

        // Highlight color
        moon.materials.get(0).set(ColorAttribute.createSpecular(Color.GRAY));
    }
}
