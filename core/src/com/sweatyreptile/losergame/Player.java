package com.sweatyreptile.losergame;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.sweatyreptile.losergame.fixtures.DuckFixtureDef;
import com.sweatyreptile.losergame.fixtures.DuckQuackFixtureDef;
import com.sweatyreptile.losergame.fixtures.DuckTopFixtureDef;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;

public class Player extends Entity{
	
	private enum Direction{
		LEFT, RIGHT, NONE
	}
	
	private static final float MAX_VELOCITY = 1f;
	
	private Sprite standingSprite;
	private Sprite duckingSprite;
	private Sprite quackingSprite;
	
	private Body leftBody;
	private Body rightBody;
	private Body leftDuckingBody;
	private Body rightDuckingBody;
	private Body leftQuackingBody;
	private Body rightQuackingBody;
	
	private Direction movingDirection;
	private boolean ducking;
	private boolean quacking;

	public Player(World world, BodyDef def, AssetManagerPlus assets) {
		super(world, def);
		DuckFixtureDef fixDef = new DuckFixtureDef(assets);
		DuckTopFixtureDef topFixDef = new DuckTopFixtureDef(assets);
		DuckQuackFixtureDef quackFixDef = new DuckQuackFixtureDef(assets);
		
		leftBody = world.createBody(def);
		rightBody = world.createBody(def);
		leftDuckingBody = world.createBody(def);
		rightDuckingBody = world.createBody(def);
		leftQuackingBody = world.createBody(def);
		rightQuackingBody = world.createBody(def);
		
		fixDef.attach(leftBody, .2f, false);
		fixDef.attach(rightBody, .2f, true);
		topFixDef.attach(leftDuckingBody, .2f, false);
		topFixDef.attach(rightDuckingBody, .2f, true);
		quackFixDef.attach(leftQuackingBody, .2f, false);
		quackFixDef.attach(rightQuackingBody, .2f, true);
		
		currentBody = leftBody;
		rightBody.setActive(false);
		leftDuckingBody.setActive(false);
		rightDuckingBody.setActive(false);
		leftQuackingBody.setActive(false);
		rightQuackingBody.setActive(false);
		movingDirection = Direction.NONE;
		
		standingSprite = new Sprite(fixDef.getTexture());
		duckingSprite = new Sprite(topFixDef.getTexture());
		quackingSprite = new Sprite(quackFixDef.getTexture());
		standingSprite.setSize(.2f, .2f);
		duckingSprite.setSize(.2f, .16f);
		quackingSprite.setSize(.2f, .2f);
		
		sprite = standingSprite;
	}
	
	public void quack() {
		if (currentBody.equals(leftBody)) {
			switchBody(currentBody, leftQuackingBody);
		}
		else if (currentBody.equals(rightBody)) {
			switchBody(currentBody, rightQuackingBody);
		}
		quacking = true;
		sprite = quackingSprite;
	}
	
	public void stopQuacking() {
		if (currentBody.equals(leftQuackingBody)) {
			switchBody(currentBody, leftBody);
		}
		else if (currentBody.equals(rightQuackingBody)) {
			switchBody(currentBody, rightBody);
		}
		quacking = false;
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
	
	public void standUp() {
		if (currentBody.equals(leftDuckingBody)) {
			switchBody(currentBody, leftBody);
		}
		else if (currentBody.equals(rightDuckingBody)) {
			switchBody(currentBody, rightBody);
		}
		ducking = false;
		sprite = standingSprite;
	}
	
	public void moveLeft() {
		movingDirection = Direction.LEFT;
		if (!ducking) switchBody(currentBody, leftBody);
		else switchBody(currentBody, leftDuckingBody);
		flipSprites(false);
	}
	
	public void moveRight() {
		movingDirection = Direction.RIGHT;
		if (!ducking) switchBody(currentBody, rightBody);
		else switchBody(currentBody, rightDuckingBody);
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
		standingSprite.setFlip(horizontal, false);
		duckingSprite.setFlip(horizontal, false);
		quackingSprite.setFlip(horizontal, false);
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
			Vector2 velocity = currentBody.getLinearVelocity();
			if (movingDirection == Direction.LEFT) {
				if (!ducking) currentBody.setLinearVelocity(-MAX_VELOCITY, velocity.y);
				else currentBody.setLinearVelocity(-MAX_VELOCITY/2, velocity.y);
				
			}
			else if (movingDirection == Direction.RIGHT) {
				if (!ducking) currentBody.setLinearVelocity(MAX_VELOCITY, velocity.y);
				else currentBody.setLinearVelocity(MAX_VELOCITY/2, velocity.y);

			}
		}
	}
	
	public Body getBody() {
		return currentBody;
	}
	
}
