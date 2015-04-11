package com.sweatyreptile.losergame.entities;

import java.util.Stack;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.sweatyreptile.losergame.Entity;
import com.sweatyreptile.losergame.EntityListener;
import com.sweatyreptile.losergame.FixtureWrapper;
import com.sweatyreptile.losergame.LoserContactListener;
import com.sweatyreptile.losergame.fixtures.EntityFixtureDef;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;
import com.sweatyreptile.losergame.sensors.ContentSensor;
import com.sweatyreptile.losergame.sensors.ContentSensorListener;

public class Sharbal extends Entity<Sharbal> {
	
	private ContentSensor sensor;
	
	private static final String[] DEFAULT_PHRASES = new String[]{
		"go away", "i hate u", "who are you even", "leave me alone", "shoo", 
		"SWEATY REPTILES UNITE!", "i miss clib", "i kick ducks like you", "wow i m cute",
		"*clever remark*", "i want a donut", "*reptile noise*", "u think ur so cool but ur nothing but a fool"};
	private static final String[] OUCH_PHRASES = new String[]{"ow", "ouch", "owwwies!"};
	private Task respondTask;
	
	private static final float PAINFUL_VELOCITY = 3f;
	
	public Sharbal(World world, LoserContactListener contactListener,
			BodyDef bodyDef, AssetManagerPlus assets, EntityFixtureDef fixtureDef, boolean flipped,
			float screenWidth, float viewportWidth, String[] phrases) {
		super(world, contactListener, bodyDef, fixtureDef, flipped, screenWidth,
				viewportWidth, fixtureDef.getName());
		
		SharbalContentSensorListener listener = new SharbalContentSensorListener();
		sensor = new ContentSensor(contactListener, listener, world, assets, "default_sensor", "default_sensor_2", 1f, 0, 0);
		sensor.setCenterRoundSensor(sprite);
		sensor.weld(world, currentBody); //TODO remember to reweld the sensor if Sharbal switches bodies
		
		addListener(new SharbalContactListener());
		
		final String[] phrases2 = phrases;
		respondTask = new Task() {
			@Override
			public void run() {
				talk(phrases2);
			}
		};
		isSpecial = true;
	}
	
	public Sharbal(World world, LoserContactListener contactListener,
			BodyDef bodyDef, AssetManagerPlus assets, EntityFixtureDef fixtureDef, boolean flipped,
			float screenWidth, float viewportWidth, Player player) {
		this(world, contactListener, bodyDef, assets, fixtureDef, flipped,
			screenWidth, viewportWidth, DEFAULT_PHRASES);
	}
	
	@Override
	public void update(float delta){
		super.update(delta);
		sensor.update(delta);
	}
	
	private class SharbalContentSensorListener implements ContentSensorListener {

		//private boolean playerEntered;
		
		@Override
		public void bodyAdded(Stack<Body> contents) {
			Body lastBody = contents.peek();
			
			if (isPlayer(lastBody)){
				Player player = (Player) lastBody.getUserData();
				if (player.isQuacking()){
					if (!respondTask.isScheduled()) Timer.schedule(respondTask, 1f);
				}
				/*if (!playerEntered){
					talk(generatePhrase(phrases));
					playerEntered = true;
				}*/
			}
		}

		@Override
		public void bodyRemoved(Stack<Body> contents) {
			/*if (playerEntered){
				playerEntered = false;
				for (Body body : contents){
					if (isPlayer(body, player) && body.getUserData().equals(player)){
						playerEntered = true;
					}
				}
			}*/
		}
		
		private boolean isPlayer(Body body){
			if (body != null && body.getUserData() != null && 
					((Entity<?>)(body.getUserData()))
						.getName().equals("duck")) return true;
			return false;
		}
		
	}
	
	private class SharbalContactListener implements EntityListener<Sharbal>{

		Body fastBody;
		
		@Override
		public void beginContact(Sharbal entity, FixtureWrapper entityFixture,
				FixtureWrapper contactee) {
			Vector2 velocityVector = contactee.getBody().getLinearVelocity();
			float velocity = (float) Math.pow((float) Math.pow(velocityVector.x, 2) + (float) Math.pow(velocityVector.y, 2), 0.5);
			if (velocity > PAINFUL_VELOCITY && fastBody == null){
				fastBody = contactee.getBody();
				talk(OUCH_PHRASES);
			}
			
		}

		@Override
		public void endContact(Sharbal entity, FixtureWrapper entityFixture,
				FixtureWrapper contactee) {
			if (contactee.getBody() == fastBody) fastBody = null;
			
		}
		
	}
	
	

}
