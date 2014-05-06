package com.sweatyreptile.losergame.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.sweatyreptile.losergame.PlayerInputProcessor;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;

public abstract class ScrollingLevelScreen extends LevelScreen {

	protected float levelEnd;
	protected float level0;

	public ScrollingLevelScreen(SpriteBatch batch, AssetManagerPlus assets,
			PlayerInputProcessor playerInputProcessor, int width, int height,
			float viewportWidth, float viewportHeight, float timeLimit) {
		super(batch, assets, playerInputProcessor, width, height,
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
			setCameraPosition(playerX, viewportHeight / 2);
		}
	}

}