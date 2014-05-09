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
		
		LevelManager levelManager = new LevelManager(assets, batch, playerInputProcessor, this, screenWidth, screenHeight);
		Screen loadingScreen = new LoadingScreen(assets, levelManager);
		
		Gdx.input.setInputProcessor(playerInputProcessor);
		
		setScreen(loadingScreen);
	}

	@Override
	public void onFinish(FinishableScreen finished, Screen next) {
		setScreen(next);
		if (next == null){
			Gdx.app.error("LoserGame", "Can't switch to null screen!");
		}
	}
	
}
