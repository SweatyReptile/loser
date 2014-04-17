package com.sweatyreptile.losergame.fixtures;

import com.sweatyreptile.losergame.loaders.AssetManagerPlus;

public class DuckFixtureDef extends EntityFixtureDef {
	
	public DuckFixtureDef(AssetManagerPlus assets) {
		super(assets, "duck");
		density = 15f;
		friction = 0.4f;
		restitution = 0f;
		
	}
	
}
