package com.sweatyreptile.losergame.sensors;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.sweatyreptile.losergame.FixtureWrapper;

public interface SensorListener {

	public void beginContact(FixtureWrapper sensor, FixtureWrapper sensee);
	public void endContact(FixtureWrapper sensor, FixtureWrapper sensee);
	
}
