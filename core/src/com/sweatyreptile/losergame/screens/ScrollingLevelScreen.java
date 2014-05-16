package com.sweatyreptile.losergame.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.TweenEquations;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.sweatyreptile.losergame.LevelManager;
import com.sweatyreptile.losergame.PlayerInputProcessor;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;
import com.sweatyreptile.losergame.tween.ScrollingLevelAccessor;

public abstract class ScrollingLevelScreen extends LevelScreen {

	private static final float TWEEN_TIME = .5f;
	protected Tween cameraTweenHorizontal;
	protected Tween cameraTweenVertical;
	protected float levelEndHorizontal;
	protected float level0Horizontal;
	
	protected Tween borderTweenX;
	protected Tween borderTweenY;
	
	protected float levelEndVertical;
	protected float level0Vertical;
	
	protected boolean vertical;
	protected boolean horizontal;
	
	protected float offsetY;
	protected boolean strictlyDownwards;
	
	protected float borderX;
	protected float borderY;

	public ScrollingLevelScreen(LevelManager levelManager, SpriteBatch batch, AssetManagerPlus assets,
			PlayerInputProcessor playerInputProcessor, int width, int height,
			float viewportWidth, float viewportHeight, float timeLimit, String alias, String levelName, 
			boolean horizontalScrolling, boolean verticalScrolling) {
		super(levelManager, batch, assets, playerInputProcessor, width, height,
				viewportWidth, viewportHeight, timeLimit, alias, levelName);
		this.vertical = verticalScrolling;
		this.horizontal = horizontalScrolling;
	}

	public ScrollingLevelScreen() {
		super();
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
				if (borderTweenX != null){
					borderTweenX.kill();
				}
				cameraTweenHorizontal = Tween.to(this, ScrollingLevelAccessor.CAMERA_POSITION, TWEEN_TIME)
					.target(playerX, viewportHeight / 2)
					.ease(TweenEquations.easeOutExpo)
					.start(tweenManager);
				
				borderTweenX = Tween.to(this, ScrollingLevelAccessor.BORDER_X, TWEEN_TIME)
						.target(playerX - viewportWidth/2)
						.ease(TweenEquations.easeOutExpo)
						.start(tweenManager);
			}
		}
		if (vertical){
			if (((levelEndVertical == 0 && level0Vertical == 0) || 
					(camera0Vertical > level0Vertical && cameraEndVertical < levelEndVertical))
					&& (!strictlyDownwards || Float.compare(oldCamY, (playerY + offsetY)) > 0)){
				if (cameraTweenVertical != null){
					cameraTweenVertical.kill();
				}
				if (borderTweenY != null){
					borderTweenY.kill();
				}
				cameraTweenVertical = Tween.to(this, ScrollingLevelAccessor.CAMERA_POSITION, TWEEN_TIME)
					.target(camera.position.x, playerY + offsetY)
					.ease(TweenEquations.easeOutExpo)
					.start(tweenManager);
				
				borderTweenY = Tween.to(this, ScrollingLevelAccessor.BORDER_Y, TWEEN_TIME)
						.target(playerY + offsetY - viewportHeight/2)
						.ease(TweenEquations.easeOutExpo)
						.start(tweenManager);
			}	
		}
		
	}
	
	public float getBorderX() {
		return borderX;
	}
	
	public float getBorderY() {
		return borderY;
	}
	
	public void updateBordersX(float newX){
		borderX = newX;
		updateBorderX("vertical_border", newX - 0.06f);
		updateBorderX("vertical_border_2", newX + viewportWidth);
		updateBorderX("horizontal_border", newX);
		updateBorderX("horizontal_border_2", newX);

	}
	
	public void updateBordersY(float newY){
		borderY = newY;
		updateBorderY("vertical_border", newY);
		updateBorderY("vertical_border_2", newY);
		updateBorderY("horizontal_border", newY + viewportHeight);
		updateBorderY("horizontal_border_2", newY - 0.06f);
	}
	
	protected void updateBorderX(String borderName, float newX){
		if (entities.containsKey(borderName)){
			Body border = entities.get(borderName).getBody();
			border.setTransform(newX, border.getPosition().y, border.getAngle());
		}
	}
	
	protected void updateBorderY(String borderName, float newY){
		if (entities.containsKey(borderName)){
			Body border = entities.get(borderName).getBody();
			border.setTransform(border.getPosition().x, newY, border.getAngle());
		}
	}
	

	
	public void setOffsetY(float offsetY){
		this.offsetY = offsetY;
	}
	
	public void setStrictlyDownwards(boolean strictlyDownwards){
		this.strictlyDownwards = strictlyDownwards;
	}

}