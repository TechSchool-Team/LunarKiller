package br.com.techschool.lunarkiller.model;

import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.UBJsonReader;

import br.com.techschool.lunarkiller.simulation.GameAction;


public class Boss {
    // Character's states
    public static final byte IDLE=0;
    public static final byte DAMAGED=1;
    public static final byte DYING=2;
    public static final byte ATTACK=3;
    
 // Indicates current state
    private byte bossState;

    // Boss animation states
    private GameObject bossStates[];

    // Boss fixed position
    //public final Vector3 bossPosition = new Vector3(-2.5f, 6.25f, -10.5f);
    public final Vector3 bossPosition = new Vector3(-5.0f, 12.5f, -21.0f);
    //public final Vector3 bossPosition = new Vector3(-1.25f, 3.125f, -5.25f);
    //public final Vector3 position = new Vector3(0,0,0);
    
    // Boss rotation
    private float angle;
    
    
    // Attack system
    public ModelInstance handPoint;
    public Vector3		 handPosition;
    private GameAction 	 ga;
    
    public boolean damaged;
    public float life;
    public float deltaCount;
    public float bossAnimDelay;
    private boolean meteoreIsGoing;
    
    //private Music death;
    /*
     * Initializes all models related to the scenario.
     */
    public Boss(GameAction _ga) {
    	// Initialize boss' states
    	bossStates = new GameObject[4];
        G3dModelLoader loader = new G3dModelLoader(new UBJsonReader());
        bossStates[0] = new GameObject(loader.loadModel(Gdx.files.internal("models/boss/idle.g3db")));
        bossStates[1] = new GameObject(loader.loadModel(Gdx.files.internal("models/boss/damaged.g3db")));
        bossStates[2] = new GameObject(loader.loadModel(Gdx.files.internal("models/boss/dying.g3db")));
        bossStates[3] = new GameObject(loader.loadModel(Gdx.files.internal("models/boss/attack.g3db")));
        bossState = IDLE;
        
        // Default properties
        angle = 0;
        damaged = false;
        life = 60000;
        bossAnimDelay = bossStates[bossState].animations.first().duration;
        ga = _ga;
        
        // The position that boss' hand hits the meteor
        ModelBuilder modelBuilder = new ModelBuilder();
        handPoint = new ModelInstance(modelBuilder.createSphere(5,5, 5, 12, 12, new Material(ColorAttribute.createDiffuse(Color.YELLOW)), Usage.Position | Usage.ColorUnpacked | Usage.Normal));
        handPosition = new Vector3();

        // Initial transformations
        //rotateBoss(Vector3.X, 10.0f);
        //translateBoss(bossPosition);
        //boss.transform.set(bossPosition,  new Quaternion(Vector3.X, 10.0f));
        
        setToTranslationAndRotation(bossPosition, Vector3.X, 10.0f);
        scaleBoss(new Vector3(0.0169f, 0.0169f, 0.0169f));
        
        //death = Gdx.audio.newMusic(Gdx.files.internal("sound/voices/death2.mp3"));
    }

    public void update(float delta, Character player) {
    	bossStates[bossState].update(delta);
        deltaCount+=delta;
        
        if(bossAnimDelay>0){
        	bossAnimDelay-=delta;
        }
        //desiredAngle = (float) Math.atan2(position.z - player.getPosition().z, position.x - player.getPosition().x);
        //deltaAngle = angle + desiredAngle;
        //angle += deltaAngle;
        
        
        //setRotation(Vector3.Y, 0);
        
        // Set the position that the boss' hands hit
        handPosition.x = (float)(bossPosition.x + 12 * Math.cos(Math.toRadians(angle + 170)));
        handPosition.y = bossPosition.y + 15;
        handPosition.z = (float)(bossPosition.z + 12 * Math.sin(Math.toRadians(angle + 170)));
		
		//handPoint.transform.setTranslation(handPosition);
        
        // State Machine
        if(life <= 0){
        	bossState = DYING;
        }
        else if(bossState == IDLE){
        	meteoreIsGoing = false;
        	if(deltaCount >= 3 && bossAnimDelay <= 0){
        		deltaCount = 0;
        		attack();
        	}
        }
        else{
        	if(bossState == ATTACK && bossStates[bossState].isAnimationFinished() && !meteoreIsGoing){
        		ga.callMeteor();
        		meteoreIsGoing = true;
        	}
        	if(bossAnimDelay <= 0){
        		idle();
        	}
        }
        
        
        //boss.transform.set(bossPosition,  new Quaternion(Vector3.X, 10.0f));
        //translateBoss(position);
    }

    public void idle(){
    	if(damaged){
    		bossState = DAMAGED;
    		damaged = false;
    		bossAnimDelay = bossStates[bossState].animations.first().duration;
    	}
    	else{
    		bossState = IDLE;
    	}
    	
    }
    
    public void hit(){
    	damaged = true;
    	if(bossState == IDLE){
    		bossState = DAMAGED;
    		damaged = false;
    		bossAnimDelay = bossStates[bossState].animations.first().duration;
    	}
    }
    
    public void die(){
    	bossState = DYING;
    	//death.play();
    }
    
    public void attack(){
    	bossState = ATTACK;
    	bossAnimDelay = bossStates[bossState].animations.first().duration;
    }
    
    public void setToTranslationAndRotation(Vector3 pos, Vector3 axis, float rot){
    	for(GameObject go:bossStates){
    		go.transform.set(pos, new Quaternion(axis, rot));
    	}
    }
    
    public void translateBoss(Vector3 pos){
        for(GameObject go:bossStates){
            go.transform.translate(pos);
        }
    }
    
    public void rotateBoss(Vector3 axis, float rot){
        for(GameObject go:bossStates){
            go.transform.rotate(axis, (rot));
        }
    }
    
    public void setRotation(Vector3 axis, float rot){
    	for(GameObject go:bossStates){
    		go.transform.set(bossPosition, new Quaternion(axis, rot));
    	}
    }

    public void scaleBoss(Vector3 scale){
        for(GameObject go:bossStates){
            go.transform.scale(scale.x, scale.y, scale.z);
        }
    }
    
    public int getState(){
    	return bossState;
    }
    
    public GameObject getCurrent(){
    	return bossStates[bossState];
    }
}
