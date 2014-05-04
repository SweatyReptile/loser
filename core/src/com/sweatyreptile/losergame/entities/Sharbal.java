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
import com.sweatyreptile.losergame.LoserContactListener;
import com.sweatyreptile.losergame.fixtures.EntityFixtureDef;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;
import com.sweatyreptile.losergame.sensors.ContentSensor;
import com.sweatyreptile.losergame.sensors.ContentSensorListener;

public class Sharbal extends Entity<Sharbal> {
	
	private Player player;
	private ContentSensor sensor;
	
	private String[] phrases;
	private static final String[] DEFAULT_PHRASES = new String[]{"go away", "i hate u", "who are you even"};
	private Task respondTask;
	
	private static final float PAINFUL_VELOCITY = 3f;
	
	public Sharbal(World world, LoserContactListener contactListener,
			BodyDef bodyDef, AssetManagerPlus assets, EntityFixtureDef fixtureDef, boolean flipped,
			float screenWidth, float viewportWidth, Player player, String[] phrases) {
		super(world, contactListener, bodyDef, fixtureDef, flipped, screenWidth,
				viewportWidth, fixtureDef.getName());
		
		this.player = player;
		this.phrases = phrases;
		
		SharbalContentSensorListener listener = new SharbalContentSensorListener();
		sensor = new ContentSensor(contactListener, listener, world, assets, "default_sensor", 1f, 0, 0);
		sensor.setCenterRoundSensor(sprite);
		sensor.weld(world, currentBody); //TODO remember to reweld the sensor if Sharbal switches bodies
		
		addListener(new SharbalContactListener());
		
		final String[] phrases2 = phrases;
		respondTask = new Task() {
			@Override
			public void run() {
				talk(generatePhrase(phrases2));
			}
		};
	}
	
	public Sharbal(World world, LoserContactListener contactListener,
			BodyDef bodyDef, AssetManagerPlus assets, EntityFixtureDef fixtureDef, boolean flipped,
			float screenWidth, float viewportWidth, Player player) {
		this(world, contactListener, bodyDef, assets, fixtureDef, flipped,
			screenWidth, viewportWidth, player, DEFAULT_PHRASES);
	}
	
	@Override
	public void update(float delta){
		super.update(delta);
		sensor.update(delta);
	}
	
	private String generatePhrase(String[] phrases){
		int random = (int) (Math.random()*phrases.length);
		System.out.println(random);
		return phrases[random];
	}
	
	private class SharbalContentSensorListener implements ContentSensorListener {

		private boolean playerEntered;
		
		@Override
		public void bodyAdded(Stack<Body> contents) {
			Body lastBody = contents.peek();
			
			if (isPlayer(lastBody, player)){
				if (player.quacking){
					if (respondTask.isScheduled()) respondTask.cancel();
					Timer.schedule(respondTask, 1f);
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
		
		private boolean isPlayer(Body body, Player player){
			if (body != null && body.getUserData() != null && body.getUserData().equals(player)) return true;
			return false;
		}
		
	}
	
	private class SharbalContactListener implements EntityListener<Sharbal>{

		@Override
		public void beginContact(Sharbal entity, Fixture entityFixture,
				Fixture contactee) {
			Vector2 velocityVector = contactee.getBody().getLinearVelocity();
			float velocity = (float) Math.pow((float) Math.pow(velocityVector.x, 2) + (float) Math.pow(velocityVector.y, 2), 0.5);
			System.out.println(velocity);
			if (velocity > PAINFUL_VELOCITY){
				talk(generatePhrase(new String[]{"ow", "ouch", "owwwies!"}));
			}
			
		}

		@Override
		public void endContact(Sharbal entity, Fixture entityFixture,
				Fixture contactee) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	

}
