package br.com.techschool.lunarkiller.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
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
                          "models/scenario/moon.g3db"));
        moon = new ModelInstance(moonModel);
        moon.transform.setToTranslation(0.0f, 0.0f, 0.0f);
        moon.calculateTransforms();
    }
}
