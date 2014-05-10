package com.sweatyreptile.losergame;

import java.math.BigDecimal;
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
	
	public void updateEntityPositions(float viewportHeight){ //Only works in vertical direction right now
		for (Entity<?> entity : chunkEntities.values()){
			
			Vector2 pos = entity.currentBody.getPosition();
			float remainder = Math.abs(pos.y % viewportHeight);
			float remainderApprox = round(remainder, 1);
			float roundedOrigin = round(originY, 1);
			float newY;

			if (Float.compare(remainderApprox, 0f) == 0) newY = roundedOrigin;
			else if (Float.compare(pos.y, 0f) > 0) newY = remainder + roundedOrigin;
			else newY = (viewportHeight - remainder) + roundedOrigin;
			
			entity.currentBody.setTransform(pos.x, round(newY, 2), entity.currentBody.getAngle());
		}
	}
	
	public float getHeight(float screenHeight, float viewportHeight){
		return ((float) background.getHeight() / screenHeight) * viewportHeight;
	}
	
	public void setOriginY(float originY){
		this.originY = originY;
	}
	
	public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
	

}
