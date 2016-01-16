package br.com.techschool.lunarkiller.model;

import br.com.techschool.lunarkiller.model.GameObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.utils.UBJsonReader;

public class Hervog {
	
	public int state;
	
	public static final int IDLE	=	0;
	public static final int SHOT	=	2;
	public static final int DYING	=	3;
	public static final int	DEAD	=	4;
	
	private static int contShots	= 	0;
	private int direction;
	private static final int	NONE	=	-1;
	private static final int	FRONT	=	0;
	private static final int	BACK	=	1;
	private static final int	LEFT	=	2;
	private static final int	RIGHT	=	3;

	private Music shot;
	private Music reload;
	
	public static final float STEP		=	3.0F;
	
	
	public GameObject stages[];
	
	
	public Hervog(){
		ModelLoader<ModelLoader.ModelParameters> modelLoader = new G3dModelLoader(new UBJsonReader());
		
		Model modelIdle  = modelLoader.loadModel(Gdx.files.internal("models/hervog/treading_water.fbx"));
		Model modelShot	 = modelLoader.loadModel(Gdx.files.internal("models/hervog/shooting.fbx"));
		Model modelDying = modelLoader.loadModel(Gdx.files.internal("models/hervog/dying_backwards.fbx"));
		
		stages = new GameObject[4];
		stages[IDLE]	= new GameObject(modelIdle, -1);
		stages[SHOT]	= new GameObject(modelShot, -1);
		stages[DYING]	= new GameObject(modelDying, 1);
	
		state 	= IDLE;
		shot 	= Gdx.audio.newMusic(Gdx.files.internal("sound/shot.mp3"));
		
		reload 	= Gdx.audio.newMusic(Gdx.files.internal("sound/shotReload.mp3"));
	}
	
	public void setInitialPosition(float x, float y, float z){
		stages[IDLE].transform.translate(x, y, z);
		stages[SHOT].transform.translate(x, y, z);
		stages[DYING].transform.translate(x, y, z);
	}
	
	public void	moveFront(){
		state	=	IDLE;
		direction	=	FRONT;
	}

	public void	moveLeft(){
		state	=	IDLE;
		direction	=	LEFT;
	}
	
	public void	moveRight(){
		state	=	IDLE;
		direction	=	RIGHT;
	}
	
	public void	moveBack(){
		state	=	IDLE;
		direction	=	BACK;
	}
	
	public void shot(){
		state = SHOT;
		
	}
	public void idle(){
		state = IDLE;
		direction = NONE;
	}
	
	
	

	
	public void update(float delta){
		
		switch(state){
			case IDLE:
				
				if(direction == FRONT){

					stages[IDLE].transform.translate(0, 0, STEP *delta);

					stages[SHOT].transform.translate(0, 0, STEP *delta);

					stages[DYING].transform.translate(0, 0, STEP *delta);
					
				}
				
				if (direction == LEFT){

					stages[IDLE].transform.translate(STEP * delta, 0, 0);

					stages[SHOT].transform.translate(STEP * delta, 0, 0);

					stages[DYING].transform.translate(STEP * delta, 0, 0);
				
				}
				if (direction == RIGHT){

					stages[IDLE].transform.translate(-STEP * delta, 0, 0);

					stages[SHOT].transform.translate(-STEP * delta, 0, 0);

					stages[DYING].transform.translate(-STEP * delta, 0, 0);
				}
				
				if(direction == BACK){
					stages[IDLE].transform.translate(0, 0, -STEP *delta);

					stages[SHOT].transform.translate(0, 0, -STEP *delta);

					stages[DYING].transform.translate(0, 0, -STEP *delta);
					
				}
				
				stages[state].update(delta);
				break;
			case SHOT:
				stages[state].update(delta);
				shot.play();
				contShots++;
				if(contShots > 3){
					reload.play();
					contShots = 0;
				}

				break;
			case DYING:
				stages[state].update(delta);

				break;
			case DEAD:
				break;
				
		}
		
	}
	
	
}

