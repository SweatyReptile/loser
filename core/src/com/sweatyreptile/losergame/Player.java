package com.sweatyreptile.losergame;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
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
	private Sprite quackingDuckingSprite;
	
	private Body leftBody;
	private Body rightBody;
	private Body leftDuckingBody;
	private Body rightDuckingBody;
	private Body leftQuackingBody;
	private Body rightQuackingBody;
	private Body leftQuackingDuckingBody;
	private Body rightQuackingDuckingBody;
	
	private ArrayList<Sensor> sensors;

	private Body grabbedObject;
	private WeldJoint grabWeld;

	private Direction movingDirection;
	private boolean flying;
	private boolean ducking;
	private boolean quacking;
	
	private Sound quackSound;
	
	private SensorContactListener contactListener;

	public Player(World world, BodyDef def, AssetManagerPlus assets, SensorContactListener contactListener) {
		super(world, def);
		this.contactListener = contactListener;
		
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
		
		leftBody.setUserData(new String[]{"left"});
		rightBody.setUserData(new String[]{"right"});
		leftDuckingBody.setUserData(new String[]{"left", "duck"});
		rightDuckingBody.setUserData(new String[]{"right", "duck"});
		leftQuackingBody.setUserData(new String[]{"left", "quack"});
		rightQuackingBody.setUserData(new String[]{"right", "quack"});
		rightQuackingDuckingBody.setUserData(new String[]{"right", "quack", "duck"});
		leftQuackingDuckingBody.setUserData(new String[]{"left", "quack", "duck"});

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
		
		CountingSensorListener flightSensorListener = new CountingSensorListener() {
			@Override public void contactAdded(int totalContacts){}
			@Override public void contactRemoved(int totalContacts) {
				if (totalContacts == 0 && !isFlying()) fly();
			}
		};
		
		CountingSensorListener landingSensorListener = new CountingSensorListener() {
			@Override public void contactRemoved(int totalContacts) {}
			@Override public void contactAdded(int totalContacts) {
				if (totalContacts != 0 && isFlying()) land();
			}
		};
		
		
		sensors = new ArrayList<Sensor>();
		Sensor flightSensor = new CountingSensor(contactListener, flightSensorListener, world, assets, "duck_flight_sensor", .2f, 0, 2);
		Sensor landingSensor = new CountingSensor(contactListener, landingSensorListener, world, assets, "duck_landing_sensor", .2f, 0, 2);
		//TODO: Sensor grabSensor = new Sensor(world, assets, "duck_grab_sensor", .2f, "grab_sensor", 2, 0);
		Collections.addAll(sensors, flightSensor, landingSensor);//, grabSensor);
		for (Sensor sensor : sensors) sensor.weld(world, currentBody);
		
		quackSound = assets.get("quack_dummy.ogg");
		
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
	
	private void weldToDuck(Body object){
		WeldJointDef grabObjectWeld = new WeldJointDef();
		grabObjectWeld.bodyA = currentBody;
		grabObjectWeld.bodyB = object;
		grabObjectWeld.initialize(currentBody, object, currentBody.getWorldCenter());
		if (grabWeld != null) {
			world.destroyJoint(grabWeld);
		}
		grabWeld = (WeldJoint) world.createJoint(grabObjectWeld);
	}
	
	private void destroyGrabWeld(){
		if (grabWeld != null) {
			world.destroyJoint(grabWeld);
			grabWeld = null;
			grabbedObject = null;
		}
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
		
		Body currentGrabBody = contactListener.getCurrentGrabBody();
		if (currentGrabBody != null && currentGrabBody.getType().equals(BodyType.DynamicBody)){
			grabbedObject = currentGrabBody;
			weldToDuck(grabbedObject);
		}
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
		if (grabbedObject != null) destroyGrabWeld();
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
		quackSound.play();
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
		
		if (grabbedObject != null){
			Vector2 grabPos = grabbedObject.getPosition();
			Vector2 grabVelocity = grabbedObject.getLinearVelocity();
			grabbedObject.setLinearVelocity(grabVelocity.x, 0f);
			grabbedObject.applyLinearImpulse(0, grabbedObject.getMass()*2, grabPos.x, grabPos.y, true);
		}
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
		for (Sensor sensor : sensors) sensor.weld(world, currentBody);
		if (grabbedObject != null) weldToDuck(grabbedObject);
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
