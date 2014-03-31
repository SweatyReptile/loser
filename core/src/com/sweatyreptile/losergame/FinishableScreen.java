package com.sweatyreptile.losergame;

import com.badlogic.gdx.Screen;

public abstract class FinishableScreen implements Screen{

	private ScreenFinishedListener finishListener;
	private Screen nextScreen;
	
	public FinishableScreen(ScreenFinishedListener finishListener, Screen nextScreen) {
		this.finishListener = finishListener;
		this.nextScreen = nextScreen;
	}
	
	protected void finish(){
		finishListener.onFinish(this, nextScreen);
	}
	
	
}
