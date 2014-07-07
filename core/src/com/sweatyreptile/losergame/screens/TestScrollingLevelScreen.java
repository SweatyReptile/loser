package com.sweatyreptile.losergame.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.sweatyreptile.losergame.Entity;
import com.sweatyreptile.losergame.EntityData;
import com.sweatyreptile.losergame.EntityFactory;
import com.sweatyreptile.losergame.entities.MusicPlayer;
import com.sweatyreptile.losergame.entities.Player;
import com.sweatyreptile.losergame.entities.Sharbal;
import com.sweatyreptile.losergame.fixtures.DuckFixtureDef;
import com.sweatyreptile.losergame.fixtures.EntityFixtureDef;
import com.sweatyreptile.losergame.fixtures.LightFixtureDef;
import com.sweatyreptile.losergame.fixtures.MetalFixtureDef;
import com.sweatyreptile.losergame.fixtures.WoodFixtureDef;

public class TestScrollingLevelScreen extends ScrollingLevelScreen {
	
	private MusicPlayer radio;
	private Sharbal sharbal;
	
	
	public TestScrollingLevelScreen() {
		super();
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
		
		horizontal = true;
		
		setupBorders(true, false);
		
		setBackground("img/bg/background_extended.png");
	
		level0Horizontal = 0f;
		levelEndHorizontal = bgWidth;
		
		EntityFactory ef = entityFactory;
		
		ef.create("wall_left", BodyType.StaticBody, -BORDER_WIDTH, 0f, new EntityFixtureDef(assets, "vertical_border"), false)
			.setSpecial(true);
		ef.create("wall_right", BodyType.StaticBody, bgWidth, 0f, new EntityFixtureDef(assets, "vertical_border"), false)
			.setSpecial(true);
		
		
		BodyDef radioBodyDef = new BodyDef();
		radioBodyDef.type = BodyType.DynamicBody;
		radioBodyDef.position.set(1.4f, 1.1f);
		radio = new MusicPlayer(contactListener, world, radioBodyDef, assets, 
				new MetalFixtureDef(assets, "radio"), false, Entity.DEFAULT_SCREEN_WIDTH,
				viewportWidth, "music/baby_come_back.ogg", false, player);
		entities.put("radio", radio);
		
		BodyDef sharbalBodyDef = new BodyDef();
		sharbalBodyDef.type = BodyType.StaticBody;
		sharbalBodyDef.position.set(4.5f, 0.1f);
		sharbal = new Sharbal(world, contactListener, sharbalBodyDef, assets,
				new EntityFixtureDef(assets, "sharbal_test"), false, 
				Entity.DEFAULT_SCREEN_WIDTH, viewportWidth, player);
		entities.put("sharbal_test", sharbal);
		
		BodyDef groundDef = new BodyDef();
		groundDef.type = BodyType.StaticBody;
		groundDef.position.set(bgWidth / 2, 0);
		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(bgWidth, .1f);
		Body groundBody = world.createBody(groundDef);
		groundBody.createFixture(groundBox, 0f);
		groundBox.dispose();
		
	}
}
