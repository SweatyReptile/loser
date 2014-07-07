package com.sweatyreptile.losergame;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Json;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;
import com.sweatyreptile.losergame.screens.FinishableScreen;
import com.sweatyreptile.losergame.screens.LevelScreen;
import com.sweatyreptile.losergame.screens.LevelTitleScreen;
import com.sweatyreptile.losergame.screens.ScreenFinishedListener;

public class LevelManager {
	
	private static final float DVWIDTH = 3.2f;
	private static final float DVHEIGHT = 1.8f;
	private Map<String, LevelTitleScreen> levelTitles;
	private Map<String, LevelScreen> levels;
	private FinishableScreen currentScreen;
	private SpriteBatch batch;
	private AssetManagerPlus assets;
	private InputMultiplexer inputMultiplexer;
	private PlayerInputProcessor inputProcessor;
	private InputProcessor editInputProcessor;
	
	private int screenWidth;
	private int screenHeight;
	
	private String currentLevel;

	private ScreenFinishedListener screenFinishedListener;
	
	public LevelManager(AssetManagerPlus assets, SpriteBatch batch, 
			InputMultiplexer inputMultiplexer, PlayerInputProcessor inputProcessor, 
			ScreenFinishedListener screenFinishedListener,
			int screenWidth, int screenHeight) {
		this.batch = batch;
		this.assets = assets;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.screenFinishedListener = screenFinishedListener;
		this.inputMultiplexer = inputMultiplexer;
		this.inputProcessor = inputProcessor;
		
		levels = new HashMap<String, LevelScreen>();
		levelTitles = new HashMap<String, LevelTitleScreen>();
		
		instantiate("test_menu", "DummyMenu", "DUCK GAME", DVWIDTH, DVHEIGHT, -1);
		instantiate("test_home", "TestScrollingLevelScreen", "WELCOME TO THE JUNGLE", DVWIDTH, DVHEIGHT, 20f);
		instantiate("test_home_old", "TestLevelScreen", "WELCOME TO THE JUNGLE", DVWIDTH, DVHEIGHT, 20f);
	}
	
	public void instantiate(String alias, String typeName, String levelName,
			float viewportWidth, float viewportHeight, float timeLimit) {
		
		LoserLog.log("LevelManager", "Instantiate " + alias + " " + levelName + " (" + typeName + ")");
		
		LevelTitleScreen titleScreen = new LevelTitleScreen(
				this, batch, assets, screenWidth, screenHeight, alias, levelName);
		
		LevelScreen level = LevelScreen.newInstance(
				typeName, this, batch, assets,
				inputProcessor, screenWidth, screenHeight,
				viewportWidth, viewportHeight, timeLimit, alias, levelName);
		
		levels.put(alias, level);
		levelTitles.put(alias, titleScreen);

	}
	
	public void instantiate(LevelData level){
		String newAlias = level.getAlias() + "_g";
		instantiate(newAlias, "EmptyLevelScreen", level.getTitle(),
				level.getViewportWidth(), level.getViewportHeight(),
				level.getTimerLength());
		LevelScreen newLevel = levels.get(newAlias);
		for (EntityData edata : level.getEntities()){
			newLevel.addEntity(edata);
		}
		newLevel.setBackground(level.getBgname());
	}
	
	public void level_save(){
		Json json = new Json();
		LevelScreen level = levels.get(currentLevel);
		LevelData data = level.getLevelData();
		//LoserLog.log("LevelData", "\n" + json.prettyPrint(data));
		instantiate(data);
	}
	
	public void edit() {
		LevelScreen level = levels.get(currentLevel);
		level.setEditMode(true);
		editInputProcessor = new EditModeInputProcessor(level);
		inputMultiplexer.addProcessor(editInputProcessor);
		restart();
	}
	
	public void play(boolean restart) {
		LevelScreen level = levels.get(currentLevel);
		if (level != null){
			level.setEditMode(false);
		}
		inputMultiplexer.removeProcessor(editInputProcessor);
		if (restart){
			restart();
		}
	}
	
	public void play(){
		play(true);
	}
	
	public void restart() {
		level(currentLevel);
	}
	
	public void nextlevel() {
		// Wow, you've reached a stub!
		
		// To be called to switch to the level
		// that comes after the current level
		
		// We don't really have multiple levels yet,
		// and we don't have the ordering down,
		// so I don't really know how I'm going to set
		// up what determines what the next level actually is.
	}

	public void level(String alias) {
		
		if (!alias.equals(currentLevel)){
			// Turn off edit mode on current level without restarting it
			play(false);
		}
		
		LoserLog.log("LevelManager", "Switch to " + alias + " title screen");
		LevelTitleScreen ltScreen = levelTitles.get(alias);
		screenFinishedListener.onFinish(currentScreen, ltScreen);
		currentScreen = ltScreen;
		currentLevel = alias;
	}
	
	public void level_notitle(String alias) {
		LoserLog.log("LevelManager", "Switch to " + alias);
		LevelScreen lvlScreen = levels.get(alias);
		screenFinishedListener.onFinish(currentScreen, lvlScreen);
		currentScreen = lvlScreen;
	}
	
	public void exit(){
		Gdx.app.exit();
	}
	
}
