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
		def.position.set(0.45f, 0.45f);
		def.fixedRotation = true;
		def.type = BodyType.DynamicBody;
		return new Player(world, contactListener, def, assets);
	}

	@Override
	protected void setupWorld() {
		setOffsetY(viewportHeight / 3);
		updateCamera();
		setStrictlyDownwards(true);
		chunks.add(new LevelChunk() {
			@Override protected void setup(){
				background = (Texture) assets.get("menu_dummy_1.png");
				EntityFactory ef = new EntityFactory(assets, chunkEntities, world, contactListener, viewportWidth, width);
				ef.create("menu_platform", BodyType.StaticBody, 0.125f, 0.4f, new EntityFixtureDef(assets, "menu_platform"), false);
			}
		});
		
		chunks.add(new LevelChunk() {
			@Override protected void setup(){
				background = (Texture) assets.get("menu_dummy_2.png");
				EntityFactory ef = new EntityFactory(assets, chunkEntities, world, contactListener, viewportWidth, width);
				ef.create("menu_go_frame", BodyType.StaticBody, 2.4f, 3.1f, new EntityFixtureDef(assets, "menu_go_frame"), false);
				ef.create("menu_reset_frame", BodyType.StaticBody, 2.1f, 2.45f, new EntityFixtureDef(assets, "menu_reset_frame"), false);
				ef.create("menu_platform_2", BodyType.StaticBody, 0.71f, 0.4f, new EntityFixtureDef(assets, "menu_platform_2"), false);
			}
		});
		
		super.setupWorld();
	}

	
	
}
