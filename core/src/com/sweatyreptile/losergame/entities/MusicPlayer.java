package com.sweatyreptile.losergame.entities;

import java.util.Stack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.sweatyreptile.losergame.Entity;
import com.sweatyreptile.losergame.Player;
import com.sweatyreptile.losergame.fixtures.EntityFixtureDef;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;
import com.sweatyreptile.losergame.sensors.ContentSensor;
import com.sweatyreptile.losergame.sensors.ContentSensorListener;
import com.sweatyreptile.losergame.sensors.SensorContactListener;

public class MusicPlayer extends Entity {

	private BodyDef bodyDef;
	private AssetManagerPlus assets;
	
	private SensorContactListener contactListener;
	private ContentSensor quackSensor;
	private Music music;
	private static final float MAX_DISTANCE = 2;
	
	private Player player;
	
	public MusicPlayer(SensorContactListener contactListener, World world, BodyDef bodyDef, AssetManagerPlus assets, 
			EntityFixtureDef fixtureDef, boolean flipped, float screenWidth,
			float viewportWidth, String musicName, boolean autoPlay, Player player) {
		super(world, bodyDef, fixtureDef, flipped, screenWidth, viewportWidth);
		
		this.contactListener = contactListener;
		this.player = player;
		this.bodyDef = bodyDef;
		this.assets = assets;
		
		music = assets.get(musicName);
		music.setLooping(true);
		if (autoPlay) music.play();
		
		final Player player2 = player; 
		ContentSensorListener quackSensorListener = new ContentSensorListener() {

			@Override
			public void bodyAdded(Stack<Body> contents) {
				Gdx.app.log("Yo yo", "sup i added a body, yo " + contents.size());
				for (Body quackBody : player2.getQuackBodies()) {
					if (contents.peek().equals(quackBody)) {
						toggleMusic();
						break;
					}
				}
			}

			@Override
			public void bodyRemoved(Stack<Body> contents) {
				Gdx.app.log("Yo yo", "sup i removed a body, yo " + contents.size());
				
			}
			
		};
		quackSensor = new ContentSensor(contactListener, quackSensorListener, world, assets, "default_sensor", .5f, 0, 0);
		quackSensor.setCenterRoundSensor(sprite);
		quackSensor.weld(world, currentBody);
		
		//DEFAULT (TODO)
		//accessRange = new Sensor(world, bodyDef, assets, "default_sensor", .5f, "music_player", 0, 0);
		//accessRange.setCenterRoundSensor(sprite);
		//accessRange.weld(world, currentBody);
		//END DEFAULT
		
	}
	
	public void setRange(String name, float scale, Object userData, int index1, int index2){
		//accessRange = new Sensor(world, bodyDef, assets, name, scale, userData, index1, index2);
		//accessRange.setCenterRoundSensor(sprite);
		//accessRange.weld(world, currentBody);
	}
	
	private void toggleMusic(){
		if (music.isPlaying()) music.pause();
		else music.play();
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		quackSensor.update(delta);
		determineVolume();
	}
	
	public void determineVolume(){
		Vector2 playerPos = player.getBody().getPosition();
		Vector2 pos = currentBody.getPosition();
		double posX = (double) pos.x;
		double posY = (double) pos.y;
		double playPosX = (double) playerPos.x;
		double playPosY = (double) playerPos.y;
		
		double distance = Math.pow(Math.pow(posX - playPosX, 2) + Math.pow(posY - playPosY, 2), 0.5);
		float volume = 1 - (float) distance/MAX_DISTANCE;
		music.setVolume(volume);
	}
	
	

}
