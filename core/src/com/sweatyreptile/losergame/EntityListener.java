package com.sweatyreptile.losergame;

import com.badlogic.gdx.physics.box2d.Fixture;

public interface EntityListener <T extends Entity<?>> {

	public void beginContact(T entity, FixtureWrapper entityFixture, FixtureWrapper contactee);
	public void endContact(T entity, FixtureWrapper entityFixture, FixtureWrapper contactee);
	
}
