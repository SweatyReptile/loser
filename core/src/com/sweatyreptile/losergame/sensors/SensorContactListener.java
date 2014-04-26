package com.sweatyreptile.losergame.sensors;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class SensorContactListener implements ContactListener {

	private Map<String, SensorListener> listeners;
	
	public SensorContactListener() {
		listeners = new HashMap<String, SensorListener>();
	}
	
	public void addListener(String nameData, SensorListener listener) {
		listeners.put(nameData, listener);
	}
	
	@Override
	public void beginContact(Contact contact) {
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		processFixture(fixtureA, fixtureB, true);
		processFixture(fixtureB, fixtureA, true);
	}
	
	@Override
	public void endContact(Contact contact) {
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		processFixture(fixtureA, fixtureB, false);
		processFixture(fixtureB, fixtureA, false);
	}

	private void processFixture(Fixture sensor, Fixture sensee, boolean beginContact) {
		if (userDataExists(sensor)) {
			if (sensor.isSensor() && !sensee.isSensor()) {
				SensorListener listener = listeners.get(sensor.getBody().getUserData());
				if (listener == null) throw new IllegalArgumentException("Listener named " + sensor.getUserData() + " is not in SensorContactListener");
				if (beginContact) {
					listener.beginContact(sensor, sensee);
				}
				else {
					listener.endContact(sensor, sensee);
				}
			}
		}
	}
	
	private boolean userDataExists(Fixture fixture){
		return fixture != null 
				&& fixture.getBody() != null 
				&& fixture.getBody().getUserData() != null;
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

}

