package com.sweatyreptile.losergame.tween;

import aurelienribon.tweenengine.TweenAccessor;

import com.badlogic.gdx.math.Vector3;
import com.sweatyreptile.losergame.screens.LevelScreen;

public class LevelAccessor implements TweenAccessor<LevelScreen> {

	public static final int CAMERA_POSITION = 0;
	
	public LevelAccessor() {
		
	}

	@Override
	public int getValues(LevelScreen level, int tweenType, float[] values) {
		switch(tweenType){
		case CAMERA_POSITION:
			Vector3 cameraPosition = level.getCameraPosition(); 
			values[0] = cameraPosition.x;
			values[1] = cameraPosition.y;
			break;
		}
		return 2;
	}

	@Override
	public void setValues(LevelScreen level, int tweenType, float[] values) {
		switch(tweenType){
		case CAMERA_POSITION:
			level.setCameraPosition(values[0], values[1]);
		}
	}

}
