package com.sweatyreptile.losergame.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.sweatyreptile.losergame.Entity;
import com.sweatyreptile.losergame.EntityFactory;
import com.sweatyreptile.losergame.PlayerInputProcessor;
import com.sweatyreptile.losergame.entities.MusicPlayer;
import com.sweatyreptile.losergame.entities.Player;
import com.sweatyreptile.losergame.entities.Sharbal;
import com.sweatyreptile.losergame.fixtures.DuckFixtureDef;
import com.sweatyreptile.losergame.fixtures.EntityFixtureDef;
import com.sweatyreptile.losergame.fixtures.MetalFixtureDef;
import com.sweatyreptile.losergame.fixtures.WoodFixtureDef;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;

public class TestScrollingLevelScreen extends ScrollingLevelScreen {
	
	private MusicPlayer radio;
	private Sharbal sharbal;
	
	public TestScrollingLevelScreen(ScreenFinishedListener listener, Screen nextScreen,
			SpriteBatch batch, AssetManagerPlus assets,
			PlayerInputProcessor playerInputProcessor, int width, int height,
			float viewportWidth, float viewportHeight, float timeLimit) {
		super(listener, nextScreen, batch, assets, playerInputProcessor, width, height,
				viewportWidth, viewportHeight, timeLimit, true, false);
		
	}
	
	@Override
	protected Player createPlayer() {
		BodyDef def = new BodyDef();
		def.position.set(viewportWidth / 2, viewportHeight / 2);
		def.fixedRotation = true;
		def.type = BodyType.DynamicBody;
		return new Player(world, contactListener, def, assets);
	}

	@Override
	protected void setupWorld() {
		
		level0Horizontal = -1.5f;
		levelEndHorizontal = viewportWidth + 1.5f;
		
		EntityFactory ef = entityFactory;
		
		ef.create("dead_duck", BodyType.DynamicBody, 1.4f, .5f, new DuckFixtureDef(assets), false);
		ef.create("wash_machine", BodyType.StaticBody, .5f, .1f, new EntityFixtureDef(assets, "wash_machine"), false);
		ef.create("cereal", BodyType.DynamicBody, 1.8f, 0.7f, new EntityFixtureDef(assets, "cereal"), false);
		ef.create("table", BodyType.StaticBody, 1.25f, .1f, new EntityFixtureDef(assets, "table"), false);
		ef.create("book_blue", BodyType.DynamicBody, 1.1f, 1.1f, new WoodFixtureDef(assets, "book_blue"), false);
		ef.create("book_red", BodyType.DynamicBody, 1.15f, 1.1f, new WoodFixtureDef(assets, "book_red"), false);
		ef.create("book_yellow", BodyType.DynamicBody, 1.2f, 1.1f, new WoodFixtureDef(assets, "book_yellow"), false);
		ef.create("shelf", BodyType.StaticBody, 1f, 1f, new EntityFixtureDef(assets, "shelf"), false);
		ef.create("pencil", BodyType.DynamicBody, 1.6f, 0.7f, new WoodFixtureDef(assets, "pencil"), false);
		
		BodyDef radioBodyDef = new BodyDef();
		radioBodyDef.type = BodyType.DynamicBody;
		radioBodyDef.position.set(1.4f, 1.1f);
		radio = new MusicPlayer(contactListener, world, radioBodyDef, assets, 
				new MetalFixtureDef(assets, "radio"), false, Entity.DEFAULT_SCREEN_WIDTH,
				viewportWidth, "baby_come_back.ogg", false, player);
		entities.put("radio", radio);
		
		BodyDef sharbalBodyDef = new BodyDef();
		sharbalBodyDef.type = BodyType.StaticBody;
		sharbalBodyDef.position.set(2.7f, 0.1f);
		sharbal = new Sharbal(world, contactListener, sharbalBodyDef, assets,
				new EntityFixtureDef(assets, "sharbal_test"), false, 
				Entity.DEFAULT_SCREEN_WIDTH, viewportWidth, player);
		entities.put("sharbal_test", sharbal);
		
		BodyDef groundDef = new BodyDef();
		groundDef.type = BodyType.StaticBody;
		groundDef.position.set(viewportWidth / 2, 0);
		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(camera.viewportWidth / 2, .1f);
		Body groundBody = world.createBody(groundDef);
		groundBody.createFixture(groundBox, 0f);
		groundBox.dispose();
		
	}
}
