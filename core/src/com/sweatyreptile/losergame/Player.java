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
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;

public class Player extends Entity{
	
	private enum Direction{
		LEFT, RIGHT, NONE
	}
	
	private static final float MAX_VELOCITY = 1f;
	
	private Body leftBody;
	private Body rightBody;
	private Direction movingDirection;

	public Player(World world, BodyDef def, AssetManagerPlus assets) {
		super(world, def);
		DuckFixtureDef fixDef = new DuckFixtureDef(assets);
		leftBody = world.createBody(def);
		rightBody = world.createBody(def);
		fixDef.attach(leftBody, .2f, true);
		fixDef.attach(rightBody, .2f, false);
		currentBody = rightBody;
		leftBody.setActive(false);
		movingDirection = Direction.NONE;
		
		sprite = new Sprite(fixDef.getTexture());
		
		sprite.setSize(.2f, .147f);
	}
	
	public void moveLeft() {
		movingDirection = Direction.LEFT;
		switchBody(currentBody, leftBody);
		sprite.setFlip(true, false);
	}
	
	public void moveRight() {
		movingDirection = Direction.RIGHT;
		switchBody(currentBody, rightBody);
		sprite.setFlip(false, false);
	}
	
	public void switchBody(Body oldBody, Body newBody){
		newBody.setTransform(oldBody.getPosition(), 0f);
		newBody.setLinearVelocity(oldBody.getLinearVelocity());
		oldBody.setActive(false);
		newBody.setActive(true);
		currentBody = newBody;
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
		}
	}
	
	public void stopMovingRight(){
		if (movingDirection == Direction.RIGHT){
			movingDirection = Direction.NONE;
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
