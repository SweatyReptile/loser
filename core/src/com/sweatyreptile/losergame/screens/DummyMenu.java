package com.sweatyreptile.losergame.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.sweatyreptile.losergame.Entity;
import com.sweatyreptile.losergame.EntityFactory;
import com.sweatyreptile.losergame.EntityListener;
import com.sweatyreptile.losergame.FixtureWrapper;
import com.sweatyreptile.losergame.LevelChunk;
import com.sweatyreptile.losergame.LevelManager;
import com.sweatyreptile.losergame.PlayerInputProcessor;
import com.sweatyreptile.losergame.entities.Player;
import com.sweatyreptile.losergame.fixtures.EntityFixtureDef;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;

public class DummyMenu extends InfiniteScrollingLevelScreen {

	public DummyMenu() {
		super();
	}
	
	public DummyMenu(LevelManager levelManager, SpriteBatch batch, AssetManagerPlus assets,
			PlayerInputProcessor playerInputProcessor, int width, int height,
			float viewportWidth, float viewportHeight, String alias, String levelName) {
		super(levelManager, batch, assets, playerInputProcessor, width, height,
				viewportWidth, viewportHeight, alias, levelName, false, true);
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
		setupBorders(true, false, true, true);
		player.standRight();
		setOffsetY(viewportHeight / 3);
		updateCamera();
		vertical = true;
		setStrictlyDownwards(true);
		chunks.add(new LevelChunk() {
			@Override protected void setup(){
				background = (Texture) assets.get("menu_dummy_1.png");
				
				EntityFactory ef = new EntityFactory(assets, chunkEntities, world, contactListener, viewportWidth, width);
				ef.create("menu_platform", BodyType.StaticBody, 0.125f, 0.4f, new EntityFixtureDef(assets, "menu_platform"), false);
			}
		});
		
		chunks.add(new LevelChunk() {
			@SuppressWarnings("unchecked")
			@Override protected void setup(){
				background = (Texture) assets.get("menu_dummy_2.png");
				
				EntityFactory ef = new EntityFactory(assets, chunkEntities, world, contactListener, viewportWidth, width);
				ef.create("menu_go_frame", BodyType.StaticBody, 2.4f, 3.1f, new EntityFixtureDef(assets, "menu_go_frame"), false);
				ef.create("menu_reset_frame", BodyType.StaticBody, 2.1f, 2.45f, new EntityFixtureDef(assets, "menu_reset_frame"), false);
				ef.create("menu_platform_2", BodyType.StaticBody, 0.71f, 0.4f, new EntityFixtureDef(assets, "menu_platform_2"), false);
				
				chunkEntities.get("menu_go_frame").addListener(new EntityListener() {
					
					private boolean switchCalled;
					
					@Override
					public void beginContact(Entity entity,
							FixtureWrapper entityFixture, FixtureWrapper contactee) {
						Player player = contactee.getPlayer();
						if (player != null) {
							if (player.isQuacking() && !switchCalled) {
								levelManager.level("test_home");
								switchCalled = true;
							}
						}
					}
					@Override
					public void endContact(Entity entity,
							FixtureWrapper entityFixture, FixtureWrapper contactee) {
					}
					
				});
			}
		});
		
		super.setupWorld();
	}

	
	
}
