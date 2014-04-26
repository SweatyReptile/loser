package com.sweatyreptile.losergame.sensors;

import com.badlogic.gdx.physics.box2d.Fixture;

public interface SensorListener {

	public void beginContact(Fixture sensor, Fixture sensee);
	public void endContact(Fixture sensor, Fixture sensee);
	
}
