package com.sweatyreptile.losergame;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.sweatyreptile.losergame.sensors.SensorListener;

public class LoserContactListener implements ContactListener {

	private Map<String, SensorListener> sensorListeners;
	private Map<String, EntityListener<?>> entityListeners;
	
	public LoserContactListener() {
		sensorListeners = new HashMap<String, SensorListener>();
		entityListeners = new HashMap<String, EntityListener<?>>();
	}
	
	public void addSensorListener(String nameData, SensorListener listener) {
		sensorListeners.put(nameData, listener);
	}
	
	public void addEntityListener(String name, EntityListener<?> listener) {
		entityListeners.put(name, listener);
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

	public void processFixture(Fixture contactor, Fixture contactee, boolean beginContact) {
		if (userDataExists(contactor)) {
			if (contactor.isSensor() && !contactee.isSensor()) {
				SensorListener listener = sensorListeners.get(contactor.getBody().getUserData());
				if (listener == null) throw new IllegalArgumentException("Listener named " + contactor.getUserData() + " is not in SensorContactListener");
				if (beginContact) {
					listener.beginContact(contactor, contactee);
				}
				else {
					listener.endContact(contactor, contactee);
				}
			}
			else if (!contactor.isSensor()){
				Entity<?> entity = (Entity<?>) contactor.getBody().getUserData();
				EntityListener<Entity<?>> listener = (EntityListener<Entity<?>>) entityListeners.get(entity.getName());
				if (listener != null){
					if (beginContact) {
						listener.beginContact(entity, contactor, contactee);
					}
					else {
						listener.endContact(entity, contactor, contactee);
					}
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

