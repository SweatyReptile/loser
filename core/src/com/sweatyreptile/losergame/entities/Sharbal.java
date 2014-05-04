package com.sweatyreptile.losergame.entities;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.sweatyreptile.losergame.Entity;
import com.sweatyreptile.losergame.LoserContactListener;
import com.sweatyreptile.losergame.fixtures.EntityFixtureDef;

public class Sharbal extends Entity<Sharbal> {

	public Sharbal(World world, LoserContactListener contactListener,
			BodyDef bodyDef, EntityFixtureDef fixtureDef, boolean flipped,
			float screenWidth, float viewportWidth, String name) {
		super(world, contactListener, bodyDef, fixtureDef, flipped, screenWidth,
				viewportWidth, name);
		// TODO Auto-generated constructor stub
	}

}
