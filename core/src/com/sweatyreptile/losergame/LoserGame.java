package com.sweatyreptile.losergame;

import aurelienribon.bodyeditor.FixedBodyEditorLoader;
import aurelienribon.tweenengine.Tween;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Logger;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;
import com.sweatyreptile.losergame.loaders.BitmapFontGroup;
import com.sweatyreptile.losergame.loaders.FontGroupParameters;
import com.sweatyreptile.losergame.loaders.FreeTypeFontLoader;
import com.sweatyreptile.losergame.screens.FinishableScreen;
import com.sweatyreptile.losergame.screens.LevelScreen;
import com.sweatyreptile.losergame.screens.LoadingScreen;
import com.sweatyreptile.losergame.screens.LoadingScreen.LoadingFinishedListener;
import com.sweatyreptile.losergame.screens.ScreenFinishedListener;
import com.sweatyreptile.losergame.tween.ScrollingLevelAccessor;

public class LoserGame extends Game implements ScreenFinishedListener{
	SpriteBatch batch;
	AssetManagerPlus assets;
	Console console;
	
	InputMultiplexer inputMultiplexer;
	PlayerInputProcessor playerInputProcessor;
	
	@Override
	public void create () {	
		
		Tween.registerAccessor(LevelScreen.class, new ScrollingLevelAccessor());
		
		assets = new AssetManagerPlus();
		assets.setLoader(BitmapFontGroup.class, new FreeTypeFontLoader(new InternalFileHandleResolver()));
		
		batch = new SpriteBatch();
		int screenWidth = Gdx.graphics.getWidth();
		int screenHeight = Gdx.graphics.getHeight();

		FontGroupParameters corbelParams = makeCorbelParams();
		
		TextureParameter filtering = new TextureParameter();
		filtering.minFilter = TextureFilter.Linear;
		filtering.magFilter = TextureFilter.Linear;
		
		assets.load("fonts/corbelb.ttf", BitmapFontGroup.class, corbelParams);
		
		assets.load("duck.json", FixedBodyEditorLoader.class);
		
		assets.load("img/ui/skins/gdxtest/uiskin.atlas", TextureAtlas.class);
		assets.load("img/ui/skins/gdxtest/uiskin.json", Skin.class);
		
		assets.load("img/bg/background_extended.png", Texture.class, filtering);
		assets.load("img/bg/menu_dummy_0.png", Texture.class, filtering);
		assets.load("img/bg/menu_dummy_1.png", Texture.class, filtering);
		assets.load("img/bg/menu_dummy_2.png", Texture.class, filtering);
		assets.load("img/ui/console_bg.png", Texture.class, filtering);
		assets.load("img/ui/console_textfield.png", Texture.class, filtering);

		assets.load("sfx/quack_dummy.ogg", Sound.class);
		assets.load("music/baby_come_back.ogg", Music.class);
		
		inputMultiplexer = new InputMultiplexer();
		
		playerInputProcessor = new PlayerInputProcessor();
		
		console = new Console(batch, assets, inputMultiplexer, Gdx.graphics.getWidth(), 200);
		LoserLog.console = console;
		
		LevelManager levelManager = new LevelManager(assets, batch, inputMultiplexer, playerInputProcessor, this, screenWidth, screenHeight);
		LoadingScreen loadingScreen = new LoadingScreen(assets, levelManager);
		
		console.setLevelManager(levelManager);
		
		GlobalInputProcessor globalInputProcessor = new GlobalInputProcessor(console);
		
		inputMultiplexer.addProcessor(0, globalInputProcessor);
		inputMultiplexer.addProcessor(1, playerInputProcessor);
		
		Gdx.input.setInputProcessor(inputMultiplexer);
		
		loadingScreen.addListener(new LoadingFinishedListener() {
			
			@Override
			public void run() {
				console.init();
			}
		});
		
		setScreen(loadingScreen);
		
	}
	
	

	@Override
	public void render() {
		super.render();
		console.render();
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
			LoserLog.error("LoserGame", "Can't switch to null screen!");
		}
	}
	
}
