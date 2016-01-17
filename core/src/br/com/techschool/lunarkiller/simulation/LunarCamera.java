package br.com.techschool.lunarkiller.simulation;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import br.com.techschool.lunarkiller.model.Character;

public class LunarCamera extends PerspectiveCamera{
	
	public Vector3 offset;
	public float dist;
	public float angle;
	public float height;
	
	private Character player;
	
	private Vector3 playerPosition;
	private Vector3 newLookAt;
	private Vector3 newPosition;
	
	
	
	public LunarCamera(float fov, float viewPortWidth, float viewPortHeight, Character player){
		super(fov, viewPortWidth, viewPortHeight);
		super.near = 0.1f;
		super.far = 1000f;
		
		this.player = player;
		
		offset = new Vector3( 0, 0, 0);
		
		angle = -25;
		height = 1.5f;
		dist = 1f;
		super.position.set(player.getPosition().x + offset.x, player.getPosition().y + offset.y, player.getPosition().z + offset.z);
		
		newPosition = new Vector3();
		newLookAt = new Vector3();
	}
	
	public void update(){
		if(player!=null){
			playerPosition = player.getPosition();
			
			newPosition.x = (float) (playerPosition.x + dist*Math.cos(Math.toRadians(angle+player.angle+90)));
			newPosition.y = playerPosition.y + height;
			newPosition.z = (float) (playerPosition.z + dist*Math.sin(Math.toRadians(angle+player.angle+90)));
			
			newLookAt = player.origin;
			newLookAt.y = 20;
			
			super.position.set(newPosition);
			super.lookAt(newLookAt);
			super.up.set(new Vector3(0,1,0));
			
			super.update();
		}
	}
}
