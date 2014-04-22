package com.sweatyreptile.losergame;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.sweatyreptile.losergame.fixtures.EntityFixtureDef;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;

public abstract class Sensor implements SensorListener{

	private Body sensorBody;
	private float sensorHeight;
	private WeldJoint sensorWeld;
	private boolean centerSensor;
	private Sprite currentSprite;
	private float sensorRadius;
	
	public Sensor(SensorContactListener contactListener, World world, AssetManagerPlus assets, String name, float scale,
			int index1, int index2){
		
		BodyDef sensorBodyDef = new BodyDef();
		sensorBodyDef.type = BodyType.DynamicBody;
		sensorBody = world.createBody(sensorBodyDef);
		
		EntityFixtureDef sensorDef = new EntityFixtureDef(assets, name);
		sensorDef.isSensor = true;
		sensorDef.attach(sensorBody, scale, false);
		sensorBody.setUserData(name);
		sensorHeight = extractHeight(index1, index2);
		
		contactListener.addListener(name, this);
	}
	
	public void weld(World world, Body newBody) {
		Vector2 bodyPosition = newBody.getPosition();
		if (centerSensor){
			sensorBody.setTransform(
					bodyPosition.x - sensorRadius + currentSprite.getWidth()/2,
					bodyPosition.y - sensorRadius + currentSprite.getHeight()/2,
					newBody.getAngle());
		}
		else{
			sensorBody.setTransform(bodyPosition.x, bodyPosition.y - sensorHeight, newBody.getAngle());
		}
		WeldJointDef weld = new WeldJointDef();
		weld.bodyA = newBody;
		weld.bodyB = sensorBody;
		weld.initialize(newBody, sensorBody, newBody.getWorldCenter());
		if (sensorWeld != null) {
			world.destroyJoint(sensorWeld);
		}
		sensorWeld = (WeldJoint) world.createJoint(weld);
	}
	
	private float extractHeight(int index1, int index2){
		Vector2 vertex1 = new Vector2();
		Vector2 vertex2 = new Vector2();
		
		try {
			PolygonShape sensorShape = (PolygonShape) sensorBody.getFixtureList().get(0).getShape();
			sensorShape.getVertex(index1, vertex1);
			sensorShape.getVertex(index2, vertex2);
			float sensorHeight = vertex1.y - vertex2.y;
			return sensorHeight;
		}
		catch (ClassCastException e){
			CircleShape sensorShape = (CircleShape) sensorBody.getFixtureList().get(0).getShape();
			float sensorHeight = sensorShape.getRadius()*2;
			return sensorHeight;
		}
		
	}
	
	public void setCenterRoundSensor(Sprite currentSprite){
		try {
			CircleShape sensorShape = (CircleShape) sensorBody.getFixtureList().get(0).getShape();
			sensorRadius = sensorShape.getRadius();
			this.centerSensor = true;
			this.currentSprite = currentSprite;
		}
		catch (ClassCastException e){
			e.printStackTrace();
		}
	}

	@Override
	public abstract void beginContact(Fixture sensor, Fixture sensee);
	
	@Override
	public abstract void endContact(Fixture sensor, Fixture sensee);
	
}
