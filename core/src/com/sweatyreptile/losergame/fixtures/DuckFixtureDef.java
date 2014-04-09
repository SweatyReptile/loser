package com.sweatyreptile.losergame.fixtures;

import aurelienribon.bodyeditor.FixedBodyEditorLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class DuckFixtureDef extends FixtureDef {

	private FixedBodyEditorLoader loader;
	
	public DuckFixtureDef() {
		super();
		density = 15f;
		friction = 0.4f;
		restitution = 0f;
		loader = new FixedBodyEditorLoader(Gdx.files.internal("duck.json"));
	}
	
	public void attach(Body duckBody, float scale) {
		loader.attachFixture(duckBody, "dummy_duck", this, scale);
	}
	
}
