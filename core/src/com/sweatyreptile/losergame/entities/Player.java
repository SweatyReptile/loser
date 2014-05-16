package com.sweatyreptile.losergame.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.sun.org.apache.xpath.internal.operations.And;
import com.sweatyreptile.losergame.DuckQuackTopFixtureDef;
import com.sweatyreptile.losergame.Entity;
import com.sweatyreptile.losergame.EntityListener;
import com.sweatyreptile.losergame.FixtureWrapper;
import com.sweatyreptile.losergame.LoserContactListener;
import com.sweatyreptile.losergame.fixtures.DuckFixtureDef;
import com.sweatyreptile.losergame.fixtures.DuckQuackFixtureDef;
import com.sweatyreptile.losergame.fixtures.DuckTopFixtureDef;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;
import com.sweatyreptile.losergame.sensors.ContentSensor;
import com.sweatyreptile.losergame.sensors.CountingSensor;
import com.sweatyreptile.losergame.sensors.CountingSensorListener;
import com.sweatyreptile.losergame.sensors.Sensor;

public class Player extends Entity<Player>{
	
	private static final float DUCK_SPRITE_SIZE = .2f;
	private static final float DUCK_BODY_SIZE = .88f * DUCK_SPRITE_SIZE;
	private static final float DUCKING_SPRITE_HEIGHT = .16f;

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

	private ContentSensor grabSensor;
	
	private PlayerContactFixerListener contactFixer;
	
	private float bodyHeight; //Assumes all standing bodies are the same height
	private float duckingBodyHeight; //Assumes all ducking bodies are the same height

	public Player(World world, LoserContactListener contactListener, BodyDef def, AssetManagerPlus assets) {
		super(world, contactListener, def, "duck");
		
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
		
		setSpriteOrigin(fixDef.getTexture(), DUCK_SPRITE_SIZE, DUCK_BODY_SIZE);
		
		leftBody.setUserData(this);
		rightBody.setUserData(this);
		leftDuckingBody.setUserData(this);
		rightDuckingBody.setUserData(this);
		leftQuackingBody.setUserData(this);
		rightQuackingBody.setUserData(this);
		leftQuackingDuckingBody.setUserData(this);
		rightQuackingDuckingBody.setUserData(this);
		
		fixDef.attach(leftBody, DUCK_BODY_SIZE, false);
		fixDef.attach(rightBody, DUCK_BODY_SIZE, true);
		topFixDef.attach(leftDuckingBody, DUCK_BODY_SIZE, false);
		topFixDef.attach(rightDuckingBody, DUCK_BODY_SIZE, true);
		quackFixDef.attach(leftQuackingBody, DUCK_BODY_SIZE, false);
		quackFixDef.attach(rightQuackingBody, DUCK_BODY_SIZE, true);
		quackTopFixDef.attach(leftQuackingDuckingBody, DUCK_BODY_SIZE, false);
		quackTopFixDef.attach(rightQuackingDuckingBody, DUCK_BODY_SIZE, true);
		
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
		standingSprite.setSize(DUCK_SPRITE_SIZE, DUCK_SPRITE_SIZE);
		duckingSprite.setSize(DUCK_SPRITE_SIZE, DUCKING_SPRITE_HEIGHT);
		quackingSprite.setSize(DUCK_SPRITE_SIZE, DUCK_SPRITE_SIZE);
		quackingDuckingSprite.setSize(DUCK_SPRITE_SIZE, DUCKING_SPRITE_HEIGHT);
		
		sprite = standingSprite;
		
		contactFixer = new PlayerContactFixerListener();
		addListener(contactFixer);
		
		CountingSensorListener flightSensorListener = new CountingSensorListener() {
			@Override public void contactAdded(int totalContacts){}
			@Override public void contactRemoved(int totalContacts) {
				if (totalContacts == 0 && !isFlying()) {
					fly();
				}
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
		grabSensor = new ContentSensor(contactListener, null, world, assets, "duck_grab_sensor", .2f, 0, 3);
		Collections.addAll(sensors, flightSensor, landingSensor, grabSensor);
		for (Sensor sensor : sensors) sensor.weld(world, currentBody);
		quackSound = assets.get("quack_dummy.ogg");
		
		bodyHeight = extractBodyHeight(leftBody);
		duckingBodyHeight = extractBodyHeight(leftDuckingBody);
		
	}
	
	public void update(float delta) {

		for (Sensor sensor : sensors) {
			sensor.update(delta);
		}
		
		super.update(delta);
		if (isMoving()) {
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
		if (!isMoving() && isFlying()) {
			Vector2 velocity = currentBody.getLinearVelocity();
			if (Float.compare(velocity.x, 0f) != 0) {
				currentBody.applyForceToCenter(-velocity.x/3, 0f, true);
			}
		}
		
	}
	
	public boolean isMoving() {
		return movingDirection != Direction.NONE;
	}
	
	public boolean hasVelocity(){
		Vector2 velocity = getBody().getLinearVelocity();
		int x = Float.compare(velocity.x, 0f);
		int y = Float.compare(velocity.y, 0f);
		if (x != 0 || y != 0f){
			return true;
		}
		return false;
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
			switchBody(currentBody, leftDuckingBody, false);
			sprite = duckingSprite;
		}
		else if (currentBody.equals(rightBody)) {
			switchBody(currentBody, rightDuckingBody, false);
			sprite = duckingSprite;
		}
		else if (currentBody.equals(leftQuackingBody)){
			switchBody(currentBody, leftQuackingDuckingBody, false);
			sprite = quackingDuckingSprite;
		}
		else if (currentBody.equals(rightQuackingBody)){
			switchBody(currentBody, rightQuackingDuckingBody, false);
			sprite = quackingDuckingSprite;
		}
		ducking = true;
		
		if (!grabSensor.isEmpty()) {
			Body currentGrabBody = grabSensor.getNewestBody();
			if (currentGrabBody.getType().equals(BodyType.DynamicBody)) {
				grabbedObject = currentGrabBody;
				weldToDuck(grabbedObject);
			}
		}
	}
	
	public void standUp() {
		if (!flying){
			if (currentBody.equals(leftDuckingBody)) {
				switchBody(currentBody, leftBody, false);
				sprite = standingSprite;
			}
			else if (currentBody.equals(rightDuckingBody)) {
				switchBody(currentBody, rightBody, false);
				sprite = standingSprite;
			}
			else if (currentBody.equals(leftQuackingDuckingBody)){
				switchBody(currentBody, leftQuackingBody, false);
				sprite = quackingSprite;
			}
			else if (currentBody.equals(rightQuackingDuckingBody)){
				switchBody(currentBody, rightQuackingBody, false);
				sprite = quackingSprite;
			}
		}
		ducking = false;
		if (grabbedObject != null) destroyGrabWeld();
	}

	public void quack() {
		if (currentBody.equals(leftBody)) {
			switchBody(currentBody, leftQuackingBody, false);
			sprite = quackingSprite;
		}
		else if (currentBody.equals(rightBody)) {
			switchBody(currentBody, rightQuackingBody, false);
			sprite = quackingSprite;
		}
		else if (currentBody.equals(leftDuckingBody)){
			switchBody(currentBody, leftQuackingDuckingBody, false);
			sprite = quackingDuckingSprite;
		}
		else if (currentBody.equals(rightDuckingBody)){
			switchBody(currentBody, rightQuackingDuckingBody, false);
			sprite = quackingDuckingSprite;
		}
		quacking = true;
		quackSound.play();
		Timer.schedule(new Timer.Task() {
			@Override
			public void run() {
				stopQuacking();
			}
		}, 0.214f); // Length of quacking sound effect in seconds
		talk("*quack lol*");
	}
	
	public void stopQuacking() {
		if (currentBody.equals(leftQuackingBody)) {
			switchBody(currentBody, leftBody, false);
			sprite = standingSprite;
		}
		else if (currentBody.equals(rightQuackingBody)) {
			switchBody(currentBody, rightBody, false);
			sprite = standingSprite;
		}
		else if (currentBody.equals(leftQuackingDuckingBody)){
			switchBody(currentBody, leftDuckingBody, false);
			sprite = duckingSprite;
		}
		else if (currentBody.equals(rightQuackingDuckingBody)){
			switchBody(currentBody, rightDuckingBody, false);
			sprite = duckingSprite;
		}
		quacking = false;
	}
	
	public void fly() {
		if (currentBody.equals(leftBody)) {
			switchBody(currentBody, leftDuckingBody, true);
			sprite = duckingSprite;
		}
		else if (currentBody.equals(rightBody)) {
			switchBody(currentBody, rightDuckingBody, true);
			sprite = duckingSprite;
		}
		else if (currentBody.equals(leftQuackingBody)){
			switchBody(currentBody, leftQuackingDuckingBody, true);
			sprite = quackingDuckingSprite;
		}
		else if (currentBody.equals(rightQuackingBody)){
			switchBody(currentBody, rightQuackingDuckingBody, true);
			sprite = quackingDuckingSprite;
		}
		flying = true;
	}
	
	public void land() {
		if (!ducking){
			if (currentBody.equals(leftDuckingBody)) {
				switchBody(currentBody, leftBody, false);
				sprite = standingSprite;
			}
			else if (currentBody.equals(rightDuckingBody)) {
				switchBody(currentBody, rightBody, false);
				sprite = standingSprite;
			}
			else if (currentBody.equals(leftQuackingDuckingBody)){
				switchBody(currentBody, leftQuackingBody, false);
				sprite = quackingSprite;
			}
			else if (currentBody.equals(rightQuackingDuckingBody)){
				switchBody(currentBody, rightQuackingBody, false);
				sprite = quackingSprite;
			}
		}
		flying = false;
	}
	
	public void moveLeft() {
		movingDirection = Direction.LEFT;
		if (quacking && (ducking || flying)) switchBody(currentBody, leftQuackingDuckingBody, false);
		else if (ducking || flying) switchBody(currentBody, leftDuckingBody, false);
		else if (quacking) switchBody(currentBody, leftQuackingBody, false);
		else switchBody(currentBody, leftBody, false);
		flipSprites(false);
	}
	
	public void moveRight() {
		movingDirection = Direction.RIGHT;
		if (quacking && (ducking || flying)) switchBody(currentBody, rightQuackingDuckingBody, false);
		else if (ducking || flying) switchBody(currentBody, rightDuckingBody, false);
		else if (quacking) switchBody (currentBody, rightQuackingBody, false);
		else switchBody(currentBody, rightBody, false);
		flipSprites(true);
	}
	
	public void jump() {
		Vector2 position = currentBody.getPosition();
		Vector2 velocity = currentBody.getLinearVelocity();
		currentBody.setLinearVelocity(velocity.x, 0f);
		currentBody.applyLinearImpulse(0, 0.40f, position.x, position.y, true);
		
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
	
	public void stopJumping() {
		Vector2 velocity = currentBody.getLinearVelocity();
		currentBody.setLinearVelocity(velocity.x, velocity.y*1/2);
		
	}

	private void switchBody(Body oldBody, Body newBody, boolean fromTop){
		setTransform(newBody, fromTop);
		newBody.setLinearVelocity(oldBody.getLinearVelocity());
		oldBody.setActive(false);
		contactFixer.flushContacts(contactListener);
		newBody.setActive(true);
		currentBody = newBody;
		for (Sensor sensor : sensors) sensor.weld(world, currentBody);
		if (grabbedObject != null) weldToDuck(grabbedObject);
	}
	
	private void setTransform(Body body, boolean fromTop){
		if (fromTop){
			Vector2 currentPos = currentBody.getPosition();
			float x = 0;
			if (ducking || flying) x = currentPos.x;
			else x = currentPos.x + bodyHeight - duckingBodyHeight;
			body.setTransform(x, currentPos.y, 0f);
		}
		else body.setTransform(currentBody.getPosition(), 0f);
	}
	
	public void standRight(){
		switchBody(currentBody, rightBody, false);
		flipSprites(true);
	}
	
	private float extractBodyHeight(Body body){
		Vector2 currentPos = currentBody.getPosition();
		float lowestValue = currentPos.x;
		float highestValue = currentPos.x;
		Array<Fixture> fixtures = body.getFixtureList();
		for (Fixture fixture : fixtures){
			PolygonShape shape = (PolygonShape) fixture.getShape();
			for (int index = 0; index < shape.getVertexCount(); index++){
				Vector2 vertex = new Vector2();
				shape.getVertex(index, vertex);
				float x = vertex.x;
				if (x > highestValue) highestValue = x;
			}
		}
		return highestValue - lowestValue;
	}

	private void flipSprites(boolean horizontal) {
		standingSprite.setFlip(horizontal, false);
		duckingSprite.setFlip(horizontal, false);
		quackingSprite.setFlip(horizontal, false);
		quackingDuckingSprite.setFlip(horizontal, false);
	}
	
	public boolean isFlying(){
		return flying;
	}
	
	public boolean isDucking(){
		return ducking;
	}
	
	public boolean isQuacking(){
		return quacking;
	}
	
	private class PlayerContactFixerListener implements EntityListener<Player>{
		
		// Holds current player contacts so that they may be flushed
		// when the player switches bodies
		private Stack<Array<FixtureWrapper>> startedContacts = new Stack<Array<FixtureWrapper>>();
		
		@Override
		public void beginContact(Player entity, FixtureWrapper entityFixture, FixtureWrapper contacted) {
			Array<FixtureWrapper> collisionCouple = new Array<FixtureWrapper>(2);
			collisionCouple.add(entityFixture);
			collisionCouple.add(contacted);
			startedContacts.push(collisionCouple);
		}

		@Override
		public void endContact(Player entity, FixtureWrapper entityFixture, FixtureWrapper contacted) {
			for (int i = 0; i < startedContacts.size(); i++){
				Array<FixtureWrapper> collisionCouple = startedContacts.get(i);
				FixtureWrapper storedEntityFixture = collisionCouple.get(0);
				FixtureWrapper storedContactedFixture = collisionCouple.get(1);
				if (entityFixture.equals(storedEntityFixture) &&
						contacted.equals(storedContactedFixture)){
					startedContacts.remove(collisionCouple);
					break; // We only need remove one instance of these
					       // fixtures because only one contact was ended
				}
			}
		}
		
		public void flushContacts(LoserContactListener contactListener){
			while(!startedContacts.isEmpty()){
				Array<FixtureWrapper> group = startedContacts.pop();
				FixtureWrapper storedEntityFixture = group.get(0);
				FixtureWrapper storedContactedFixture = group.get(1);
				contactListener.processFixture(storedEntityFixture.getFixture(), storedContactedFixture.getFixture(), false);
				contactListener.processFixture(storedContactedFixture.getFixture(), storedEntityFixture.getFixture(), false);
			}
		}
		
	}
	
	public float getX(){
		return currentBody.getPosition().x;
	}
	
	public float getY() {
		return currentBody.getPosition().y;
	}
	
}
