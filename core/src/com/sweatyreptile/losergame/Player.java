package com.sweatyreptile.losergame;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.sweatyreptile.losergame.fixtures.DuckFixtureDef;
import com.sweatyreptile.losergame.fixtures.DuckQuackFixtureDef;
import com.sweatyreptile.losergame.fixtures.DuckTopFixtureDef;
import com.sweatyreptile.losergame.fixtures.EntityFixtureDef;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;

public class Player extends Entity{
	
	private enum Direction{
		LEFT, RIGHT, NONE
	}
	
	private static final float MAX_VELOCITY = 1f;
	
	private Sprite standingSprite;
	private Sprite duckingSprite;
	private Sprite quackingSprite;
	private Sprite quackingDuckingSprite;
	
	private Body leftBody;
	private Body rightBody;
	private Body leftDuckingBody;
	private Body rightDuckingBody;
	private Body leftQuackingBody;
	private Body rightQuackingBody;
	private Body leftQuackingDuckingBody;
	private Body rightQuackingDuckingBody;
	
	private Body flightSensorBody;
	private WeldJoint flightSensorWeld;
	private float sensorHeight;

	private Direction movingDirection;
	private boolean flying;
	private boolean ducking;
	private boolean quacking;

	public Player(World world, BodyDef def, AssetManagerPlus assets) {
		super(world, def);
		DuckFixtureDef fixDef = new DuckFixtureDef(assets);
		DuckTopFixtureDef topFixDef = new DuckTopFixtureDef(assets);
		DuckQuackFixtureDef quackFixDef = new DuckQuackFixtureDef(assets);
		DuckQuackTopFixtureDef quackTopFixDef = new DuckQuackTopFixtureDef(assets);
		
		leftBody = world.createBody(def);
		rightBody = world.createBody(def);
		leftDuckingBody = world.createBody(def);
		rightDuckingBody = world.createBody(def);
		leftQuackingBody = world.createBody(def);
		rightQuackingBody = world.createBody(def);
		leftQuackingDuckingBody = world.createBody(def);
		rightQuackingDuckingBody = world.createBody(def);
		flightSensorBody = world.createBody(def);
		
		fixDef.attach(leftBody, .2f, false);
		fixDef.attach(rightBody, .2f, true);
		topFixDef.attach(leftDuckingBody, .2f, false);
		topFixDef.attach(rightDuckingBody, .2f, true);
		quackFixDef.attach(leftQuackingBody, .2f, false);
		quackFixDef.attach(rightQuackingBody, .2f, true);
		quackTopFixDef.attach(leftQuackingDuckingBody, .2f, false);
		quackTopFixDef.attach(rightQuackingDuckingBody, .2f, true);
		
		currentBody = leftBody;
		rightBody.setActive(false);
		leftDuckingBody.setActive(false);
		rightDuckingBody.setActive(false);
		leftQuackingBody.setActive(false);
		rightQuackingBody.setActive(false);
		leftQuackingDuckingBody.setActive(false);
		rightQuackingDuckingBody.setActive(false);
		movingDirection = Direction.NONE;
		
		standingSprite = new Sprite(fixDef.getTexture());
		duckingSprite = new Sprite(topFixDef.getTexture());
		quackingSprite = new Sprite(quackFixDef.getTexture());
		quackingDuckingSprite = new Sprite(quackTopFixDef.getTexture());
		standingSprite.setSize(.2f, .2f);
		duckingSprite.setSize(.2f, .16f);
		quackingSprite.setSize(.2f, .2f);
		quackingDuckingSprite.setSize(.2f, .16f);
		
		sprite = standingSprite;
		
		BodyDef flightSensorBodyDef = new BodyDef();
		flightSensorBodyDef.type = BodyType.DynamicBody;
		flightSensorBodyDef.position.set(def.position.x, def.position.y); //TODO move down
		flightSensorBody = world.createBody(flightSensorBodyDef);
		
		EntityFixtureDef flightSensorDef = new EntityFixtureDef(assets, "duck_flight_sensor");
		flightSensorDef.isSensor = true;
		flightSensorDef.attach(flightSensorBody, .2f, false);
		flightSensorBody.setUserData("flight_sensor");
		
		extractSensorHeight();
		weldSensor(currentBody);
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

	public void quack() {
		if (currentBody.equals(leftBody)) {
			switchBody(currentBody, leftQuackingBody);
			sprite = quackingSprite;
		}
		else if (currentBody.equals(rightBody)) {
			switchBody(currentBody, rightQuackingBody);
			sprite = quackingSprite;
		}
		else if (currentBody.equals(leftDuckingBody)){
			switchBody(currentBody, leftQuackingDuckingBody);
			sprite = quackingDuckingSprite;
		}
		else if (currentBody.equals(rightDuckingBody)){
			switchBody(currentBody, rightQuackingDuckingBody);
			sprite = quackingDuckingSprite;
		}
		quacking = true;
	}
	
	public void stopQuacking() {
		if (currentBody.equals(leftQuackingBody)) {
			switchBody(currentBody, leftBody);
			sprite = standingSprite;
		}
		else if (currentBody.equals(rightQuackingBody)) {
			switchBody(currentBody, rightBody);
			sprite = standingSprite;
		}
		else if (currentBody.equals(leftQuackingDuckingBody)){
			switchBody(currentBody, leftDuckingBody);
			sprite = duckingSprite;
		}
		else if (currentBody.equals(rightQuackingDuckingBody)){
			switchBody(currentBody, rightDuckingBody);
			sprite = duckingSprite;
		}
		quacking = false;
	}
	
	public void duck() {
		if (currentBody.equals(leftBody)) {
			switchBody(currentBody, leftDuckingBody);
			sprite = duckingSprite;
		}
		else if (currentBody.equals(rightBody)) {
			switchBody(currentBody, rightDuckingBody);
			sprite = duckingSprite;
		}
		else if (currentBody.equals(leftQuackingBody)){
			switchBody(currentBody, leftQuackingDuckingBody);
			sprite = quackingDuckingSprite;
		}
		else if (currentBody.equals(rightQuackingBody)){
			switchBody(currentBody, rightQuackingDuckingBody);
			sprite = quackingDuckingSprite;
		}
		ducking = true;
	}
	
	public void fly() {
		if (currentBody.equals(leftBody)) {
			switchBody(currentBody, leftDuckingBody);
			sprite = duckingSprite;
		}
		else if (currentBody.equals(rightBody)) {
			switchBody(currentBody, rightDuckingBody);
			sprite = duckingSprite;
		}
		else if (currentBody.equals(leftQuackingBody)){
			switchBody(currentBody, leftQuackingDuckingBody);
			sprite = quackingDuckingSprite;
		}
		else if (currentBody.equals(rightQuackingBody)){
			switchBody(currentBody, rightQuackingDuckingBody);
			sprite = quackingDuckingSprite;
		}
		flying = true;
	}
	
	public void land() {
		if (!ducking){
			if (currentBody.equals(leftDuckingBody)) {
				switchBody(currentBody, leftBody);
				sprite = standingSprite;
			}
			else if (currentBody.equals(rightDuckingBody)) {
				switchBody(currentBody, rightBody);
				sprite = standingSprite;
			}
			else if (currentBody.equals(leftQuackingDuckingBody)){
				switchBody(currentBody, leftQuackingBody);
				sprite = quackingSprite;
			}
			else if (currentBody.equals(rightQuackingDuckingBody)){
				switchBody(currentBody, rightQuackingBody);
				sprite = quackingSprite;
			}
		}
		flying = false;
	}
	
	public void standUp() {
		if (!flying){
			if (currentBody.equals(leftDuckingBody)) {
				switchBody(currentBody, leftBody);
				sprite = standingSprite;
			}
			else if (currentBody.equals(rightDuckingBody)) {
				switchBody(currentBody, rightBody);
				sprite = standingSprite;
			}
			else if (currentBody.equals(leftQuackingDuckingBody)){
				switchBody(currentBody, leftQuackingBody);
				sprite = quackingSprite;
			}
			else if (currentBody.equals(rightQuackingDuckingBody)){
				switchBody(currentBody, rightQuackingBody);
				sprite = quackingSprite;
			}
		}
		ducking = false;
	}
	
	public void moveLeft() {
		movingDirection = Direction.LEFT;
		if (quacking && (ducking || flying)) switchBody(currentBody, leftQuackingDuckingBody);
		else if (ducking || flying) switchBody(currentBody, leftDuckingBody);
		else if (quacking) switchBody(currentBody, leftQuackingBody);
		else switchBody(currentBody, leftBody);
		flipSprites(false);
	}
	
	public void moveRight() {
		movingDirection = Direction.RIGHT;
		if (quacking && (ducking || flying)) switchBody(currentBody, rightQuackingDuckingBody);
		else if (ducking || flying) switchBody(currentBody, rightDuckingBody);
		else if (quacking) switchBody (currentBody, rightQuackingBody);
		else switchBody(currentBody, rightBody);
		flipSprites(true);
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
	
	private void switchBody(Body oldBody, Body newBody){
		newBody.setTransform(oldBody.getPosition(), 0f);
		newBody.setLinearVelocity(oldBody.getLinearVelocity());
		oldBody.setActive(false);
		newBody.setActive(true);
		currentBody = newBody;
		weldSensor(currentBody);
	}

	private void weldSensor(Body newBody) {
		Vector2 bodyPosition = newBody.getPosition();
		flightSensorBody.setTransform(bodyPosition.x, bodyPosition.y - sensorHeight, newBody.getAngle());
		WeldJointDef weld = new WeldJointDef();
		weld.bodyA = currentBody;
		weld.bodyB = flightSensorBody;
		weld.initialize(currentBody, flightSensorBody, currentBody.getWorldCenter());
		if (flightSensorWeld != null) {
			world.destroyJoint(flightSensorWeld);
		}
		flightSensorWeld = (WeldJoint) world.createJoint(weld);
	}
	
	private void extractSensorHeight(){
		Vector2 vertex1 = new Vector2();
		Vector2 vertex2 = new Vector2();
		PolygonShape sensorShape = (PolygonShape) flightSensorBody.getFixtureList().get(0).getShape();
		sensorShape.getVertex(0, vertex1);
		sensorShape.getVertex(2, vertex2);
		sensorHeight = vertex1.y - vertex2.y;
	}

	private void flipSprites(boolean horizontal) {
		standingSprite.setFlip(horizontal, false);
		duckingSprite.setFlip(horizontal, false);
		quackingSprite.setFlip(horizontal, false);
		quackingDuckingSprite.setFlip(horizontal, false);
	}

	public Body getBody() {
		return currentBody;
	}
	
	public boolean isFlying(){
		return flying;
	}
	
	public boolean isDucking(){
		return ducking;
	}
	
}
