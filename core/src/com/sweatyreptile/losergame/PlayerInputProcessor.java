package com.sweatyreptile.losergame;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.sweatyreptile.losergame.entities.Player;

public class PlayerInputProcessor implements InputProcessor {

	private Player player;
	private boolean showConsole;

	@Override
	public boolean keyDown(int keycode) {
		if (player != null && !showConsole) {
			switch(keycode) {
			case Input.Keys.SHIFT_LEFT:
			case Input.Keys.SHIFT_RIGHT:
			case Input.Keys.S:
			case Input.Keys.DOWN:
				player.duck();
				return true;
			case Input.Keys.A:
			case Input.Keys.LEFT:
				player.moveLeft();
				return true;
			case Input.Keys.D:
			case Input.Keys.RIGHT:
				player.moveRight();
				return true;
			case Input.Keys.W:
			case Input.Keys.UP:
			case Input.Keys.SPACE:
				player.jump();
				return true;
			case Input.Keys.GRAVE:
				showConsole = !showConsole;
				return true;
			//case Input.Keys.E:
			//case Input.Keys.ENTER:
			default:
				player.quack();
				return true;
			}
		}
		else if (showConsole){
			switch(keycode) {
			case Input.Keys.GRAVE:
				showConsole = !showConsole;
				return true;
			default:
				return false;
			}
		}
		else{
			return false;
		}
	}

	@Override
	public boolean keyUp(int keycode) {
		if (player != null) {
			switch(keycode) {
			case Input.Keys.SHIFT_LEFT:
			case Input.Keys.SHIFT_RIGHT:
			case Input.Keys.S:
			case Input.Keys.DOWN:
				player.standUp();
				return true;
			case Input.Keys.A:
			case Input.Keys.LEFT:
				player.stopMovingLeft();
				return true;
			case Input.Keys.D:
			case Input.Keys.RIGHT:
				player.stopMovingRight();
				return true;
			case Input.Keys.W:
			case Input.Keys.UP:
			case Input.Keys.SPACE:
				player.stopJumping();
				return true;
			//case Input.Keys.E:
			//case Input.Keys.ENTER:
			default:
				player.stopQuacking();
				return true;
			}
		}
		else {
			return false;
		}
		//return false;
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
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void clearPlayer() {
	  this.player = null;
	}
	
	public boolean showConsole(){
		return showConsole;
	}

}
