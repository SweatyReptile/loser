package com.sweatyreptile.losergame.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.sweatyreptile.losergame.EntityFactory;
import com.sweatyreptile.losergame.LevelChunk;
import com.sweatyreptile.losergame.PlayerInputProcessor;
import com.sweatyreptile.losergame.entities.Player;
import com.sweatyreptile.losergame.fixtures.EntityFixtureDef;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;

public class MenuInfiniteScrollingLevelScreen extends
		InfiniteScrollingLevelScreen {

	public MenuInfiniteScrollingLevelScreen(ScreenFinishedListener listener,
			Screen nextScreen, SpriteBatch batch, AssetManagerPlus assets,
			PlayerInputProcessor playerInputProcessor, int width, int height,
			float viewportWidth, float viewportHeight) {
		super(listener, nextScreen, batch, assets, playerInputProcessor, width, height,
				viewportWidth, viewportHeight, false, true);
		// TODO Auto-generated constructor stub
	}
	

	@Override
	protected Player createPlayer() {
		BodyDef def = new BodyDef();
		def.position.set(0.5f, 0.8f);
		def.fixedRotation = true;
		def.type = BodyType.DynamicBody;
		return new Player(world, contactListener, def, assets);
	}

	@Override
	protected void setupWorld() {
		chunks.add(new LevelChunk() {
			@Override protected void setup(){
				background = (Texture) assets.get("menu_dummy_1.png");
				EntityFactory ef = new EntityFactory(assets, chunkEntities, world, contactListener, viewportWidth, width);
				ef.create("menu_platform", BodyType.StaticBody, 0.255f, 0.785f, new EntityFixtureDef(assets, "menu_platform"), false);
			}
		});
		
		chunks.add(new LevelChunk() {
			@Override protected void setup(){
				background = (Texture) assets.get("menu_dummy_2.png");
				EntityFactory ef = new EntityFactory(assets, chunkEntities, world, contactListener, viewportWidth, width);
				ef.create("menu_go_frame", BodyType.StaticBody, 1.95f, 1.2f, new EntityFixtureDef(assets, "menu_go_frame"), false);
				ef.create("menu_reset_frame", BodyType.StaticBody, 1.75f, 0.34f, new EntityFixtureDef(assets, "menu_reset_frame"), false);
			}
		});
		
		/*BodyDef groundDef = new BodyDef();
		groundDef.type = BodyType.StaticBody;
		groundDef.position.set(viewportWidth / 2, 0);
		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(camera.viewportWidth / 2, .1f);
		Body groundBody = world.createBody(groundDef);
		groundBody.createFixture(groundBox, 0f);
		groundBox.dispose();*/
		
		super.setupWorld();
	}

	
	
}
