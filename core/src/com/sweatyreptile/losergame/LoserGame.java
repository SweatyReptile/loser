package com.sweatyreptile.losergame;

import aurelienribon.bodyeditor.FixedBodyEditorLoader;
import aurelienribon.tweenengine.Tween;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;
import com.sweatyreptile.losergame.loaders.BitmapFontGroup;
import com.sweatyreptile.losergame.loaders.FontGroupParameters;
import com.sweatyreptile.losergame.loaders.FreeTypeFontLoader;
import com.sweatyreptile.losergame.screens.FinishableScreen;
import com.sweatyreptile.losergame.screens.LevelScreen;
import com.sweatyreptile.losergame.screens.LoadingScreen;
import com.sweatyreptile.losergame.screens.LoadingScreen.LoadingFinishedListener;
import com.sweatyreptile.losergame.screens.ScreenFinishedListener;
import com.sweatyreptile.losergame.tween.LevelScreenAccessor;

public class LoserGame extends Game implements ScreenFinishedListener{
	SpriteBatch batch;
	AssetManagerPlus assets;
	Console console;
	
	InputMultiplexer inputMultiplexer;
	PlayerInputProcessor playerInputProcessor;
	
	@Override
	public void create () {	
		
		Tween.registerAccessor(LevelScreen.class, new LevelScreenAccessor());
		
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

		assets.load("img/ui/console_bg.png", Texture.class, filtering);
		assets.load("img/ui/console_textfield.png", Texture.class, filtering);
		
		loadBackgrounds(filtering);

		assets.load("sfx/quack_dummy.ogg", Sound.class);
		assets.load("music/baby_come_back.ogg", Music.class);
		
		inputMultiplexer = new InputMultiplexer();
		
		playerInputProcessor = new PlayerInputProcessor();
		
		console = new Console(batch, assets, inputMultiplexer, Gdx.graphics.getWidth(), 200);
		LoserLog.console = console;
		
		final LevelManager levelManager = new LevelManager(assets, batch, inputMultiplexer, playerInputProcessor, this, screenWidth, screenHeight);
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
				levelManager.lvl_reload_all();
				levelManager.lvl_new("test_menu", "DummyMenu", "DUCK GAME");
			}
		});
		
		setScreen(loadingScreen);
		
	}

	private void loadBackgrounds(TextureParameter filtering) {
		boolean hack = true;
		if(hack && Gdx.app.getType() == ApplicationType.Desktop){
			// Hack, because listing classpath directories
			// isn't supported, and that's how the files are
			// packaged on desktop.
			// To update this for new files, turn off the hack
			// and watch the log for entries like this:
			//
			// BGLoad: img/bg/background.png
			// BGLoad: img/bg/background_extended.png
			// BGLoad: img/bg/menu_dummy_0.png
			// BGLoad: img/bg/menu_dummy_1.png
			// BGLoad: img/bg/menu_dummy_2.png
			// BGLoad: img/bg/void.png
			// Just add them to the list... :(
			assets.load("img/bg/background.png", Texture.class, filtering);
			assets.load("img/bg/background_extended.png", Texture.class, filtering);
			assets.load("img/bg/menu_dummy_0.png", Texture.class, filtering);
			assets.load("img/bg/menu_dummy_1.png", Texture.class, filtering);
			assets.load("img/bg/menu_dummy_2.png", Texture.class, filtering);
			assets.load("img/bg/void.png", Texture.class, filtering);
		}
		else{
			FileHandle[] bgDir = Gdx.files.internal("img/bg/").list();
			for (FileHandle file : bgDir){
				Gdx.app.log("BGLoad", file.path());
				assets.load(file.path(), Texture.class, filtering);
			}
		}
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
