package com.sweatyreptile.losergame;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class PlayerInputProcessor implements InputProcessor {

	private Body playerBody;

	@Override
	public boolean keyDown(int keycode) {
		switch(keycode) {
		case Input.Keys.A:
			Vector2 position = playerBody.getPosition();
			playerBody.applyLinearImpulse(-0.5f, 0, position.x, position.y, true);
			return true;
		case Input.Keys.D:
			Vector2 position2 = playerBody.getPosition();
			playerBody.applyLinearImpulse(0.5f, 0, position2.x, position2.y, true);
			return true;
		case Input.Keys.W:
			Vector2 position3 = playerBody.getPosition();
			playerBody.applyLinearImpulse(0, 1.5f, position3.x, position3.y, true);
			return true;
		}
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
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

	public void setPlayer(Body playerBody) {
		this.playerBody = playerBody;
	}

}
