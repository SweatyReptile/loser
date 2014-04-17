package com.sweatyreptile.losergame.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;

public class LoadingScreen extends FinishableScreen{

	private AssetManager loader;
	
	public LoadingScreen(ScreenFinishedListener listener, AssetManager loader, Screen nextScreen) {
		super(listener, nextScreen);
		this.loader = loader;
	}

	@Override
	public void render(float delta) {
		if (loader.update()){
			finish();
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
