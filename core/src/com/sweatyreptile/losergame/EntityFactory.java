package com.sweatyreptile.losergame;

import java.util.Map;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.sweatyreptile.losergame.fixtures.EntityFixtureDef;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;
import com.sweatyreptile.losergame.screens.EntityData;

public class EntityFactory {

	private Map<String, Entity<?>> entities;
	private World world;
	private LoserContactListener contactListener;
	private float viewportWidth;
	private float screenWidth;
	
	public EntityFactory(AssetManagerPlus assets, Map<String, Entity<?>> entities,
			World world, LoserContactListener contactListener, float viewportWidth, float screenWidth) {
		super();
		this.entities = entities;
		this.world = world;
		this.contactListener = contactListener;
		this.viewportWidth = viewportWidth;
		this.screenWidth = screenWidth;
	}

	public void create(String name, BodyType bodyType, 
			float xPosition, float yPosition, 
			EntityFixtureDef entityFixtureDef, boolean flip){
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(xPosition, yPosition);
		bodyDef.type = bodyType;
		
		Entity<?> entity = new Entity<Entity<?>>(world, contactListener, bodyDef, entityFixtureDef, flip, screenWidth, viewportWidth, name);
		entities.put(name, entity);
	}

	public void create(EntityData entityData) {
		String name = entityData.getName();
		BodyType bodyType = entityData.getBodyType();
		float x = entityData.getX();
		float y = entityData.getY();
		EntityFixtureDef fixtureDef = entityData.getFixtureDef();
		boolean flipped = entityData.isFlipped();
		create(name, bodyType, x, y, fixtureDef, flipped);
	}

	
}
