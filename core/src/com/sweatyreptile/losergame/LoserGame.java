package com.sweatyreptile.losergame;

import aurelienribon.bodyeditor.FixedBodyEditorLoader;
import aurelienribon.tweenengine.Tween;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;
import com.sweatyreptile.losergame.loaders.BitmapFontGroup;
import com.sweatyreptile.losergame.loaders.FreeTypeFontLoader;
import com.sweatyreptile.losergame.loaders.FontGroupParameters;
import com.sweatyreptile.losergame.screens.FinishableScreen;
import com.sweatyreptile.losergame.screens.LevelScreen;
import com.sweatyreptile.losergame.screens.LoadingScreen;
import com.sweatyreptile.losergame.screens.ScreenFinishedListener;
import com.sweatyreptile.losergame.tween.ScrollingLevelAccessor;

public class LoserGame extends Game implements ScreenFinishedListener{
	SpriteBatch batch;
	AssetManagerPlus assets;
	
	@Override
	public void create () {	
		
		Tween.registerAccessor(LevelScreen.class, new ScrollingLevelAccessor());
		
		assets = new AssetManagerPlus();
		assets.setLoader(BitmapFontGroup.class, new FreeTypeFontLoader(new InternalFileHandleResolver()));
		
		batch = new SpriteBatch();
		int screenWidth = Gdx.graphics.getWidth();
		int screenHeight = Gdx.graphics.getHeight();

		FontGroupParameters corbelParams = makeCorbelParams();
		
		assets.load("corbelb.ttf", BitmapFontGroup.class, corbelParams);
		
		assets.load("badlogic.jpg", Texture.class);
		assets.load("duck.json", FixedBodyEditorLoader.class);
		
		assets.load("background.png", Texture.class);
		assets.load("menu_dummy_1.png", Texture.class);
		assets.load("menu_dummy_2.png", Texture.class);

		assets.load("quack_dummy.ogg", Sound.class);
		assets.load("baby_come_back.ogg", Music.class);
		
		PlayerInputProcessor playerInputProcessor = new PlayerInputProcessor();
		
		LevelManager levelManager = new LevelManager(assets, batch, playerInputProcessor, this, screenWidth, screenHeight);
		Screen loadingScreen = new LoadingScreen(assets, levelManager);
		
		Gdx.input.setInputProcessor(playerInputProcessor);
		
		setScreen(loadingScreen);
	}

	private FontGroupParameters makeCorbelParams() {
		FontGroupParameters corbelFontParams = new FontGroupParameters();
		FreeTypeFontParameter speechTypeParams = new FreeTypeFontParameter();
		speechTypeParams.size = 18;
		FreeTypeFontParameter titleTypeParams = new FreeTypeFontParameter();
		titleTypeParams.size = 72;
		corbelFontParams.fontTypes.put("speech", speechTypeParams);
		corbelFontParams.fontTypes.put("title", titleTypeParams);
		return corbelFontParams;
	}

	@Override
	public void onFinish(FinishableScreen finished, Screen next) {
		setScreen(next);
		if (next == null){
			Gdx.app.error("LoserGame", "Can't switch to null screen!");
		}
	}
	
}
