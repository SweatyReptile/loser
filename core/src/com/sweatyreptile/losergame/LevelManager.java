package com.sweatyreptile.losergame;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;
import com.sweatyreptile.losergame.screens.FinishableScreen;
import com.sweatyreptile.losergame.screens.LevelScreen;
import com.sweatyreptile.losergame.screens.LevelTitleScreen;
import com.sweatyreptile.losergame.screens.ScreenFinishedListener;

public class LevelManager {

	private Map<String, LevelTitleScreen> levelTitles;
	private Map<String, LevelScreen> levels;
	private FinishableScreen currentScreen;
	private SpriteBatch batch;
	private AssetManagerPlus assets;
	private PlayerInputProcessor inputProcessor;
	
	private int screenWidth;
	private int screenHeight;

	private ScreenFinishedListener screenFinishedListener;
	
	public LevelManager(AssetManagerPlus assets, SpriteBatch batch, 
			PlayerInputProcessor inputProcessor, 
			int screenWidth, int screenHeight){
		this.batch = batch;
		this.assets = assets;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		
		levels = new HashMap<String, LevelScreen>();
		levelTitles = new HashMap<String, LevelTitleScreen>();
		
		instantiate("test_home", "TestScrollingLevelScreen", "WELCOME TO THE JUNGLE", 3.2f, 1.8f, 30f);
	}
	
	public void instantiate(String alias, String typeName, String levelName,
			float viewportWidth, float viewportHeight, float timeLimit){
		
		LevelTitleScreen titleScreen = new LevelTitleScreen(
				this, batch, assets, screenWidth, screenHeight, alias, levelName);
		
		LevelScreen level = LevelScreen.newInstance(
				typeName, this, batch, assets,
				inputProcessor, screenWidth, screenHeight,
				viewportWidth, viewportHeight, timeLimit, alias, levelName);
		
		levels.put(alias, level);
		levelTitles.put(alias, titleScreen);
		
		Gdx.app.log("LevelManager", "Instantiated " + alias);
	}
	
	public void level(String alias){
		LevelTitleScreen ltScreen = levelTitles.get(alias);
		screenFinishedListener.onFinish(currentScreen, ltScreen);
		currentScreen = ltScreen;
	}
	
	public void level_notitle(String alias){
		LevelScreen lvlScreen = levels.get(alias);
		screenFinishedListener.onFinish(currentScreen, lvlScreen);
		currentScreen = lvlScreen;
	}
	
}
