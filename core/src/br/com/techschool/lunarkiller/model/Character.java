package br.com.techschool.lunarkiller.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffectLoader;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
import com.badlogic.gdx.graphics.g3d.particles.batches.PointSpriteParticleBatch;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.UBJsonReader;

import br.com.techschool.lunarkiller.simulation.GameAction;
import br.com.techschool.lunarkiller.util.Commands;

/*
 * Contains the character's model and anything related to it.
 */
public class Character {
	// Character's states
	public static final byte IDLE=0;
	public static final byte WALK=0;
	public static final byte SHOT=2;
	public static final byte SHOT_STRONG=3;
	public static final byte DYING=1;
	public static final float PI = 3.1415f;
	private GameObject	states[]; 
	private byte state;
	// Instance of the character's model
    public ModelInstance player;
    
    private Vector3 	position;
    public Vector3		origin;
    public float		height;
    public float 		radious;
    public float		angle;
    private float		initialAngle;
    
    private Vector3 gunPosition;
    public float	reloadTime;
    
    public boolean DEBUG = false;
    public ModelInstance gunPoint;
    private float masterShotCount;
    private boolean shoudShot;
    
    public float animDelay;
    private GameAction ga;
    
    // Particles
    public ParticleSystem particleSystem;
    public PointSpriteParticleBatch pointSpriteBatch;
    public AssetManager assets;
    
    /*
     * 
     */
    public Character(GameAction _ga) {
    	// Initialize player's states
    	states = new GameObject[4];
    	G3dModelLoader loader = new G3dModelLoader(new UBJsonReader());
    	states[0] = new GameObject(loader.loadModel(Gdx.files.internal("models/character/idle.g3db")));
    	states[1] = new GameObject(loader.loadModel(Gdx.files.internal("models/character/dying.g3db")));
    	states[2] = new GameObject(loader.loadModel(Gdx.files.internal("models/character/shooting.g3db")));
    	states[3] = new GameObject(loader.loadModel(Gdx.files.internal("models/character/shooting2.g3db")));
    	state = IDLE;
    	
    	// Set player's position
    	origin = new Vector3(-5,12.25f,-17.5f);
    	height = 12.25f; //12.25f;
    	radious = 20;//5;
    	angle = 0;//PI/2
    	initialAngle = 90;
    	masterShotCount = 0;
    	ga = _ga;
    	shoudShot = false;
    	
    	position = new Vector3(0,0,0);
    	states[state].transform.setToTranslation(position);
    	animDelay = states[state].animations.first().duration;;
        // Load and store the players's model from file
    	if(DEBUG){
    		ModelBuilder modelBuilder = new ModelBuilder();
    		player = new ModelInstance(modelBuilder.createCapsule(0.3333f, 1.25f, 24, new Material(ColorAttribute.createDiffuse(Color.YELLOW)), Usage.Position | Usage.ColorUnpacked));
    	}
    	else{
    		player = states[state];
    		//player = new ModelInstance(loader.loadModel(Gdx.files.internal("models/character/lunarkiller_final.g3db")));
    	}
    	
    	rotatePlayer(180);
    	translatePlayer(new Vector3( 10,height, -5));
    	
    	//Debug gun
    	ModelBuilder modelBuilder = new ModelBuilder();
        gunPoint = new ModelInstance(modelBuilder.createSphere(0.3333f, 0.3333f, 0.3333f, 12, 12, new Material(ColorAttribute.createDiffuse(Color.YELLOW)), Usage.Position | Usage.ColorUnpacked | Usage.Normal));
        gunPosition = new Vector3(0,0,0);
        
        // Create Particles
        pointSpriteBatch = new PointSpriteParticleBatch();
        particleSystem = ParticleSystem.get();
        particleSystem.add(pointSpriteBatch);
        
        // Loading Effects
        assets = new AssetManager();
        loadEffects();
    }
    
    public void loadEffects(){
    	ParticleEffectLoader.ParticleEffectLoadParameter loadParam = new ParticleEffectLoader.ParticleEffectLoadParameter(particleSystem.getBatches());
        ParticleEffectLoader loader = new ParticleEffectLoader(new InternalFileHandleResolver());
        assets.setLoader(ParticleEffect.class, loader);
        assets.load("particles/fireTest.part", ParticleEffect.class, loadParam);
        assets.load("particles/fireTest2.part", ParticleEffect.class, loadParam);
		assets.finishLoading();
    }
    
    
    public void translatePlayer(Vector3 pos){
    	for(GameObject go:states){
    		go.transform.translate(pos);
    	}
    	player.transform.translate(pos);
    }
    
    public void rotatePlayer(float rot){
    	for(GameObject go:states){
			go.transform.rotate(Vector3.Y, (rot));
		}
    	player.transform.rotate(Vector3.Y, (rot));
    }
    
    public GameObject getModel(){
    	return states[state];
    }
    
    public Vector3 getPosition(){
    	position = states[state].transform.getTranslation(position);
    	return this.position;
    }
    
    public void update(float delta){
    	states[state].update(delta);
    	
    	player = states[state];
    	
    	// Move
    	if(Commands.COMANDS_COUNT == 0){
    		if(!prepareShot()){
    			idle();
    		}
    	}
    	else{
    		if(Commands.MOVE_BACK && radious < 22.5){
    			radious += 15f*delta;
    			walk();
        	}
        	if(Commands.MOVE_FRONT && radious > 10){
        		radious -= 15f*delta;
        		walk();
        	}
    		if(Commands.MOVE_LEFT && angle < 41){// Clockwise
    			angle+=+15*delta;
    			walk();
    		}
    		if(Commands.MOVE_RIGHT && angle>-45){ // Anti-Clockwise
    			angle+=-15*delta;
    			walk();
    		}
    		System.out.println(masterShotCount);
    		if(Commands.CMD_SHOT && masterShotCount < 3){
    			shot(delta);
    		}
    		else{
    			prepareShot();
    		}
    	}
    	
    	Vector3 targetPosition = new Vector3(
    			(float)(origin.x + radious*Math.cos(Math.toRadians(angle+initialAngle))),
    			12.5f,
				(float)(origin.z + radious*Math.sin(Math.toRadians(angle+initialAngle))));
		
		for(GameObject go:states){
			go.transform.set(targetPosition, new Quaternion(Vector3.Y, 180-angle));
		}
		player.transform.set(targetPosition, new Quaternion(Vector3.Y, 180-angle));
		
		gunPosition.x = (float) (targetPosition.x + 1f*Math.cos(Math.toRadians(angle-90)));
		gunPosition.y = targetPosition.y + 1.5f;
		gunPosition.z = (float) (targetPosition.z + 1f*Math.sin(Math.toRadians(angle-90)));
		
		gunPoint.transform.setTranslation(gunPosition);
		
		if(reloadTime > 0){
			reloadTime-=delta;
		}
		if(animDelay > 0){
			animDelay-=delta;
			if(shoudShot){//0.56 ~ 0.12
				if(state==SHOT && animDelay < states[SHOT].animations.first().duration - 0.12f){
					shoudShot = false;
					ga.shot(false);
					shotFX(false);
				}
				else if(state==SHOT_STRONG && animDelay < states[SHOT_STRONG].animations.first().duration - 1.06f){
					shoudShot = false;
					ga.shot(true);
					shotFX(true);
				}
			}
		}
		else if(state==SHOT_STRONG){
			idle();
		}
    }
    
    // Add loaded particles to particle systems
    public void shotFX(boolean strong){
    	if(strong){
    		ParticleEffect originalEffect = assets.get("particles/fireTest2.part");
    		ParticleEffect fx = originalEffect.copy();
    		fx.init();
    		fx.translate(gunPosition);
    		fx.start();
    		particleSystem.add(fx);
    	}
    	else{
    		ParticleEffect originalEffect = assets.get("particles/fireTest.part");
    		ParticleEffect fx = originalEffect.copy();
    		fx.init();
    		fx.translate(gunPosition);
    		fx.start();
    		particleSystem.add(fx);
    	}
    }
    
    public boolean prepareShot(){
    	if(masterShotCount>0 && reloadTime <=0){
			if(masterShotCount < 2){
				state = SHOT;
	    		reloadTime = 1.12f;
	    		animDelay = states[SHOT].animations.first().duration;
			}else{
				state = SHOT_STRONG;
				animDelay = states[SHOT_STRONG].animations.first().duration;
	    		reloadTime = 3f;
			}
			shoudShot = true;
			masterShotCount=0;

			return true;
		}
    	return false;
    }
    
    public void shot(float delta){
    	if(reloadTime <= 0){
    		masterShotCount+=delta;
    	}
    }
    
    public void walk(){
    	if(!Commands.CMD_SHOT && animDelay <=0){
    		state = WALK;
    	}
    	
    }
    
    public void idle(){
    	if(animDelay<=0){
    		state = IDLE;
    	}
    }
    
    public int getState(){
    	return state;
    }
    
    public Vector3 getGunPosition(){
    	return gunPosition;
    }
    
}
