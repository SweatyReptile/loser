package com.sweatyreptile.losergame.screens;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.sweatyreptile.losergame.fixtures.EntityFixtureDef;

public class EntityData {

	private String name;
	private BodyType bodyType;
	private float x;
	private float y;
	private EntityFixtureDef fixtureDef;
	private boolean flipped;
	
	public EntityData(String name, BodyType bodyType, float x, float y,
			EntityFixtureDef fixtureDef, boolean flipped) {
		this.name = name;
		this.bodyType = bodyType;
		this.x = x;
		this.y = y;
		this.fixtureDef = fixtureDef;
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

	public EntityFixtureDef getFixtureDef() {
		return fixtureDef;
	}

	public void setFixtureDef(EntityFixtureDef fixtureDef) {
		this.fixtureDef = fixtureDef;
	}

	public boolean isFlipped() {
		return flipped;
	}

	public void setFlipped(boolean flipped) {
		this.flipped = flipped;
	}
	
	

}
