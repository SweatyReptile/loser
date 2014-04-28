package com.sweatyreptile.losergame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.sweatyreptile.losergame.fixtures.EntityFixtureDef;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;

public class Entity <T extends Entity<?>>{

	// Default size of screen (in pixels) relative to the size of 
	// the image used as the sprite for the entity (in pixels)
	public static final float DEFAULT_SCREEN_WIDTH = 1280;
	
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
	
	private LoserContactListener contactListener;
	
	public Entity(World world, LoserContactListener contactListener, BodyDef bodyDef, String name){
		this.sprite = new Sprite();
		currentBody = world.createBody(bodyDef);
		this.world = world;
		this.name = name;
		this.contactListener = contactListener;
		currentBody.setUserData(this);
	}
	
	public Entity(World world, LoserContactListener contactListener, BodyDef bodyDef, 
			EntityFixtureDef fixtureDef, float scale, 
			boolean flipped, String name) {
		
		this.name = name;
		this.contactListener = contactListener;
		
		currentBody = world.createBody(bodyDef);
		Texture spriteTexture = fixtureDef.getTexture();
		
		float spriteScale = scale;
		float bodyScale = 0f;
		if (bodyDef.type == BodyType.DynamicBody){
			bodyScale = 0.84f * scale;
		}
		else {
			bodyScale = scale;
		}
		fixtureDef.attach(currentBody, bodyScale, flipped);
		
		sprite = new Sprite(spriteTexture);		
		setSpriteOrigin(spriteTexture, spriteScale, bodyScale);
		sprite.setSize(spriteWidth, spriteHeight);
		
		sprite.setOrigin(spriteOriginX, spriteOriginY);
		
		currentBody.setUserData(this);
	}
	
	public Entity(World world, LoserContactListener contactListener, BodyDef bodyDef, 
			EntityFixtureDef fixtureDef, boolean flipped,
			float screenWidth, float viewportWidth, String name) {
		this(world, contactListener, bodyDef, fixtureDef, 
				fixtureDef.getTexture().getWidth() * viewportWidth / screenWidth,
				flipped, name);
	}
	
	public Entity(World world, BodyDef bodyDef, LoserContactListener contactListener,
			EntityFixtureDef fixtureDef, AssetManagerPlus assets, 
			String bodyName, float scale, String name) {
		this(world, contactListener, bodyDef, fixtureDef, scale, false, name);
	} 
	
	protected void setSpriteOrigin(Texture texture, float spriteScale,
			float bodyScale) {
		spriteWidth = 1f * spriteScale;
		spriteHeight = (float)texture.getHeight() * spriteScale / texture.getWidth();
		bodyWidth = 1f * bodyScale;
		bodyHeight = (float) texture.getHeight() * bodyScale / texture.getWidth();
		spriteOriginX = (spriteWidth - bodyWidth) / 2;
		spriteOriginY = (spriteHeight - bodyHeight) / 2;
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
	
	public void addListener(EntityListener<T> listener){
		contactListener.addEntityListener(name, listener);
	}
	
}
