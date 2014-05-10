package com.sweatyreptile.losergame;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.sweatyreptile.losergame.fixtures.EntityFixtureDef;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;

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
			float newY;
			if (pos.y >= 0) newY = Math.abs(pos.y % viewportHeight) + originY;
			else newY = (viewportHeight - Math.abs(pos.y % viewportHeight)) + originY;
			entity.currentBody.setTransform(pos.x, newY, entity.currentBody.getAngle());
		}
	}
	
	public float getHeight(float screenHeight, float viewportHeight){
		return ((float) background.getHeight() / screenHeight) * viewportHeight;
	}
	
	public void setOriginY(float originY){
		this.originY = originY;
	}

	protected void setupBorders(EntityFactory entityFactory, float viewportHeight, float viewportWidth, 
			AssetManagerPlus assets, boolean horizontal, boolean vertical){ //0.06 is the width of borders
		if (horizontal){
			entityFactory.create("horizontal_border", BodyType.StaticBody, 0f, viewportHeight, new EntityFixtureDef(assets, "horizontal_border"), false);
			entityFactory.create("horizontal_border", BodyType.StaticBody, 0f, -0.06f, new EntityFixtureDef(assets, "horizontal_border"), false);
		}
		if (vertical){
			entityFactory.create("vertical_border", BodyType.StaticBody, viewportWidth, 0f, new EntityFixtureDef(assets, "vertical_border"), false);
			entityFactory.create("vertical_border", BodyType.StaticBody, -0.06f, 0f, new EntityFixtureDef(assets, "vertical_border"), false);
		}
	}
	

}
