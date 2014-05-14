package com.sweatyreptile.losergame;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.Shape.Type;
import com.sweatyreptile.losergame.entities.Player;

public class FixtureWrapper {

	Fixture fixture;
	
	public FixtureWrapper(Fixture fixture) {
		this.fixture = fixture;
	}
	
	public Fixture getFixture() {
		return fixture;
	}
	
	
	/**
	 * 
	 * @return Player if this is a fixture on the player, otherwise null
	 */
	
	public Player getPlayer() {
		if (isPlayer()) {
			return (Player) getBody().getUserData();
		}
		else{
			return null;
		}
	}
	
	public boolean isPlayer(){
		Body body = fixture.getBody();
		if (body != null && body.getUserData() != null && 
				((Entity<?>)(body.getUserData()))
					.getName().equals("duck")) return true;
		return false;
	}

	public Type getType() {
		return fixture.getType();
	}

	public int hashCode() {
		return fixture.hashCode();
	}

	public Shape getShape() {
		return fixture.getShape();
	}

	public void setSensor(boolean sensor) {
		fixture.setSensor(sensor);
	}

	public boolean isSensor() {
		return fixture.isSensor();
	}

	public void setFilterData(Filter filter) {
		fixture.setFilterData(filter);
	}

	public boolean equals(Object obj) {
		return fixture.equals(obj);
	}

	public Filter getFilterData() {
		return fixture.getFilterData();
	}

	public void refilter() {
		fixture.refilter();
	}

	public Body getBody() {
		return fixture.getBody();
	}

	public boolean testPoint(Vector2 p) {
		return fixture.testPoint(p);
	}

	public boolean testPoint(float x, float y) {
		return fixture.testPoint(x, y);
	}

	public void setDensity(float density) {
		fixture.setDensity(density);
	}

	public float getDensity() {
		return fixture.getDensity();
	}

	public float getFriction() {
		return fixture.getFriction();
	}

	public void setFriction(float friction) {
		fixture.setFriction(friction);
	}

	public float getRestitution() {
		return fixture.getRestitution();
	}

	public void setRestitution(float restitution) {
		fixture.setRestitution(restitution);
	}

	public void setUserData(Object userData) {
		fixture.setUserData(userData);
	}

	public Object getUserData() {
		return fixture.getUserData();
	}

	public String toString() {
		return fixture.toString();
	}

}
