package com.sweatyreptile.losergame;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class GlobalInputProcessor implements InputProcessor {

	private Console console;
	
	public GlobalInputProcessor(Console console) {
		this.console = console;
	}

	@Override
	public boolean keyDown(int keycode) {
		switch(keycode){
		case Input.Keys.GRAVE:
			console.toggle();
			return true;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
