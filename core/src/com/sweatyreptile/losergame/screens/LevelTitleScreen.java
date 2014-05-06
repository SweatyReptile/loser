package com.sweatyreptile.losergame.screens;

import java.awt.Font;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class LevelTitleScreen extends FinishableScreen {

	private static final float SEC_PER_CHAR = 0.0025f;
	private int screenWidth;
	private int screenHeight;
	
	private BitmapFont font;
	private String title;
	private SpriteBatch batch;
	
	private Task finishTask;
	private float timeInSeconds;
	
	public LevelTitleScreen(SpriteBatch batch, int screenWidth, int screenHeight, 
			ScreenFinishedListener finishListener,
			Screen nextScreen, String title) {
		super(finishListener, nextScreen);
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.title = title;
		this.batch = batch;
		finishTask = new Task() {
			@Override
			public void run() {
				finish();
			}
		};
		font = generateFont(72, 1f, Color.WHITE); //TODO this is a default, make overloaded constructors, also fix scaling
	}
	
	public LevelTitleScreen(SpriteBatch batch, int screenWidth, int screenHeight, 
			ScreenFinishedListener finishListener,
			Screen nextScreen, String title, float timeInSeconds) {
		this(batch, screenWidth, screenHeight, finishListener, nextScreen, title);
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
		//quack
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

}
