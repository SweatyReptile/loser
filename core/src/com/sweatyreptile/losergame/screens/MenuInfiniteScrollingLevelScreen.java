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
import com.sweatyreptile.losergame.fixtures.WoodFixtureDef;
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
		def.position.set(viewportWidth / 2, viewportHeight / 2);
		def.fixedRotation = true;
		def.type = BodyType.DynamicBody;
		return new Player(world, contactListener, def, assets);
	}

	@Override
	protected void setupWorld() {
		chunks.add(new LevelChunk() {
			@Override protected void setup(){
				background = (Texture) assets.get("background.png");
				EntityFactory ef = new EntityFactory(assets, chunkEntities, world, contactListener, viewportWidth, width);
				ef.create("book_blue", BodyType.StaticBody, 2f, 1.5f, new WoodFixtureDef(assets, "book_blue"), false);
			}
		});
		
		chunks.add(new LevelChunk() {
			@Override protected void setup(){
				background = (Texture) assets.get("background.png");
				EntityFactory ef = new EntityFactory(assets, chunkEntities, world, contactListener, viewportWidth, width);
				ef.create("book_red", BodyType.StaticBody, 1f, 1.5f, new WoodFixtureDef(assets, "book_red"), false);
			}
		});
		
		chunks.add(new LevelChunk() {
			@Override protected void setup(){
				background = (Texture) assets.get("background.png");
				EntityFactory ef = new EntityFactory(assets, chunkEntities, world, contactListener, viewportWidth, width);
				ef.create("book_yellow", BodyType.StaticBody, 0.5f, 1f, new WoodFixtureDef(assets, "book_yellow"), false);
			}
		});
		
		BodyDef groundDef = new BodyDef();
		groundDef.type = BodyType.StaticBody;
		groundDef.position.set(viewportWidth / 2, 0);
		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(camera.viewportWidth / 2, .1f);
		Body groundBody = world.createBody(groundDef);
		groundBody.createFixture(groundBox, 0f);
		groundBox.dispose();
		
		super.setupWorld();
	}

	
	
}
