package com.sweatyreptile.losergame;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class LoserContactListener implements ContactListener {

	private int flightContacts;
	private int landingContacts;
	
	@Override
	public void beginContact(Contact contact) {	
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		
		if (checkForFlightSensor(fixtureA, fixtureB) &&
				!checkForLandingSensor(fixtureA, fixtureB) &&
				!checkForGrabSensor(fixtureA, fixtureB)){
			flightContacts++;
		}
		
		if (checkForLandingSensor(fixtureA, fixtureB) &&
				!checkForFlightSensor(fixtureA, fixtureB) &&
				!checkForGrabSensor(fixtureA, fixtureB)){
			landingContacts++;
		}
		
		if (checkForGrabSensor(fixtureA, fixtureB) &&
				!checkForFlightSensor(fixtureA, fixtureB) &&
				!checkForLandingSensor(fixtureA, fixtureB)){
			//TODO
		}
		
	}

	@Override
	public void endContact(Contact contact) {
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		
		if (checkForFlightSensor(fixtureA, fixtureB) &&
			!checkForLandingSensor(fixtureA, fixtureB) &&
			!checkForGrabSensor(fixtureA, fixtureB)){
			flightContacts--;
		}
		
		if (checkForLandingSensor(fixtureA, fixtureB) &&
				!checkForFlightSensor(fixtureA, fixtureB) &&
				!checkForGrabSensor(fixtureA, fixtureB)){
			landingContacts--;
		}
		
		if (checkForGrabSensor(fixtureA, fixtureB) &&
				!checkForFlightSensor(fixtureA, fixtureB) &&
				!checkForLandingSensor(fixtureA, fixtureB)){
			//TODO
		} 
	}
	
	private boolean userDataExists(Fixture fixture){
		return fixture != null 
				&& fixture.getBody() != null 
				&& fixture.getBody().getUserData() != null;
	}
	
	private boolean checkForFlightSensor(Fixture fixtureA, Fixture fixtureB){
		if (userDataExists(fixtureA) && fixtureA.getBody().getUserData().equals("flight_sensor")){
			return true;
		}
		else if (userDataExists(fixtureB) && fixtureB.getBody().getUserData().equals("flight_sensor")){
			return true;
		}
		return false;
	}
	
	private boolean checkForLandingSensor(Fixture fixtureA, Fixture fixtureB){
		if (userDataExists(fixtureA) && fixtureA.getBody().getUserData().equals("landing_sensor")){
			return true;
		}
		else if (userDataExists(fixtureB) && fixtureB.getBody().getUserData().equals("landing_sensor")){
			return true;
		}
		return false;
	}
	
	private boolean checkForGrabSensor(Fixture fixtureA, Fixture fixtureB){
		if (userDataExists(fixtureA) && fixtureA.getBody().getUserData().equals("grab_sensor")){
			return true;
		}
		else if (userDataExists(fixtureB) && fixtureB.getBody().getUserData().equals("grab_sensor")){
			return true;
		}
		return false;
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}
	
	public boolean isFlightSensorContacting(){
		if (flightContacts > 0) return true;
		return false;
	}
	
	public boolean isLandingSensorContacting(){
		if (landingContacts > 0) return true;
		return false;
	}

}
