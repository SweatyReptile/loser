package com.sweatyreptile.losergame.entities;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.sweatyreptile.losergame.Entity;
import com.sweatyreptile.losergame.Sensor;
import com.sweatyreptile.losergame.fixtures.EntityFixtureDef;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;

public class MusicPlayer extends Entity {

	private BodyDef bodyDef;
	private AssetManagerPlus assets;
	
	private Sensor accessRange;
	private Music music;
	private static final float MAX_DISTANCE = 3;
	
	public MusicPlayer(World world, BodyDef bodyDef, AssetManagerPlus assets, 
			EntityFixtureDef fixtureDef, boolean flipped, float screenWidth,
			float viewportWidth, String musicName, boolean autoPlay) {
		super(world, bodyDef, fixtureDef, flipped, screenWidth, viewportWidth);
		
		this.bodyDef = bodyDef;
		this.assets = assets;
		
		music = assets.get(musicName);
		music.setLooping(true);
		if (autoPlay) music.play();
		
		//DEFAULT (TODO)
		accessRange = new Sensor(world, bodyDef, assets, "default_sensor", .5f, "music_player", 0, 0);
		accessRange.setCenterRoundSensor(sprite);
		accessRange.weld(world, currentBody);
		//END DEFAULT
		
	}
	
	public void setRange(String name, float scale, Object userData, int index1, int index2){
		accessRange = new Sensor(world, bodyDef, assets, name, scale, userData, index1, index2);
		accessRange.setCenterRoundSensor(sprite);
		accessRange.weld(world, currentBody);
	}
	
	private void toggleMusic(){
		if (music.isPlaying()) music.pause();
		else music.play();
	}
	
	public void decideVolume(Body playerBody){
		Vector2 playerPos = playerBody.getPosition();
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
