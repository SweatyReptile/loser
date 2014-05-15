package com.sweatyreptile.losergame.entities;

import java.util.Stack;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.sweatyreptile.losergame.Entity;
import com.sweatyreptile.losergame.LoserContactListener;
import com.sweatyreptile.losergame.fixtures.EntityFixtureDef;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;
import com.sweatyreptile.losergame.sensors.ContentSensor;
import com.sweatyreptile.losergame.sensors.ContentSensorListener;

public class MusicPlayer extends Entity<MusicPlayer> {

	private ContentSensor quackSensor;
	private Music music;
	private static final float MAX_DISTANCE = 2;
	
	private Player player;
	private AssetManagerPlus assets;
	private LoserContactListener contactListener;
	
	public MusicPlayer(LoserContactListener contactListener, World world, BodyDef bodyDef, AssetManagerPlus assets, 
			EntityFixtureDef fixtureDef, boolean flipped, float screenWidth,
			float viewportWidth, String musicName, boolean autoPlay, Player player) {
		super(world, contactListener, bodyDef, fixtureDef, flipped, screenWidth, viewportWidth, fixtureDef.getName());
		
		this.player = player;
		this.assets = assets;
		
		music = assets.get(musicName);
		music.setLooping(true);
		if (autoPlay) music.play();
		
		ContentSensorListener quackSensorListener = new MusicPlayerContentSensorListener();
		quackSensor = new ContentSensor(contactListener, quackSensorListener, world, assets, "default_sensor", .5f, 0, 0);
		quackSensor.setCenterRoundSensor(sprite);
		quackSensor.weld(world, currentBody);
		
	}
	
	public void setRange(String name, float scale, Object userData, int index1, int index2, boolean setCenter){
		quackSensor.destroy(world);
		quackSensor = new ContentSensor(contactListener, new MusicPlayerContentSensorListener(),
				world, assets, name, scale, index1, index2);
		if (setCenter) quackSensor.setCenterRoundSensor(sprite);
		quackSensor.weld(world, currentBody);
	}
	
	private void toggleMusic(){
		if (music.isPlaying()) music.pause();
		else music.play();
		talk("*beep*");
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
	
	private class MusicPlayerContentSensorListener implements ContentSensorListener{
		
		@Override
		public void bodyAdded(Stack<Body> contents) {
			Body lastBody = contents.peek();
			if (isPlayer(lastBody)){	
				Player player = (Player) lastBody.getUserData();
				if (player.isQuacking()){
					toggleMusic();
				}
			}
		}

		@Override
		public void bodyRemoved(Stack<Body> contents) {
			
		}
		
		private boolean isPlayer(Body body){
			if (body != null && body.getUserData() != null && 
					((Entity<?>)(body.getUserData()))
						.getName().equals("duck")) return true;
			return false;
		}
	}

}
