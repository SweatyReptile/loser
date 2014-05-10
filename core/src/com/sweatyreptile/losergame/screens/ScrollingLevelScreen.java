package com.sweatyreptile.losergame.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sweatyreptile.losergame.PlayerInputProcessor;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;

public abstract class ScrollingLevelScreen extends LevelScreen {

	protected float levelEndHorizontal;
	protected float level0Horizontal;
	
	protected float levelEndVertical;
	protected float level0Vertical;
	
	protected boolean vertical;
	protected boolean horizontal;
	
	protected float offsetY;
	protected boolean strictlyDownwards;

	public ScrollingLevelScreen(ScreenFinishedListener listener, Screen nextScreen,
			SpriteBatch batch, AssetManagerPlus assets,
			PlayerInputProcessor playerInputProcessor, int width, int height,
			float viewportWidth, float viewportHeight, float timeLimit,
			boolean horizontalScrolling, boolean verticalScrolling) {
		super(listener, nextScreen, batch, assets, playerInputProcessor, width, height,
				viewportWidth, viewportHeight, timeLimit);
		this.vertical = verticalScrolling;
		this.horizontal = horizontalScrolling;
	}

	@Override
	public void update(float delta) {
		if (player.hasVelocity()){
			updateCamera();
		}
		super.update(delta);

	}

	protected void updateCamera() {
		float oldCamY = camera.position.y;
		
		float playerX = player.getX();
		float camera0Horizontal = playerX - (viewportWidth / 2);
		float cameraEndHorizontal = playerX + (viewportWidth / 2);
		
		float playerY = player.getY();
		float camera0Vertical = playerY - (viewportHeight / 2);
		float cameraEndVertical = playerY + (viewportHeight / 2);
		

		if (horizontal){
			if ((levelEndHorizontal == 0 && level0Horizontal == 0) || 
					camera0Horizontal > level0Horizontal && cameraEndHorizontal < levelEndHorizontal){
				setCameraPosition(playerX, camera.position.y);
			}
		}
		if (vertical){
			if (((levelEndVertical == 0 && level0Vertical == 0) || 
					(camera0Vertical > level0Vertical && cameraEndVertical < levelEndVertical))
					&& ((!strictlyDownwards) || Float.compare(oldCamY, (playerY + offsetY)) > 0)){
				setCameraPosition(camera.position.x, playerY + offsetY);
			}	
		}
				
	}
	
	public void setOffsetY(float offsetY){
		this.offsetY = offsetY;
	}
	
	public void setStrictlyDownwards(boolean strictlyDownwards){
		this.strictlyDownwards = strictlyDownwards;
	}

}