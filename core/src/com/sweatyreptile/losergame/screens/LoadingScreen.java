package com.sweatyreptile.losergame.screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.sweatyreptile.losergame.LevelManager;

public class LoadingScreen implements FinishableScreen{

	private List<LoadingFinishedListener> listeners;
	private AssetManager loader;
	private LevelManager levelManager;
	
	public LoadingScreen(AssetManager loader, LevelManager levelManager) {
		new World(new Vector2(0, 0), false);
		this.loader = loader;
		this.levelManager = levelManager;
		this.listeners = new ArrayList<LoadingFinishedListener>();
	}

	@Override
	public void render(float delta) {
		if (loader.update()){
			finish();
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

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
		for (LoadingFinishedListener listener : listeners){
			listener.run();
		}
		levelManager.level("test_menu");
	}
	
	public void addListener(LoadingFinishedListener listener){
		listeners.add(listener);
	}

	public static interface LoadingFinishedListener {
		public void run();
	}
	
}
