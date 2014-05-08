package com.sweatyreptile.losergame;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;
import com.sweatyreptile.losergame.screens.LevelScreen;
import com.sweatyreptile.losergame.screens.ScreenFinishedListener;
import com.sweatyreptile.losergame.screens.TestScrollingLevelScreen;

public class LevelManager {

	private Map<String, LevelScreen> levels;
	private LevelScreen currentLevel;
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
		
		instantiateScreen("testHome", "TestScrollingLevelScreen", "WELCOME TO THE JUNGLE", 3.2f, 1.8f, 30f);
	}
	
	public void instantiateScreen(String alias, String typeName, String levelName,
			float viewportWidth, float viewportHeight, float timeLimit){
		
		LevelScreen level = LevelScreen.newInstance(
				typeName, this, batch, assets,
				inputProcessor, screenWidth, screenHeight,
				viewportWidth, viewportHeight, timeLimit, levelName);
		
		levels.put(alias, level);
		
	}
	
	public void update(float delta){
		
	}

}
