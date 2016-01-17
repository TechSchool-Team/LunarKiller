package br.com.techschool.lunarkiller.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.UBJsonReader;

public class Boss {

    // Instance of the boss's model
    public ModelInstance boss;

    // Character's states
    public static final byte IDLE=0;
    public static final byte DAMAGED=1;
    public static final byte DYING=2;
    public static final byte ATTACK=3;

    // Boss animation states
    private GameObject states[]; 

    // Indicates current state
    private byte state;

    /*
     * Initializes all models related to the scenario.
     */
    public Boss() {
        states = new GameObject[4];
        G3dModelLoader loader = new G3dModelLoader(new UBJsonReader());
        states[0] = new GameObject(loader.loadModel(Gdx.files.internal("models/boss/idle.g3db")));
        states[1] = new GameObject(loader.loadModel(Gdx.files.internal("models/boss/damaged.g3db")));
        states[2] = new GameObject(loader.loadModel(Gdx.files.internal("models/boss/dying.g3db")));
        states[3] = new GameObject(loader.loadModel(Gdx.files.internal("models/boss/attack.g3db")));
        state = DAMAGED;

        Model bossModel = loader.loadModel(Gdx.files.internal("models/boss/boss.g3db"));
        boss = states[state];

        // Initial transformations
        translateBoss(new Vector3(-2.5f, 6.25f, -10.5f));
        rotateBoss(Vector3.X, 10.0f);
        scaleBoss(new Vector3(0.13f, 0.13f, 0.13f));
    }

    public void update(float delta) {
        states[state].update(delta);
        boss = states[state];
    }

    public void translateBoss(Vector3 pos){
        for(GameObject go:states){
            go.transform.translate(pos);
        }
        boss.transform.translate(pos);
    }
    
    public void rotateBoss(Vector3 axis, float rot){
        for(GameObject go:states){
            go.transform.rotate(axis, (rot));
        }
        boss.transform.rotate(axis, (rot));
    }

    public void scaleBoss(Vector3 scale){
        for(GameObject go:states){
            go.transform.scale(scale.x, scale.y, scale.z);
        }
        boss.transform.scale(scale.x, scale.y, scale.z);
    }
}
