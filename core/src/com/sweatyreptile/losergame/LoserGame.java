package com.sweatyreptile.losergame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LoserGame extends Game implements ScreenFinishedListener{
	SpriteBatch batch;
	AssetManager assets;
	
	@Override
	public void create () {
		assets = new AssetManager();
		batch = new SpriteBatch();

		assets.load("badlogic.jpg", Texture.class);
		Screen testScreen = new TestLevelScreen(batch, assets);
		Screen loadingScreen = new LoadingScreen(this, assets, testScreen);
		
		setScreen(loadingScreen);
	}

	@Override
	public void onFinish(FinishableScreen finished, Screen next) {
		setScreen(next);
		Gdx.app.log("A", next.getClass().toString());
	}
	
}
