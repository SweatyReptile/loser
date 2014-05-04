package com.sweatyreptile.losergame.entities;

import java.util.Stack;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.sweatyreptile.losergame.Entity;
import com.sweatyreptile.losergame.LoserContactListener;
import com.sweatyreptile.losergame.fixtures.EntityFixtureDef;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;
import com.sweatyreptile.losergame.sensors.ContentSensor;
import com.sweatyreptile.losergame.sensors.ContentSensorListener;

public class Sharbal extends Entity<Sharbal> {
	
	private Player player;
	private ContentSensor sensor;
	
	private String[] phrases;
	private static final String[] DEFAULT_PHRASES = new String[]{"go away", "i hate u", "who are you even"};;
	
	public Sharbal(World world, LoserContactListener contactListener,
			BodyDef bodyDef, AssetManagerPlus assets, EntityFixtureDef fixtureDef, boolean flipped,
			float screenWidth, float viewportWidth, Player player, String[] phrases) {
		super(world, contactListener, bodyDef, fixtureDef, flipped, screenWidth,
				viewportWidth, fixtureDef.getName());
		
		this.player = player;
		this.phrases = phrases;
		
		SharbalContentSensorListener listener = new SharbalContentSensorListener();
		sensor = new ContentSensor(contactListener, listener, world, assets, "default_sensor", .5f, 0, 0);
		sensor.setCenterRoundSensor(sprite);
		sensor.weld(world, currentBody); //TODO remember to reweld the sensor if Sharbal switches bodies
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
	
	private String generateSpeech(){
		int random = (int) (Math.random()*phrases.length);
		System.out.println(random);
		return phrases[random];
	}
	
	private class SharbalContentSensorListener implements ContentSensorListener { //TODO: find a better way to decide whether to talk

		private boolean playerEntered;
		
		@Override
		public void bodyAdded(Stack<Body> contents) {
			Body lastBody = contents.peek();
			if (!playerEntered && isPlayer(lastBody, player)){
				talk(generateSpeech());
				playerEntered = true;
			}
		}

		@Override
		public void bodyRemoved(Stack<Body> contents) {
			if (playerEntered){
				playerEntered = false;
				for (Body body : contents){
					if (isPlayer(body, player) && body.getUserData().equals(player)){
						playerEntered = true;
					}
				}
			}
		}
		
		private boolean isPlayer(Body body, Player player){
			if (body != null && body.getUserData() != null && body.getUserData().equals(player)) return true;
			return false;
		}
		
	}
	
	

}
