package com.sweatyreptile.losergame.fixtures;

import com.sweatyreptile.losergame.loaders.AssetManagerPlus;

public class DuckTopFixtureDef extends EntityFixtureDef {

	public DuckTopFixtureDef(AssetManagerPlus assets) {
		super(assets, "duck_top");
		density = 15f;
		friction = 0.4f;
		restitution = 0f;
	}

}
