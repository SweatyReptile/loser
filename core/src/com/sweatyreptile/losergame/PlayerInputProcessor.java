package com.sweatyreptile.losergame;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class PlayerInputProcessor implements InputProcessor {

	private Player player;

	@Override
	public boolean keyDown(int keycode) {
		switch(keycode) {
		case Input.Keys.A:
			player.moveLeft();
			return true;
		case Input.Keys.D:
			player.moveRight();
			return true;
		case Input.Keys.W:
		case Input.Keys.SPACE:
			player.jump();
			return true;
		}
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch(keycode) {
		case Input.Keys.A:
			player.stopMovingLeft();
			return true;
		case Input.Keys.D:
			player.stopMovingRight();
			return true;
		}
		return false;
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

}
