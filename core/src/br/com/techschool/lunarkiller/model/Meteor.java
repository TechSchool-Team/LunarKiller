package br.com.techschool.lunarkiller.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.UBJsonReader;

public class Meteor {
	private Vector3 position;
	//private Vector3 rotation;
	private Vector3 direction;
	public ModelInstance meteor;
	//private float spd = 15f;
	public boolean destroy;
	
	public Meteor(Vector3 pos, Vector3 dir){
		G3dModelLoader loader = new G3dModelLoader(new UBJsonReader());
		Model meteorModel = loader.loadModel(Gdx.files.internal("models/rock/rock.g3db"));
		meteor = new ModelInstance(meteorModel);
		//ModelBuilder modelBuilder = new ModelBuilder();
		//meteor = new ModelInstance(modelBuilder.createSphere(2f, 2f, 0.5f, 12, 12, new Material(ColorAttribute.createDiffuse(Color.RED)), Usage.Position | Usage.ColorUnpacked | Usage.Normal));
		position = pos;
		direction = dir;
		destroy = false;
		this.meteor.transform.setTranslation(position);
		//this.meteor.transform.scale(10f, 10f, 10f);
	}
	
	public void update(float delta, Character player){
		this.meteor.transform.translate(direction);
		this.position.add(direction);
		
		Vector3 ballDist =  new Vector3(0,0,0);//this.bulletLocation.sub(boss.bossPosition);
		ballDist.x = this.position.x - player.getPosition().x;
		ballDist.y = 0;
		ballDist.z = this.position.z - player.getPosition().z;
		
		if(Math.sqrt(ballDist.x*ballDist.x + ballDist.z*ballDist.z) < 12){
			destroy = true;
			player.die();
		}
		
		if(!destroy){
			this.destroy = this.position.x > 500 || this.position.y > 500 || this.position.z > 500;
		}
	}
	
	public ModelInstance getMesh(){
		return meteor;
	}
	
}
