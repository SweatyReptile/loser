package com.sweatyreptile.losergame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
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
		
		Screen testScreen = new TestLevelScreen(batch, assets, 
				Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 3.2f, 1.8f);
		
		Screen loadingScreen = new LoadingScreen(this, assets, testScreen);
		
		setScreen(loadingScreen);
	}

	@Override
	public void onFinish(FinishableScreen finished, Screen next) {
		setScreen(next);
		Gdx.app.log("A", next.getClass().toString());
	}
	
}
