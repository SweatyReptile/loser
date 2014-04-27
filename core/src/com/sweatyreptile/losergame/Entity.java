package com.sweatyreptile.losergame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.World;
import com.sweatyreptile.losergame.fixtures.EntityFixtureDef;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;

public class Entity {

	protected World world;
	protected Sprite sprite;
	protected Body currentBody;
	
	protected float spriteWidth;
	protected float spriteHeight;
	protected float bodyWidth;
	protected float bodyHeight;
	
	protected float spriteOriginX;
	protected float spriteOriginY;
	
	protected String name;
	
	private ContactListener contactListener;
	
	public Entity(World world, ContactListener contactListener, BodyDef bodyDef, String name){
		this.sprite = new Sprite();
		currentBody = world.createBody(bodyDef);
		this.world = world;
		this.name = name;
		this.contactListener = contactListener;
	}
	
	public Entity(World world, ContactListener contactListener, BodyDef bodyDef, 
			EntityFixtureDef fixtureDef, float scale, 
			boolean flipped, String name) {
		
		this.name = name;
		this.contactListener = contactListener;
		
		currentBody = world.createBody(bodyDef);
		Texture spriteTexture = fixtureDef.getTexture();
		
		float spriteScale = scale;
		float bodyScale = 0f;
		if (bodyDef.type == BodyType.DynamicBody){
			bodyScale = 0.88f * scale;
		}
		else {
			bodyScale = scale;
		}
		fixtureDef.attach(currentBody, bodyScale, flipped);
		
		bodyWidth = 1f * bodyScale;
		bodyHeight = (float) spriteTexture.getHeight() * bodyScale / spriteTexture.getWidth();
		spriteWidth = 1f * spriteScale;
		spriteHeight = (float) spriteTexture.getHeight() * spriteScale / spriteTexture.getWidth();
		
		sprite = new Sprite(spriteTexture);
		sprite.setSize(spriteWidth, spriteHeight);
		
		spriteOriginX = (spriteWidth - bodyWidth) / 2;
		spriteOriginY = (spriteHeight - bodyHeight) / 2;
		
		sprite.setOrigin(spriteOriginX, spriteOriginY);
	}
	
	public Entity(World world, ContactListener contactListener, BodyDef bodyDef, 
			EntityFixtureDef fixtureDef, boolean flipped,
			float screenWidth, float viewportWidth, String name) {
		this(world, contactListener, bodyDef, fixtureDef, 
				fixtureDef.getTexture().getWidth() * viewportWidth / screenWidth,
				flipped, name);
	}
	
	public Entity(World world, BodyDef bodyDef, ContactListener contactListener,
			EntityFixtureDef fixtureDef, AssetManagerPlus assets, 
			String bodyName, float scale, String name) {
		this(world, contactListener, bodyDef, fixtureDef, scale, false, name);
	} 
	
	public void render(SpriteBatch renderer){
		sprite.draw(renderer);
	}
	
	public void update(float delta){
		updateSprite(delta);
	}
	
	private void updateSprite(float delta){
		Vector2 position = currentBody.getPosition();
		
		float spritex = position.x - spriteOriginX;
		float spritey = position.y - spriteOriginY;
		
		sprite.setPosition(spritex, spritey);
		sprite.setRotation(MathUtils.radiansToDegrees * currentBody.getAngle());
	}
	
	public void setX(float x){
		sprite.setX(x);
	}
	
	public void setY(float y){
		sprite.setY(y);
	}

	public String getName() {
		return name;
	}
}
