package com.sweatyreptile.losergame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.sweatyreptile.losergame.LevelManager;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;

public class LevelTitleScreen implements FinishableScreen {

	private static final float SEC_PER_CHAR = 0.05f;
	private int screenWidth;
	private int screenHeight;
	private SpriteBatch batch;
	private AssetManagerPlus assets;
	private LevelManager levelManager;
	
	private BitmapFont font;
	private String title;
	
	private String levelAlias;
	
	private Task finishTask;
	private float timeInSeconds;
	
	public LevelTitleScreen(LevelManager levelManager, SpriteBatch batch, AssetManagerPlus assets,
			int screenWidth, int screenHeight, String levelAlias, String title) {
		this.levelManager = levelManager;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.levelAlias = levelAlias;
		this.title = title;
		this.batch = batch;
		this.assets = assets;
		finishTask = new Task() {
			@Override
			public void run() {
				Gdx.graphics.setContinuousRendering(true);
				finish();
			}
		};
		font = generateFont(72, 1f, Color.WHITE);
	}
	
	public LevelTitleScreen(LevelManager levelManager, SpriteBatch batch, AssetManagerPlus assets, int screenWidth, int screenHeight, 
			Screen nextScreen, String levelAlias, String title, float timeInSeconds) {
		this(levelManager, batch, assets, screenWidth, screenHeight, levelAlias, title);
		this.timeInSeconds = timeInSeconds;
	}
	
	private BitmapFont generateFont(int size, float scale, Color color) {
		BitmapFont font;
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("corbelb.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = size;
		font = generator.generateFont(parameter);
		font.setScale(scale);
		font.setColor(color);
		generator.dispose();
		return font;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		font.draw(batch, title, getTextX(), getTextY());
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		if (timeInSeconds == 0) timeInSeconds = delaySeconds(title);
		Timer.schedule(finishTask, timeInSeconds);
		Sound quackSound = (Sound) assets.get("quack_dummy.ogg");
		quackSound.play();
		Gdx.graphics.setContinuousRendering(false);
		Gdx.graphics.requestRendering();
	}
	
	private float delaySeconds(String title){
		return title.replaceAll("[^\\p{L}\\p{Nd}]", "").length()*SEC_PER_CHAR;
	}

	private float getTextY() {
		float y = (screenHeight / 2) + (font.getBounds(title).height / 2); //font renders from top left corner
		return y;
	}

	private float getTextX() {
		float x = (screenWidth / 2) - (font.getBounds(title).width / 2);
		return x;
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void finish() {
		levelManager.level_notitle(levelAlias);
	}

}
