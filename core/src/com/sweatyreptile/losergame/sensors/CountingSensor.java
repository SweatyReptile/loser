package com.sweatyreptile.losergame.sensors;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.sweatyreptile.losergame.FixtureWrapper;
import com.sweatyreptile.losergame.LoserContactListener;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;

public class CountingSensor extends Sensor {

	private int totalContacts;
	private CountingSensorListener listener;
	private boolean dirtyAdded;
	private boolean dirtyRemoved;
	
	public CountingSensor(LoserContactListener contactListener, CountingSensorListener listener, World world, AssetManagerPlus assets, 
			String name, float scale, int index1, int index2) {
		super(contactListener, world, assets, name, scale, index1, index2);
		this.listener = listener;
	}

	@Override
	public void beginContact(FixtureWrapper sensor, FixtureWrapper sensee) {
		totalContacts++;
		dirtyAdded = true;
	}

	@Override
	public void endContact(FixtureWrapper sensor, FixtureWrapper sensee) {
		totalContacts--;
		dirtyRemoved = true;
	}

	@Override
	public void update(float delta) {
		if (dirtyAdded) {
			listener.contactAdded(totalContacts);
			dirtyAdded = false;
		}
		if (dirtyRemoved) {
			listener.contactRemoved(totalContacts);
			dirtyRemoved = true;
		}
		
	}

}
