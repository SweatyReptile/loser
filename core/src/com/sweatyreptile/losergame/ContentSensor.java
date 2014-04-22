package com.sweatyreptile.losergame;

import java.util.ArrayList;
import java.util.Stack;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;

public class ContentSensor extends Sensor {

	ArrayList<String> test;
	Stack<Body> contents;
	
	public ContentSensor(SensorContactListener contactListener, World world,
			AssetManagerPlus assets, String name, float scale, int index1,
			int index2) {
		super(contactListener, world, assets, name, scale, index1, index2);
		contents = new Stack<Body>();
		
	}

	@Override
	public void beginContact(Fixture sensor, Fixture sensee) {
		//if (sensee.getBody() != null)
		Body senseeBody = sensee.getBody();
		if (!contents.contains(senseeBody)) contents.add(senseeBody);

	}

	@Override
	public void endContact(Fixture sensor, Fixture sensee) {
		Body senseeBody = sensee.getBody();
		if (contents.contains(senseeBody)) contents.remove(senseeBody);

	}
	
	public Body getNewestBody() {
		if (!contents.isEmpty()) return contents.peek();
		return null;
	}

}
