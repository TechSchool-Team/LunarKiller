package br.com.techschool.lunarkiller.model;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController.AnimationDesc;

public class GameObject extends ModelInstance{
	private AnimationController animationController;
	private boolean animationFinished;
	
	public GameObject(Model model) {
		super(model);
		
		animationController = new AnimationController(this);
		
		animationController.setAnimation(this.animations.get(0).id, -1, new AnimationController.AnimationListener(){
			public void onLoop(AnimationDesc animation){
				animationFinished = true;
			}
			
			public void onEnd(AnimationDesc animation){
				animationFinished = true;
			}
		});
	}
	
	public void update(float delta){
		animationController.update(delta);
	}
	
	public boolean isAnimationFinished(){
		return this.animationFinished;
	}
	
	public void resetAnimation(){
		this.animationFinished = false;
	}
}
