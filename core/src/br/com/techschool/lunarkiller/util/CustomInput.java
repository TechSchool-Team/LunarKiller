package br.com.techschool.lunarkiller.util;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class CustomInput  implements InputProcessor{

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		
		switch(keycode){
		case Input.Keys.W:
			Commands.MOVE_FRONT = true;
			Commands.COMANDS_COUNT++;
			break;
		case Input.Keys.A:
			Commands.MOVE_LEFT = true;
			Commands.COMANDS_COUNT++;
			break;
		case Input.Keys.S:
			Commands.MOVE_BACK = true;
			Commands.COMANDS_COUNT++;
			break;
		case Input.Keys.D:
			Commands.MOVE_RIGHT = true;
			Commands.COMANDS_COUNT++;
			break;
		case Input.Keys.SPACE:
			Commands.CMD_SHOT = true;
			Commands.COMANDS_COUNT++;
			break;
		default:
			return false;
		}
		
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		
		switch(keycode){
		case Input.Keys.W:
			Commands.MOVE_FRONT = false;
			Commands.COMANDS_COUNT--;
			break;
		case Input.Keys.A:
			Commands.MOVE_LEFT = false;
			Commands.COMANDS_COUNT--;
			break;
		case Input.Keys.S:
			Commands.MOVE_BACK = false;
			Commands.COMANDS_COUNT--;
			break;
		case Input.Keys.D:
			Commands.MOVE_RIGHT = false;
			Commands.COMANDS_COUNT--;
			break;
		case Input.Keys.SPACE:
			Commands.CMD_SHOT = false;
			Commands.COMANDS_COUNT--;
			break;
		default:
			return false;
		}
		
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		
		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
