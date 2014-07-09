package com.sweatyreptile.losergame;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import com.sweatyreptile.losergame.screens.LevelScreen;
import com.sweatyreptile.losergame.tween.LevelScreenAccessor;

public class EditModeInputProcessor implements InputProcessor {

	private LevelScreen level;
	
	public EditModeInputProcessor(LevelScreen level){
		this.level = level;
	}
	
	private boolean notnull() {
		if (level.getEditInputProcessor() != null){
			return true;
		}
		return false;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if (notnull()){
			return level.getEditInputProcessor().keyDown(keycode);
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (notnull()){
			return level.getEditInputProcessor().keyUp(keycode);
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		if (notnull()){
			return level.getEditInputProcessor().keyTyped(character);
		}
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (notnull()){
			boolean consumed = level.getEditInputProcessor().touchDown(screenX, screenY, pointer, button);
			if (!consumed){
				Vector3 worldCoords = level.getCamera().unproject(
						new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
				Tween.to(level, LevelScreenAccessor.CAMERA_POSITION, 2f)
					 .target(worldCoords.x, worldCoords.y)
					 .ease(TweenEquations.easeOutExpo)
					 .start(level.getTweenManager());
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (notnull()){
			return level.getEditInputProcessor().touchUp(screenX, screenY, pointer, button);
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (notnull()){
			return level.getEditInputProcessor().touchDragged(screenX, screenY, pointer);
		}
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		if (notnull()){
			return level.getEditInputProcessor().mouseMoved(screenX, screenY);
		}
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		if (notnull()){
			return level.getEditInputProcessor().scrolled(amount);
		}
		return false;
	}

}
