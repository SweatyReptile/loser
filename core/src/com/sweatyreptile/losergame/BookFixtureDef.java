package com.sweatyreptile.losergame;

import com.sweatyreptile.losergame.loaders.AssetManagerPlus;

public class BookFixtureDef extends EntityFixtureDef {

	public BookFixtureDef(AssetManagerPlus assets, String name) {
		super(assets, name);
		density = 85f;
	}

}
