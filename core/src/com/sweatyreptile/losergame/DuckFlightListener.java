package com.sweatyreptile.losergame;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class DuckFlightListener implements ContactListener {

	private int contacts;
	
	@Override
	public void beginContact(Contact contact) {	
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		if ((fixtureA != null && fixtureA.getBody() != null && fixtureA.getBody().getUserData() != null &&
				fixtureA.getBody().getUserData().equals("flight_sensor"))
			|| (fixtureB != null && fixtureB.getBody() != null && fixtureB.getBody().getUserData() != null &&
				fixtureB.getBody().getUserData().equals("flight_sensor"))){
			contacts++;
		}
		
	}

	@Override
	public void endContact(Contact contact) {
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		if ((fixtureA != null && fixtureA.getBody() != null && fixtureA.getBody().getUserData() != null &&
				fixtureA.getBody().getUserData().equals("flight_sensor"))
			|| (fixtureB != null && fixtureB.getBody() != null && fixtureB.getBody().getUserData() != null &&
				fixtureB.getBody().getUserData().equals("flight_sensor"))){
			contacts--;
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}
	
	public boolean isFlying(){
		if (contacts == 0) return true;
		return false;
	}

}
