package com.sweatyreptile.losergame;

import aurelienribon.bodyeditor.FixedBodyEditorLoader;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;
import com.sweatyreptile.losergame.screens.FinishableScreen;
import com.sweatyreptile.losergame.screens.LoadingScreen;
import com.sweatyreptile.losergame.screens.ScreenFinishedListener;
import com.sweatyreptile.losergame.screens.TestLevelScreen;

public class LoserGame extends Game implements ScreenFinishedListener{
	SpriteBatch batch;
	AssetManagerPlus assets;
	
	@Override
	public void create () {
		assets = new AssetManagerPlus();
		batch = new SpriteBatch();

		assets.load("badlogic.jpg", Texture.class);
		assets.load("duck.json", FixedBodyEditorLoader.class);
		assets.load("background.png", Texture.class);
		assets.load("duck.png", Texture.class);
		assets.load("book_blue.png", Texture.class);
		assets.load("book_red.png", Texture.class);
		assets.load("book_yellow.png", Texture.class);
		assets.load("cereal.png", Texture.class);
		assets.load("duck_quack_top.png", Texture.class);
		assets.load("duck_quack.png", Texture.class);
		assets.load("duck_top.png", Texture.class);
		assets.load("dummy_duck_sprite.png", Texture.class);
		assets.load("shelf.png", Texture.class);
		assets.load("table.png", Texture.class);
		assets.load("wash_machine.png", Texture.class);
		
		PlayerInputProcessor playerInputProcessor = new PlayerInputProcessor();
		
		Screen testScreen = new TestLevelScreen(batch, assets, playerInputProcessor,
				Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 3.2f, 1.8f);
		
		Screen loadingScreen = new LoadingScreen(this, assets, testScreen);
		
		Gdx.input.setInputProcessor(playerInputProcessor);
		
		setScreen(loadingScreen);
	}

	@Override
	public void onFinish(FinishableScreen finished, Screen next) {
		setScreen(next);
		Gdx.app.log("A", next.getClass().toString());
	}
	
}
