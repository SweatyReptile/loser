package com.sweatyreptile.losergame.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.TweenEquations;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.sweatyreptile.losergame.PlayerInputProcessor;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;
import com.sweatyreptile.losergame.tween.LevelAccessor;

public abstract class ScrollingLevelScreen extends LevelScreen {

	protected Tween cameraTweenHorizontal;
	protected Tween cameraTweenVertical;
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
				if (cameraTweenHorizontal != null){
					cameraTweenHorizontal.kill();
				}
				cameraTweenHorizontal = Tween.to(this, LevelAccessor.CAMERA_POSITION, .5f)
					.target(playerX, viewportHeight / 2)
					.ease(TweenEquations.easeOutExpo)
					.start(tweenManager);
				updateBordersX(playerX - viewportWidth/2);
			}
		}
		if (vertical){
			if (((levelEndVertical == 0 && level0Vertical == 0) || 
					(camera0Vertical > level0Vertical && cameraEndVertical < levelEndVertical))
					&& (!strictlyDownwards || Float.compare(oldCamY, (playerY + offsetY)) > 0)){
				if (cameraTweenVertical != null){
					cameraTweenVertical.kill();
				}
				cameraTweenVertical = Tween.to(this, LevelAccessor.CAMERA_POSITION, .5f)
					.target(camera.position.x, playerY + offsetY)
					.ease(TweenEquations.easeOutExpo)
					.start(tweenManager);
				updateBordersY(playerY + offsetY - viewportHeight/2);
			}	
		}
		
	}
	
	protected void updateBordersX(float newX){
		updateBorderX("vertical_border", newX - 0.06f);
		updateBorderX("vertical_border_2", newX + viewportWidth);
		updateBorderX("horizontal_border", newX);
		updateBorderX("horizontal_border_2", newX);

	}
	
	protected void updateBordersY(float newY){
		updateBorderY("vertical_border", newY);
		updateBorderY("vertical_border_2", newY);
		updateBorderY("horizontal_border", newY + viewportHeight);
		updateBorderY("horizontal_border_2", newY - 0.06f);
	}
	
	protected void updateBorderX(String borderName, float newX){
		try {
			Body border = entities.get(borderName).getBody();
			border.setTransform(newX, border.getPosition().y, border.getAngle());
		}
		catch (NullPointerException e) {} ;
	}
	
	protected void updateBorderY(String borderName, float newY){
		try {
			Body border = entities.get(borderName).getBody();
			border.setTransform(border.getPosition().x, newY, border.getAngle());
		}
		catch (NullPointerException e) {} ;
	}
	

	
	public void setOffsetY(float offsetY){
		this.offsetY = offsetY;
	}
	
	public void setStrictlyDownwards(boolean strictlyDownwards){
		this.strictlyDownwards = strictlyDownwards;
	}

}