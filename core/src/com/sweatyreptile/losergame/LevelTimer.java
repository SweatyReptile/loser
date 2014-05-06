package com.sweatyreptile.losergame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.TimeUtils;
import com.sweatyreptile.losergame.screens.LevelScreen;

public class LevelTimer {
	
	private static final float WIDTH = 0.015f;
	private float viewportWidth;
	private float viewportHeight;
	private LevelScreen level;
	
	private float totalSeconds;
	private float elapsedSeconds;
	private float startTime;
	private float endTime;
	
	private boolean timesUp;
		
	public LevelTimer(LevelScreen level, float viewportWidth, float viewportHeight, float totalSeconds){
		this.level = level;
		this.viewportWidth = viewportWidth;
		this.viewportHeight = viewportHeight;
		this.totalSeconds = totalSeconds;
	}
	
	public void start(){
		startTime = (float) TimeUtils.nanoTime();
		endTime = startTime + (totalSeconds * (float) Math.pow(10, 9));
	}
	
	public void render(ShapeRenderer shapeRenderer){
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.WHITE);
		shapeRenderer.rect(0f, viewportHeight-WIDTH, viewportWidth, WIDTH);
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.rect(0f, viewportHeight-WIDTH, calculateWidth(), WIDTH);
		shapeRenderer.end();
	}
	
	public void update(){
		if (!timesUp){
			float nanoTime = (float) TimeUtils.nanoTime();
			if (nanoTime >= endTime){
				timesUp =  true;
				level.finish();
			}
			elapsedSeconds = (nanoTime - startTime) / (float) Math.pow(10, 9) ;
		}
	}
	
	private float calculateWidth(){
		return (viewportWidth - ((elapsedSeconds / totalSeconds) * viewportWidth));
	}
	
	public boolean timesUp(){
		return timesUp;
	}

}
