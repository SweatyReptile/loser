package com.sweatyreptile.losergame;

import aurelienribon.bodyeditor.FixedBodyEditorLoader;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;

public class EntityFixtureDef extends FixtureDef {

	private FixedBodyEditorLoader loader;
	private String name;
	
	public EntityFixtureDef(AssetManagerPlus assets, String name) {
		loader = assets.get("duck.json", FixedBodyEditorLoader.class);
		this.name = name;
	}
	
	public void attach(Body body, float scale, boolean flipped) {
		loader.attachFixture(body, name, this, scale, flipped);
	}
	
}
