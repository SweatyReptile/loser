package com.sweatyreptile.losergame.fixtures;

import com.badlogic.gdx.physics.box2d.FixtureDef;

public class DuckFixtureDef extends FixtureDef {

	public DuckFixtureDef() {
		super();
		density = 0.5f;
		friction = 0.4f;
		restitution = .5f;
	}

	
	
}
