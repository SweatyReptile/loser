package com.sweatyreptile.losergame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TestLevelScreen implements Screen {

	private SpriteBatch spriteRenderer;
	private Texture image;
	private AssetManager assets;
	
	public TestLevelScreen(SpriteBatch batch, AssetManager assets){
		spriteRenderer = batch;
		this.assets = assets;
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.5f, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		spriteRenderer.begin();
		spriteRenderer.draw(image, 0, 0);
		spriteRenderer.end();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {
		image = assets.get("badlogic.jpg", Texture.class);
	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	// Don't dispose of the renderer here, so that 
	// it may be used by other Screens
	@Override
	public void dispose() {

	}

}
