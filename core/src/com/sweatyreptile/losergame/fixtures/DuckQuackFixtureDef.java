package com.sweatyreptile.losergame.fixtures;

import com.sweatyreptile.losergame.loaders.AssetManagerPlus;

public class DuckQuackFixtureDef extends EntityFixtureDef {

	public DuckQuackFixtureDef(AssetManagerPlus assets) {
		super(assets, "duck_quack");
		density = 15f;
		friction = 0.4f;
		restitution = 0f;
	}

}
