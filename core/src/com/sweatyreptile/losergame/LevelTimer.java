package com.sweatyreptile.losergame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.sweatyreptile.losergame.screens.LevelScreen;

public class LevelTimer {
	
	private static final float WIDTH = 0.015f;
	private float viewportWidth;
	private float viewportHeight;
	private LevelManager levelManager;
	
	private float totalSeconds;
	private boolean on;
	private float timePassed;
		
	public LevelTimer(LevelManager levelManager, float viewportWidth, float viewportHeight, float totalSeconds){
		this.levelManager = levelManager;
		this.viewportWidth = viewportWidth;
		this.viewportHeight = viewportHeight;
		this.totalSeconds = totalSeconds;
	}
	
	public void start(){
		on = true;
	}
	
	public void render(ShapeRenderer shapeRenderer){
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.WHITE);
		shapeRenderer.rect(0f, viewportHeight-WIDTH, viewportWidth, WIDTH);
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.rect(0f, viewportHeight-WIDTH, calculateWidth(), WIDTH);
		shapeRenderer.end();
	}
	
	public void update(float delta){
		if (on) {
			timePassed += delta;
			if (timePassed >= totalSeconds) {
				on = false;
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						levelManager.restart();
					}
				});
			}
		}
	}
	
	private float calculateWidth(){
		return viewportWidth - ((timePassed / totalSeconds) * viewportWidth);
	}
	
	public boolean isOn(){
		return on;
	}

}
