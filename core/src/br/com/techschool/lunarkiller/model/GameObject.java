package br.com.techschool.lunarkiller.model;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController.AnimationDesc;

public class GameObject extends ModelInstance {
	
	private AnimationController animationController;
	private	boolean				animationDone;
	
	
	
	public GameObject(Model model, int loopCount) {
		super(model);
		animationController	=	new	AnimationController(this);
		animationDone		=	false;
		
		animationController.setAnimation(animations.get(0).id , loopCount, new AnimationController.AnimationListener(){

			@Override
			public void onEnd(AnimationDesc animation) {
				animationDone	= true;
			}

			@Override
			public void onLoop(AnimationDesc animation) {
				animationDone	= true;
			}
			
		});
	}
	
	
	public void update(float delta){
		animationController.update(delta);
		
		
	}
	
	public boolean isAnimationDone(){
		return this.animationDone;
	}
	
	
	public void resetAnimation(){
		this.animationDone = false;
	}


}
