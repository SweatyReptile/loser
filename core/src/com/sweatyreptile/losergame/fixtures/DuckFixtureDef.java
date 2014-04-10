package com.sweatyreptile.losergame.fixtures;

import aurelienribon.bodyeditor.FixedBodyEditorLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;

public class DuckFixtureDef extends FixtureDef {

	private FixedBodyEditorLoader loader;
	
	public DuckFixtureDef(AssetManagerPlus assets) {
		super();
		density = 15f;
		friction = 0.4f;
		restitution = 0f;
		loader = assets.get("duck.json", FixedBodyEditorLoader.class);
	}
	
	public void attach(Body duckBody, float scale, boolean flipped) {
		loader.attachFixture(duckBody, "dummy_duck", this, scale, flipped);
	}

	public FixedBodyEditorLoader getLoader() {
		return loader;
	}
	
	
	
}
