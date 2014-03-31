package com.sweatyreptile.losergame;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;

public class LoadingScreen implements Screen {

	// Is this an acceptable practice? I just want to be able to set the next screen
	private LoserGame game;
	private AssetManager loader;
	private Screen nextScreen;
	
	public LoadingScreen(LoserGame game, AssetManager loader, Screen nextScreen) {
		this.loader = loader;
		this.game = game;
		this.nextScreen = nextScreen;
	}

	@Override
	public void render(float delta) {
		if (loader.update()){
			game.setScreen(nextScreen);
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
