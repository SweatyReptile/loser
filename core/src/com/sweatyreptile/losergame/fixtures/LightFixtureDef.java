package com.sweatyreptile.losergame.fixtures;

import com.sweatyreptile.losergame.loaders.AssetManagerPlus;

public class LightFixtureDef extends EntityFixtureDef {

	public LightFixtureDef(AssetManagerPlus assets, String name) {
		super(assets, name);
		density = 5f;
	}
	
}
