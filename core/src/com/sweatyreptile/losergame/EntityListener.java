package com.sweatyreptile.losergame;

import com.badlogic.gdx.physics.box2d.Fixture;

public interface EntityListener <T extends Entity<?>> {

	public void beginContact(T entity, Fixture sensee, Fixture sensee2);
	public void endContact(T entity, Fixture sensee, Fixture sensee2);
	
}
