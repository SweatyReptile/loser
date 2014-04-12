package com.sweatyreptile.losergame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;

public class Entity {

	protected Sprite sprite;
	protected Body currentBody;
	
	protected float spriteWidth;
	protected float spriteHeight;
	protected float bodyWidth;
	protected float bodyHeight;
	
	public Entity(World world, BodyDef bodyDef){
		this.sprite = new Sprite();
		currentBody = world.createBody(bodyDef);
	}
	
	public Entity(World world, BodyDef bodyDef, 
			EntityFixtureDef fixtureDef, float scale, 
			boolean flipped) {
		
		float spriteScale = scale;
		float bodyScale = 0.92f * scale;
		
		currentBody = world.createBody(bodyDef);
		fixtureDef.attach(currentBody, bodyScale, flipped);
		Texture spriteTexture = fixtureDef.getTexture();
		
		bodyWidth = 1f * bodyScale;
		bodyHeight = (float) spriteTexture.getHeight() * scale / spriteTexture.getWidth();
		spriteWidth = 1f * spriteScale;
		spriteHeight = (float) spriteTexture.getHeight() * spriteScale / spriteTexture.getWidth();
		
		sprite = new Sprite(spriteTexture);
		sprite.setSize(spriteWidth, spriteHeight);
		sprite.setOrigin(0f, 0f);
	}
	
	public Entity(World world, BodyDef bodyDef, 
			EntityFixtureDef fixtureDef, AssetManagerPlus assets, 
			String bodyName, float scale) {
		this(world, bodyDef, fixtureDef, scale, false);
	} 
	
	public void render(SpriteBatch renderer){
		sprite.draw(renderer);
	}
	
	public void update(float delta){
		updateSprite(delta);
	}
	
	private void updateSprite(float delta){
		Vector2 position = currentBody.getPosition();
		
		float spritex = position.x - (spriteWidth - bodyWidth) / 2;
		float spritey = position.y - (spriteHeight - bodyHeight) / 2;
		
		sprite.setPosition(spritex, spritey);
		sprite.setRotation(MathUtils.radiansToDegrees * currentBody.getAngle());
	}
	
	public void setX(float x){
		sprite.setX(x);
	}
	
	public void setY(float y){
		sprite.setY(y);
	}
}
