package com.sweatyreptile.losergame.tween;

import aurelienribon.tweenengine.TweenAccessor;

import com.badlogic.gdx.math.Vector3;
import com.sweatyreptile.losergame.screens.LevelScreen;

public class LevelScreenAccessor implements TweenAccessor<LevelScreen> {

	public static final int CAMERA_POSITION = 0;
	public static final int BORDER_X = 1;
	public static final int BORDER_Y = 2;
	
	public LevelScreenAccessor() {
		
	}

	@Override
	public int getValues(LevelScreen level, int tweenType, float[] values) {
		switch(tweenType){
		case CAMERA_POSITION:
			Vector3 cameraPosition = level.getCameraPosition(); 
			values[0] = cameraPosition.x;
			values[1] = cameraPosition.y;
			return 2;
		case BORDER_X:
			values[0] = level.getCameraScroller().getBorderX();
			return 1;
		case BORDER_Y:
			values[0] = level.getCameraScroller().getBorderY();
			return 1;
		default: return 0;

		}
	}

	@Override
	public void setValues(LevelScreen level, int tweenType, float[] values) {
		switch(tweenType){
		case CAMERA_POSITION:
			level.setCameraPosition(values[0], values[1]);
			break;
		case BORDER_X:
			level.getCameraScroller().updateBordersX(values[0], level.getEntities());
			break;
		case BORDER_Y:
			level.getCameraScroller().updateBordersY(values[0], level.getEntities());
			break;
		}
	}

}
