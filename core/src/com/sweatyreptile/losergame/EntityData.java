package com.sweatyreptile.losergame;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.sweatyreptile.losergame.fixtures.EntityFixtureDef;

public class EntityData {

	private String name;
	private BodyType bodyType;
	private float x;
	private float y;
	private boolean flipped;
	private float density;
	private float restitution;
	private float friction;
	private boolean isSensor;
	private boolean isSpecial;
	
	public EntityData(){
		
	}
	
	public EntityData(String name, BodyType bodyType, float x, float y,
			float density, float restitution, float friction, boolean isSensor, boolean isSpecial, boolean flipped) {
		this.name = name;
		this.bodyType = bodyType;
		this.x = x;
		this.y = y;
		this.density = density;
		this.restitution = restitution;
		this.friction = friction;
		this.isSensor = isSensor;
		this.isSpecial = isSpecial;
		this.flipped = flipped;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BodyType getBodyType() {
		return bodyType;
	}

	public void setBodyType(BodyType bodyType) {
		this.bodyType = bodyType;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getDensity() {
		return density;
	}

	public void setDensity(float density) {
		this.density = density;
	}

	public float getRestitution() {
		return restitution;
	}

	public void setRestitution(float restitution) {
		this.restitution = restitution;
	}

	public float getFriction() {
		return friction;
	}

	public void setFriction(float friction) {
		this.friction = friction;
	}

	public boolean isSensor() {
		return isSensor;
	}

	public void setSensor(boolean isSensor) {
		this.isSensor = isSensor;
	}

	public boolean isFlipped() {
		return flipped;
	}

	public void setFlipped(boolean flipped) {
		this.flipped = flipped;
	}

	public boolean isSpecial() {
		return false;
	}
	
	public boolean setSpecial(boolean special) {
		return special;
	}
	
	

}
