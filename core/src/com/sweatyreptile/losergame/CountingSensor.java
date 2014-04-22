package com.sweatyreptile.losergame;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;

public class CountingSensor extends Sensor implements SensorListener {

	private int totalContacts;
	private CountingSensorListener listener;
	
	public CountingSensor(SensorContactListener contactListener, CountingSensorListener listener, World world, AssetManagerPlus assets, 
			String name, float scale, int index1, int index2) {
		super(contactListener, world, assets, name, scale, index1, index2);
		this.listener = listener;
	}

	@Override
	public void beginContact(Fixture sensor, Fixture sensee) {
		totalContacts++;
		listener.contactAdded(totalContacts);
	}

	@Override
	public void endContact(Fixture sensor, Fixture sensee) {
		totalContacts--;
		listener.contactRemoved(totalContacts);
	}

}
