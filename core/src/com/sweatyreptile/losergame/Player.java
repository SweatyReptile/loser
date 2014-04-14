package com.sweatyreptile.losergame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.sweatyreptile.losergame.fixtures.DuckFixtureDef;
import com.sweatyreptile.losergame.fixtures.DuckTopFixtureDef;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;

public class Player extends Entity{
	
	private enum Direction{
		LEFT, RIGHT, NONE
	}
	
	private static final float MAX_VELOCITY = 1f;
	
	private Sprite standingSprite;
	private Sprite duckingSprite;
	
	private Body leftBody;
	private Body rightBody;
	private Body leftDuckingBody;
	private Body rightDuckingBody;
	
	private Direction movingDirection;
	private boolean ducking;

	public Player(World world, BodyDef def, AssetManagerPlus assets) {
		super(world, def);
		DuckFixtureDef fixDef = new DuckFixtureDef(assets);
		DuckTopFixtureDef topFixDef = new DuckTopFixtureDef(assets);
		
		leftBody = world.createBody(def);
		rightBody = world.createBody(def);
		leftDuckingBody = world.createBody(def);
		rightDuckingBody = world.createBody(def);
		
		fixDef.attach(leftBody, .2f, false);
		fixDef.attach(rightBody, .2f, true);
		topFixDef.attach(leftDuckingBody, .2f, false);
		topFixDef.attach(rightDuckingBody, .2f, true);
		
		currentBody = rightBody;
		leftBody.setActive(false);
		leftDuckingBody.setActive(false);
		rightDuckingBody.setActive(false);
		movingDirection = Direction.NONE;
		
		standingSprite = new Sprite(fixDef.getTexture());
		duckingSprite = new Sprite(topFixDef.getTexture());
		standingSprite.setSize(.2f, .2f);
		duckingSprite.setSize(.2f, .16f);
		
		sprite = standingSprite;
	}
	
	public void duck() {
		if (currentBody.equals(leftBody)) {
			switchBody(currentBody, leftDuckingBody);
		}
		else if (currentBody.equals(rightBody)) {
			switchBody(currentBody, rightDuckingBody);
		}
		ducking = true;
		sprite = duckingSprite;
	}
	
	public void moveLeft() {
		movingDirection = Direction.LEFT;
		switchBody(currentBody, leftBody);
		flipSprites(false);
	}
	
	public void moveRight() {
		movingDirection = Direction.RIGHT;
		switchBody(currentBody, rightBody);
		flipSprites(true);
	}
	
	public void switchBody(Body oldBody, Body newBody){
		newBody.setTransform(oldBody.getPosition(), 0f);
		newBody.setLinearVelocity(oldBody.getLinearVelocity());
		oldBody.setActive(false);
		newBody.setActive(true);
		currentBody = newBody;
	}
	
	public void flipSprites(boolean horizontal) {
		sprite.setFlip(horizontal, false);
		duckingSprite.setFlip(horizontal, false);
	}
	
	public void jump() {
		Vector2 position = currentBody.getPosition();
		Vector2 velocity = currentBody.getLinearVelocity();
		currentBody.setLinearVelocity(velocity.x, 0f);
		currentBody.applyLinearImpulse(0, 0.5f, position.x, position.y, true);
	}
	
	public void stopMovingLeft() {
		if (movingDirection == Direction.LEFT){
			movingDirection = Direction.NONE;
			Vector2 velocity = currentBody.getLinearVelocity();
			currentBody.setLinearVelocity(velocity.x*2/3, velocity.y);
		}
	}
	
	public void stopMovingRight(){
		if (movingDirection == Direction.RIGHT){
			movingDirection = Direction.NONE;
			Vector2 velocity = currentBody.getLinearVelocity();
			currentBody.setLinearVelocity(velocity.x*2/3, velocity.y);
		}
	}
	
	public void update(float delta) {
		super.update(delta);
		if (movingDirection != Direction.NONE) {
			Vector2 position = currentBody.getPosition();
			Vector2 velocity = currentBody.getLinearVelocity();
			if (movingDirection == Direction.LEFT) {
				currentBody.setLinearVelocity(-MAX_VELOCITY, velocity.y);
				
			}
			else if (movingDirection == Direction.RIGHT) {
				currentBody.setLinearVelocity(MAX_VELOCITY, velocity.y);
			}
		}
	}
	
	public Body getBody() {
		return currentBody;
	}
	
}
