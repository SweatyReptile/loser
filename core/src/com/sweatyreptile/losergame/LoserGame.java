package com.sweatyreptile.losergame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LoserGame extends Game {
	SpriteBatch batch;
	AssetManager assets;
	
	@Override
	public void create () {
		assets = new AssetManager();
		batch = new SpriteBatch();

		Texture logic = new Texture("badlogic.jpg");
		assets.load("badlogic.jpg", Texture.class);
		Screen testScreen = new TestLevelScreen(batch, assets);
		Screen loadingScreen = new LoadingScreen(this, assets, testScreen);
		
		setScreen(loadingScreen);
	}
	
}
