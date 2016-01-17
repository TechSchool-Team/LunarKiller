package br.com.techschool.lunarkiller.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.utils.UBJsonReader;

public class Boss {

    // Instance of the boss's model
    public ModelInstance boss;

    /*
     * Initializes all models related to the scenario.
     */
    public Boss() {
        G3dModelLoader loader = new G3dModelLoader(new UBJsonReader());

        // Load and store main scenario model from file
        Model bossModel = loader.loadModel(Gdx.files.internal(
                          "models/boss/boss.g3db"));

        boss = new ModelInstance(bossModel);

        // Initial transformations
        boss.transform.translate(-5.0f, 11.25f, -17.5f);
        boss.transform.rotate(1.0f, 0.0f, 0.0f, 20.0f);
        boss.transform.scale(1.8f, 1.8f, 1.8f);
    }
}
