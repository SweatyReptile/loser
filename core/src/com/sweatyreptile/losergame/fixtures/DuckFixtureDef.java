package com.sweatyreptile.losergame.fixtures;

import aurelienribon.bodyeditor.FixedBodyEditorLoader;

import com.sweatyreptile.losergame.EntityFixtureDef;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;

public class DuckFixtureDef extends EntityFixtureDef {

	private FixedBodyEditorLoader loader;
	
	public DuckFixtureDef(AssetManagerPlus assets) {
		super(assets, "duck");
		density = 15f;
		friction = 0.4f;
		restitution = 0f;
		
	}
	
}
