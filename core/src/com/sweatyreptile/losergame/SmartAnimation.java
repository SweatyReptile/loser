package com.sweatyreptile.losergame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;

public class SmartAnimation extends Animation {

	private float stateTime;
	private TextureRegion currentFrame;
	private boolean looping;
	private Sprite sprite;
	
	public SmartAnimation(float frameDuration,
			Array<? extends TextureRegion> keyFrames) {
		super(frameDuration, keyFrames);
		stateTime = 0f;
		currentFrame = getKeyFrame(stateTime, looping);
		sprite = new Sprite(currentFrame);
		sprite.setSize(.2f, .2f); //TODO (test)
	}
	//Specific to TextureAtlas
	public SmartAnimation(float frameDuration, boolean looping, 
			String atlasPath, AssetManagerPlus assets) {
		this(frameDuration, ((TextureAtlas) assets.get("temp/flap.txt")).getRegions());
		this.looping = looping;
	}
	//Default looping true
	public SmartAnimation(float frameDuration, 
			String atlasPath, AssetManagerPlus assets) {
		this(frameDuration, true, atlasPath, assets);
	}
	
	public void update(){
		stateTime += Gdx.graphics.getDeltaTime();
		currentFrame = getKeyFrame(stateTime, looping);
	}
	
	public TextureRegion currentFrame(){
		return currentFrame;
	}
	
	public Sprite getSprite(){
		sprite.setRegion(currentFrame);
		return sprite;
	}
	
	

}
