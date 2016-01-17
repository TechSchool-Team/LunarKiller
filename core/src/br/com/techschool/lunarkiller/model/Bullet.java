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
	private final float spd = 15f;
	private ModelInstance mesh;
	
	public Bullet(Vector3 pos, Vector3 dir, boolean strong){
		this.bulletLocation  = pos;
		this.direction = dir;
		ModelBuilder modelBuilder = new ModelBuilder();
		if(strong){
			this.mesh = new ModelInstance(modelBuilder.createSphere(2f, 2f, 0.5f, 12, 12, new Material(ColorAttribute.createDiffuse(Color.RED)), Usage.Position | Usage.ColorUnpacked | Usage.Normal));
		}
		else{
			this.mesh = new ModelInstance(modelBuilder.createSphere(0.5f, 0.5f, 0.1f, 12, 12, new Material(ColorAttribute.createDiffuse(Color.YELLOW)), Usage.Position | Usage.ColorUnpacked | Usage.Normal));
		}
        
        this.mesh.transform.setTranslation(bulletLocation);
	}
	
	public void update(float delta){
		Vector3 trans = direction;
		this.mesh.transform.translate(direction);
		
		this.destroy = this.bulletLocation.x > 1000 || this.bulletLocation.y > 1000 || this.bulletLocation.z > 1000;
	}
	
	public ModelInstance getMesh(){
		return mesh;
	}
}
