package com.sweatyreptile.losergame.entities;

import java.util.Stack;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.sweatyreptile.losergame.Entity;
import com.sweatyreptile.losergame.LoserContactListener;
import com.sweatyreptile.losergame.fixtures.EntityFixtureDef;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;
import com.sweatyreptile.losergame.sensors.ContentSensor;
import com.sweatyreptile.losergame.sensors.ContentSensorListener;

public class Sharbal extends Entity<Sharbal> {
	
	Player player;
	ContentSensor sensor;
	
	public Sharbal(World world, LoserContactListener contactListener,
			BodyDef bodyDef, AssetManagerPlus assets, EntityFixtureDef fixtureDef, boolean flipped,
			float screenWidth, float viewportWidth, Player player) {
		super(world, contactListener, bodyDef, fixtureDef, flipped, screenWidth,
				viewportWidth, fixtureDef.getName());

		this.player = player;
		
		SharbalContentSensorListener listener = new SharbalContentSensorListener();
		sensor = new ContentSensor(contactListener, listener, world, assets, "default_sensor", .5f, 0, 0);
		sensor.setCenterRoundSensor(sprite);
		sensor.weld(world, currentBody); //TODO remember to reweld the sensor if Sharbal switches bodies
	}
	
	@Override
	public void update(float delta){
		super.update(delta);
		sensor.update(delta);
	}
	
	private class SharbalContentSensorListener implements ContentSensorListener { //TODO: find a better way to decide whether to talk

		private boolean playerEntered;
		
		@Override
		public void bodyAdded(Stack<Body> contents) {
			Body lastBody = contents.peek();
			if (!playerEntered && isPlayer(lastBody, player)){
				talk("go away");
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
