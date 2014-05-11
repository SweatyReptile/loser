package com.sweatyreptile.losergame.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.TweenEquations;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sweatyreptile.losergame.PlayerInputProcessor;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;
import com.sweatyreptile.losergame.tween.LevelAccessor;

public abstract class ScrollingLevelScreen extends LevelScreen {

	protected Tween cameraTween;
	protected float levelEnd;
	protected float level0;

	public ScrollingLevelScreen(ScreenFinishedListener listener, Screen nextScreen,
			SpriteBatch batch, AssetManagerPlus assets,
			PlayerInputProcessor playerInputProcessor, int width, int height,
			float viewportWidth, float viewportHeight, float timeLimit) {
		super(listener, nextScreen, batch, assets, playerInputProcessor, width, height,
				viewportWidth, viewportHeight, timeLimit);
	}

	@Override
	public void update(float delta) {
		if (player.hasVelocity()){
			updateCamera();
		}
		super.update(delta);

	}

	private void updateCamera() {
		float playerX = player.getX();
		float camera0 = playerX - (viewportWidth / 2);
		float cameraEnd = playerX + (viewportWidth / 2);
		
		if ((levelEnd == 0 && level0 == 0) || 
				camera0 > level0 && cameraEnd < levelEnd){
			
			if (cameraTween != null){
				cameraTween.kill();
			}
			
			cameraTween = Tween.to(this, LevelAccessor.CAMERA_POSITION, .5f)
				.target(playerX, viewportHeight / 2)
				.ease(TweenEquations.easeOutExpo)
				.start(tweenManager);
			
			//setCameraPosition(playerX, viewportHeight / 2);
		}
	}

}