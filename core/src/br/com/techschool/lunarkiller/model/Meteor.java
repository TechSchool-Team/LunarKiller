package br.com.techschool.lunarkiller.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.UBJsonReader;

public class Meteor {
	private Vector3 position;
	private Vector3 rotation;
	public ModelInstance meteor;
	
	public Meteor(){
		G3dModelLoader loader = new G3dModelLoader(new UBJsonReader());
		Model meteorModel = loader.loadModel(Gdx.files.internal("model/rock.g3db"));
		meteor = new ModelInstance(meteor);
		
	}
	
	public void update(float delta){
		
	}
	
	
}
