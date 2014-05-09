package com.sweatyreptile.losergame;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class LevelChunk {
	
	protected ArrayList<Entity> entities;
	protected Texture background;
	
	public LevelChunk(){
		entities = new ArrayList<Entity>();
		setup();
	}
	
	protected abstract void setup();
	
	public void render(float delta, SpriteBatch renderer, float x, float y, float viewportWidth, float viewportHeight) {
		renderer.begin();
		renderer.disableBlending();
		renderer.draw(background, x, y, viewportWidth, viewportHeight);
		renderer.enableBlending();
		renderer.end();
		
	}

	public void update(float delta) {
		for (Entity<?> entity : entities){
			entity.update(delta);
		}
		
	}
	
	public float getHeight(float screenHeight, float viewportHeight){
		return ((float) background.getHeight() / screenHeight) * viewportHeight;
	}
	
	

}
