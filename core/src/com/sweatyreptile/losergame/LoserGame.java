package com.sweatyreptile.losergame;

import aurelienribon.bodyeditor.FixedBodyEditorLoader;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;
import com.sweatyreptile.losergame.screens.FinishableScreen;
import com.sweatyreptile.losergame.screens.LevelTitleScreen;
import com.sweatyreptile.losergame.screens.LoadingScreen;
import com.sweatyreptile.losergame.screens.ScreenFinishedListener;
import com.sweatyreptile.losergame.screens.TestScrollingLevelScreen;
import com.sweatyreptile.losergame.screens.TestLevelScreen;

public class LoserGame extends Game implements ScreenFinishedListener{
	SpriteBatch batch;
	AssetManagerPlus assets;
	
	@Override
	public void create () {
		assets = new AssetManagerPlus();
		batch = new SpriteBatch();
		int screenWidth = Gdx.graphics.getWidth();
		int screenHeight = Gdx.graphics.getHeight();

		assets.load("badlogic.jpg", Texture.class);
		assets.load("duck.json", FixedBodyEditorLoader.class);
		assets.load("background.png", Texture.class);
		assets.load("quack_dummy.ogg", Sound.class);
		assets.load("baby_come_back.ogg", Music.class);
		
		PlayerInputProcessor playerInputProcessor = new PlayerInputProcessor();
		
		Screen testScreen = new TestScrollingLevelScreen(this, null, batch, assets, playerInputProcessor,
				screenWidth, screenHeight, 3.2f, 1.8f, 30);
		
		Screen testTitleScreen = new LevelTitleScreen(batch, assets, screenWidth, screenHeight, 
				this, testScreen, "WELCOME TO THE JUNGLE");
		
		Screen loadingScreen = new LoadingScreen(this, assets, testTitleScreen);
		
		Gdx.input.setInputProcessor(playerInputProcessor);
		
		setScreen(loadingScreen);
	}

	@Override
	public void onFinish(FinishableScreen finished, Screen next) {
		setScreen(next);
		Gdx.app.log("A", next.getClass().toString());
	}
	
}
