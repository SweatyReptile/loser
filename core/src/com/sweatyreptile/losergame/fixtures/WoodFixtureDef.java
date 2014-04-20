package com.sweatyreptile.losergame.fixtures;

import com.sweatyreptile.losergame.loaders.AssetManagerPlus;

public class WoodFixtureDef extends EntityFixtureDef {

	public WoodFixtureDef(AssetManagerPlus assets, String name) {
		super(assets, name);
		density = 85f;
	}

}
