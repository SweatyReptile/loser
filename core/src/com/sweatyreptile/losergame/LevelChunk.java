package com.sweatyreptile.losergame;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class LevelChunk {
	
	protected float viewportWidth;
	protected float viewportHeight;
	protected float screenWidth;
	protected float screenHeight;
	protected float originY;
	protected Map<String, Entity<?>> chunkEntities;
	protected Texture background;
	float bgWidth;
	float bgHeight;
	
	public LevelChunk(float viewportWidth, float viewportHeight, float screenWidth, float screenHeight){
		this.viewportHeight = viewportHeight;
		this.viewportWidth = viewportWidth;
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		chunkEntities = new HashMap<String, Entity<?>>();
		setup();
	}
	
	protected abstract void setup();
	
	public void renderBackground(float delta, SpriteBatch renderer) { 
		renderer.draw(background, 0, originY, viewportWidth, viewportHeight); //needs fixin TODO
	}
	
	public void renderEntities(float delta, SpriteBatch renderer) {
		for (Entity<?> entity : chunkEntities.values()){
			entity.render(renderer);
		}
		
	}

	public void update(float delta) {
		for (Entity<?> entity : chunkEntities.values()){
			entity.update(delta);
		}
		
	}
	
	public void updateEntityPositions(){ //Only works in vertical direction right now
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
	
	public float getHeight(){
		return bgHeight;
	}
	
	public void setOriginY(float originY){
		this.originY = originY;
	}
	
	public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
	
	protected void setBackground(Texture bg){
		background = bg;
		bgHeight = ((float) background.getHeight() / screenHeight) * viewportHeight;
		bgWidth = ((float) background.getWidth() / screenWidth) * viewportWidth;
	}
	

}
