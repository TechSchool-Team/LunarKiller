package br.com.techschool.lunarkiller.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Quaternion;
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

    // Boss fixed position
    private final Vector3 position = new Vector3(-2.5f, 6.25f, -10.5f);
    
    // Boss rotation
    private float angle;
    private float desiredAngle;
    private float deltaAngle;
    
    // Attack system
    public ModelInstance handPoint;
    public Vector3		 handPosition;
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
        state = ATTACK;

        boss = states[state];
        angle = 0;
        
        ModelBuilder modelBuilder = new ModelBuilder();
        handPoint = new ModelInstance(modelBuilder.createSphere(5,5, 5, 12, 12, new Material(ColorAttribute.createDiffuse(Color.YELLOW)), Usage.Position | Usage.ColorUnpacked | Usage.Normal));
        handPosition = new Vector3();

        // Initial transformations
        translateBoss(position);
        rotateBoss(Vector3.X, 10.0f);
        scaleBoss(new Vector3(0.13f, 0.13f, 0.13f));
    }

    public void update(float delta, Character player) {
        states[state].update(delta);
        boss = states[state];
        
        //desiredAngle = (float) Math.atan2(position.z - player.getPosition().z, position.x - player.getPosition().x);
        //deltaAngle = angle + desiredAngle;
        //angle += deltaAngle;
        
        
        //setRotation(Vector3.Y, 0);
        
        // Set the position that the boss' hands hit
        handPosition.x = (float)(position.x + 12 * Math.cos(Math.toRadians(angle + 170)));
        handPosition.y = position.y + 15;
        handPosition.z = (float)(position.z + 12 * Math.sin(Math.toRadians(angle + 170)));
		
		//handPoint.transform.setTranslation(handPosition);
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
    
    public void setRotation(Vector3 axis, float rot){
    	for(GameObject go:states){
    		go.transform.set(position, new Quaternion(axis, rot));
    	}
    	boss.transform.set(position,  new Quaternion(axis, rot));
    }

    public void scaleBoss(Vector3 scale){
        for(GameObject go:states){
            go.transform.scale(scale.x, scale.y, scale.z);
        }
        boss.transform.scale(scale.x, scale.y, scale.z);
    }
}
