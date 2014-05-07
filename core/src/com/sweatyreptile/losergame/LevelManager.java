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

	private ScreenFinishedListener screenFinishedListener;
	
	public LevelManager(AssetManagerPlus assets, SpriteBatch batch, 
			float screenwidth, float screenheight){
		this.batch = batch;
		this.assets = assets;
		
		levels = new HashMap<String, LevelScreen>();
		
		String testLevelName = "WELCOME TO THE JUNGLE";
	}
	
	public void update(float delta){
		
	}

}
