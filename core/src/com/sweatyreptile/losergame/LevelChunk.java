package com.sweatyreptile.losergame;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class LevelChunk {
	
	protected float originY;
	protected Map<String, Entity<?>> chunkEntities;
	protected Texture background;
	
	public LevelChunk(){
		chunkEntities = new HashMap<String, Entity<?>>();
		setup();
	}
	
	protected abstract void setup();
	
	public void render(float delta, SpriteBatch renderer, float x, float y, float viewportWidth, float viewportHeight) {
		renderer.begin();
		renderer.disableBlending();
		renderer.draw(background, x, y, viewportWidth, viewportHeight);
		renderer.enableBlending();
		for (Entity<?> entity : chunkEntities.values()){
			entity.render(renderer);
		}
		renderer.end();
		
	}

	public void update(float delta) {
		for (Entity<?> entity : chunkEntities.values()){
			entity.update(delta);
		}
		
	}
	
	public void updateEntityPositions(float viewportHeight){
		for (Entity<?> entity : chunkEntities.values()){
			Vector2 pos = entity.currentBody.getPosition();
			float newY = Math.abs(pos.y % viewportHeight) + originY;
			entity.currentBody.setTransform(pos.x, newY, entity.currentBody.getAngle());
			System.out.println("ORIGIN: " + originY);
			System.out.println("POSITION: " + pos.y);
			System.out.println("NEW Y: " + newY);

			//entity.update(delta);
		}
	}
	
	public float getHeight(float screenHeight, float viewportHeight){
		return ((float) background.getHeight() / screenHeight) * viewportHeight;
	}
	
	public void setOriginY(float originY){
		this.originY = originY;
	}
	
	

}
