package com.sweatyreptile.losergame.sensors;

import java.util.Stack;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.sweatyreptile.losergame.LoserContactListener;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;

public class ContentSensor extends Sensor {

	private Stack<Body> contents;
	private ContentSensorListener listener;
	private boolean dirtyAdded;
	private boolean dirtyRemoved;
	
	public ContentSensor(LoserContactListener contactListener, ContentSensorListener listener, World world,
			AssetManagerPlus assets, String name, float scale, int index1,
			int index2) {
		super(contactListener, world, assets, name, scale, index1, index2);
		contents = new Stack<Body>();
		this.listener = listener;
	}
	
	@SuppressWarnings("unchecked")
	public void update(float delta) {
		if (dirtyAdded) {
			listener.bodyAdded((Stack<Body>) contents.clone());
			dirtyAdded = false;
		}
		if (dirtyRemoved) {
			listener.bodyRemoved((Stack<Body>) contents.clone());
			dirtyRemoved = false;
		}
	}

	@Override
	public void beginContact(Fixture sensor, Fixture sensee) {
		Body senseeBody = sensee.getBody();
		//if (!contents.contains(senseeBody)) 
		contents.add(senseeBody);
		if (listener != null) {
			dirtyAdded = true;
		}
	}

	@Override
	public void endContact(Fixture sensor, Fixture sensee) {
		Body senseeBody = sensee.getBody();
		if (contents.contains(senseeBody)) {
			contents.remove(senseeBody);
		}
		if (listener != null) {
			dirtyRemoved = true;
		}
	}
	
	public boolean isEmpty() {
		return contents.empty();
	}

	public Body getNewestBody() {
		return contents.peek();
	}
}
