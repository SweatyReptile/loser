package com.sweatyreptile.losergame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public class Player {
	
	private enum Direction{
		LEFT, RIGHT, NONE
	}
	
	private static final float MAX_VELOCITY = 1f;
	private Body playerBody;
	private Direction movingDirection;

	public Player(World world, BodyDef def) {
		playerBody = world.createBody(def);
		movingDirection = Direction.NONE;
	}
	
	public void moveLeft() {
		movingDirection = Direction.LEFT;
	}
	
	public void moveRight() {
		movingDirection = Direction.RIGHT;
	}
	
	public void jump() {
		Vector2 position = playerBody.getPosition();
		Vector2 velocity = playerBody.getLinearVelocity();
		playerBody.setLinearVelocity(velocity.x, 0f);
		playerBody.applyLinearImpulse(0, 0.5f, position.x, position.y, true);
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
		if (movingDirection != Direction.NONE) {
			Vector2 position = playerBody.getPosition();
			Vector2 velocity = playerBody.getLinearVelocity();
			if (movingDirection == Direction.LEFT) {
				playerBody.setLinearVelocity(-MAX_VELOCITY, velocity.y);
				
			}
			else if (movingDirection == Direction.RIGHT) {
				playerBody.setLinearVelocity(MAX_VELOCITY, velocity.y);
			}
		}
	}
	
	public Body getBody() {
		return playerBody;
	}
	
}
