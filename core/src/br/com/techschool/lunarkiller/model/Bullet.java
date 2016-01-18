package br.com.techschool.lunarkiller.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

public class Bullet {
	private Vector3 bulletLocation;
	private Vector3 direction;
	public boolean destroy = false;
	//private final float spd = 15f;
	private ModelInstance mesh;
	public float addScore;
	private boolean strong;
	
	public Bullet(Vector3 pos, Vector3 dir, boolean _strong){
		addScore = 0;
		this.bulletLocation  = pos;
		this.direction = dir;
		this.strong= _strong;
		ModelBuilder modelBuilder = new ModelBuilder();
		if(strong){
			this.mesh = new ModelInstance(modelBuilder.createSphere(2f, 2f, 0.5f, 12, 12, new Material(ColorAttribute.createDiffuse(Color.RED)), Usage.Position | Usage.ColorUnpacked | Usage.Normal));
		}
		else{
			this.mesh = new ModelInstance(modelBuilder.createSphere(0.5f, 0.5f, 0.1f, 12, 12, new Material(ColorAttribute.createDiffuse(Color.YELLOW)), Usage.Position | Usage.ColorUnpacked | Usage.Normal));
		}
        
        this.mesh.transform.setTranslation(bulletLocation);
	}
	
	public void update(float delta, Boss boss){
		
		this.mesh.transform.translate(direction);
		this.bulletLocation.add(direction);
		
		Vector3 ballDist =  new Vector3(0,0,0);//this.bulletLocation.sub(boss.bossPosition);
		ballDist.x = this.bulletLocation.x - boss.bossPosition.x;
		ballDist.y = 0;
		ballDist.z = this.bulletLocation.z - boss.bossPosition.z;
		
		if(Math.sqrt(ballDist.x*ballDist.x + ballDist.z*ballDist.z) < 12){
			destroy = true;
			boss.hit();
			addScore = 10;
			if(strong)
				addScore = 50;
		}
		
		if(!destroy){
			this.destroy = this.bulletLocation.x > 500 || this.bulletLocation.y > 500 || this.bulletLocation.z > 500;
		}
		
	}
	
	public ModelInstance getMesh(){
		return mesh;
	}
}
