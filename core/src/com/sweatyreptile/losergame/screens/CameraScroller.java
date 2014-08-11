package com.sweatyreptile.losergame.screens;

import java.util.Map;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.physics.box2d.Body;
import com.sweatyreptile.losergame.Entity;
import com.sweatyreptile.losergame.entities.Player;
import com.sweatyreptile.losergame.tween.LevelScreenAccessor;

public class CameraScroller{

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
	
	protected LevelScreen levelScreen;
	protected Camera camera;
	protected Player player;
	protected TweenManager tweenManager;

	public CameraScroller(boolean vertical, boolean horizontal, LevelScreen levelScreen, Camera camera, Player player, TweenManager tweenManager){
		this.vertical = vertical;
		this.horizontal = horizontal;
		this.levelScreen = levelScreen;
		this.camera = camera;
		this.player = player;
		this.tweenManager = tweenManager;
	}

	public CameraScroller(LevelScreen levelScreen, Camera camera, Player player, TweenManager tweenManager){
		this(false, false, levelScreen, camera, player, tweenManager);
	}

	public void update(float delta) {
		if (player.hasVelocity()){
			updateCamera();
		}

	}

	protected void updateCamera() {
		float oldCamY = camera.position.y;
		
		float playerX = player.getX();
		float camera0Horizontal = playerX - (camera.viewportWidth / 2);
		float cameraEndHorizontal = playerX + (camera.viewportWidth / 2);
		
		float playerY = player.getY();
		float camera0Vertical = playerY - (camera.viewportHeight / 2);
		float cameraEndVertical = playerY + (camera.viewportHeight / 2);
		

		if (horizontal){
			if ((levelEndHorizontal == 0 && level0Horizontal == 0) || 
					camera0Horizontal > level0Horizontal && cameraEndHorizontal < levelEndHorizontal){
				if (cameraTweenHorizontal != null){
					cameraTweenHorizontal.kill();
				}
				if (borderTweenX != null){
					borderTweenX.kill();
				}
				cameraTweenHorizontal = Tween.to(levelScreen, LevelScreenAccessor.CAMERA_POSITION, TWEEN_TIME)
					.target(playerX, camera.viewportHeight / 2)
					.ease(TweenEquations.easeOutExpo)
					.start(tweenManager);
				
				borderTweenX = Tween.to(levelScreen, LevelScreenAccessor.BORDER_X, TWEEN_TIME)
						.target(playerX - camera.viewportWidth/2)
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
				cameraTweenVertical = Tween.to(levelScreen, LevelScreenAccessor.CAMERA_POSITION, TWEEN_TIME)
					.target(camera.position.x, playerY + offsetY)
					.ease(TweenEquations.easeOutExpo)
					.start(tweenManager);
				
				borderTweenY = Tween.to(levelScreen, LevelScreenAccessor.BORDER_Y, TWEEN_TIME)
						.target(playerY + offsetY - camera.viewportHeight/2)
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
	
	public void updateBordersX(float newX, Map<String, Entity<?>> entities){
		borderX = newX;
		updateBorderX(entities, "vertical_border", newX - 0.06f);
		updateBorderX(entities, "vertical_border_2", newX + camera.viewportWidth);
		updateBorderX(entities, "horizontal_border", newX);
		updateBorderX(entities, "horizontal_border_2", newX);

	}
	
	public void updateBordersY(float newY, Map<String, Entity<?>> entities){
		borderY = newY;
		updateBorderY(entities, "vertical_border", newY);
		updateBorderY(entities, "vertical_border_2", newY);
		updateBorderY(entities, "horizontal_border", newY + camera.viewportHeight);
		updateBorderY(entities, "horizontal_border_2", newY - 0.06f);
	}
	
	protected void updateBorderX(Map<String, Entity<?>> entities, String borderName, float newX){
		if (entities.containsKey(borderName)){
			Body border = entities.get(borderName).getBody();
			border.setTransform(newX, border.getPosition().y, border.getAngle());
		}
	}
	
	protected void updateBorderY(Map<String, Entity<?>> entities, String borderName, float newY){
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

	public void setHorizontal(boolean horizontal) {
		this.horizontal = horizontal;
	}

	public void setlevel0Horizontal(float level0) {
		level0Horizontal = level0;
	}

	public void setlevelEndHorizontal(float levelEnd) {
		levelEndHorizontal = levelEnd;
	}

	public void setVertical(boolean vertical) {
		this.vertical = vertical;
	}

	public Camera getCamera() {
		return camera;
	}

}