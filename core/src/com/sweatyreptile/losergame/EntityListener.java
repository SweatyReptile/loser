package com.sweatyreptile.losergame;

import com.badlogic.gdx.physics.box2d.Fixture;

public interface EntityListener <T extends Entity<?>> {

	public void beginContact(T entity, Fixture sensee);
	public void endContact(T entity, Fixture sensee);
	
}
