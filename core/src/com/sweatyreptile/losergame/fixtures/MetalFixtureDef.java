package com.sweatyreptile.losergame.fixtures;

import com.sweatyreptile.losergame.loaders.AssetManagerPlus;

public class MetalFixtureDef extends EntityFixtureDef{

	public MetalFixtureDef(AssetManagerPlus assets, String name) {
		super(assets, name);
		density = 415f;
		friction = 0.1f;
		
	}

}
