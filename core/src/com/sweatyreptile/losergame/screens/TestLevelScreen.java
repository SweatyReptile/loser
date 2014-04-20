package com.sweatyreptile.losergame.screens;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.sweatyreptile.losergame.Entity;
import com.sweatyreptile.losergame.EntityFactory;
import com.sweatyreptile.losergame.LoserContactListener;
import com.sweatyreptile.losergame.Player;
import com.sweatyreptile.losergame.PlayerInputProcessor;
import com.sweatyreptile.losergame.fixtures.DuckFixtureDef;
import com.sweatyreptile.losergame.fixtures.EntityFixtureDef;
import com.sweatyreptile.losergame.fixtures.MetalFixtureDef;
import com.sweatyreptile.losergame.fixtures.WoodFixtureDef;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;

public class TestLevelScreen implements Screen { 
	
	private int width;
	private int height;
	private float viewportWidth;
	private float viewportHeight;
	
	private Camera camera;
	private SpriteBatch spriteRenderer;
	private Box2DDebugRenderer physRenderer;
	
	private AssetManagerPlus assets;
	private Texture background;
	
	private World physWorld;
	private Player player;
	private PlayerInputProcessor playerInputProcessor;
	
	private Map<String, Entity> entities;
	
	private EntityFactory entityFactory;
	private LoserContactListener contactListener;
	
	public TestLevelScreen(SpriteBatch batch, AssetManagerPlus assets, PlayerInputProcessor playerInputProcessor,
			int width, int height, float viewportWidth, float viewportHeight){
		spriteRenderer = batch;
		this.assets = assets;
		this.width = width;
		this.height = height;
		this.viewportWidth = viewportWidth;
		this.viewportHeight = viewportHeight;
		this.playerInputProcessor = playerInputProcessor;
		this.entities = new HashMap<String, Entity>();
	}
	
	@Override
	public void render(float delta) {
		update(delta);
		Gdx.gl.glClearColor(0.5f, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		spriteRenderer.begin();
		
		spriteRenderer.disableBlending();		
		spriteRenderer.draw(background, 0f, 0f, viewportWidth, viewportHeight);
		spriteRenderer.enableBlending();
		
		player.render(spriteRenderer);
		for (Entity entity : entities.values()){
			entity.render(spriteRenderer);
		}
		
		spriteRenderer.end();
		
		physRenderer.render(physWorld, camera.combined);
	}
	
	public void update(float delta) {
		physWorld.step(1/60f, 6, 2); // TODO: Change step
		
		if (!contactListener.isFlightSensorContacting() && !player.isFlying()) player.fly();
		else if (contactListener.isLandingSensorContacting() && player.isFlying()) player.land();
		player.update(delta);
		for (Entity entity : entities.values()){
			entity.update(delta);
		}
	}

	@Override
	public void resize(int width, int height) {
		camera.position.set(new Vector3(viewportWidth/ 2, viewportHeight/ 2, 0));
		camera.viewportHeight = viewportHeight;
		camera.viewportWidth = viewportWidth;
		camera.update();
		spriteRenderer.setProjectionMatrix(camera.combined);
	}

	@Override
	public void show() {
		camera = new OrthographicCamera(width, height);
		
		resize(width, height);
		
		physWorld = new World(new Vector2(0f, -9.8f), true);
		physRenderer = new Box2DDebugRenderer();
		
		entityFactory = new EntityFactory(assets, entities, physWorld, viewportWidth, width);
		background = assets.get("background.png");
		setupWorld();
	}
	
	private void setupWorld() {
		
		contactListener = new LoserContactListener();
		physWorld.setContactListener(contactListener);
		
		EntityFactory ef = entityFactory;
		
		BodyDef duckDef = new BodyDef();
		
		duckDef.type = BodyType.DynamicBody;
		duckDef.position.set(2f, viewportHeight/2);
		duckDef.fixedRotation = true;
		
		player = new Player(physWorld, duckDef, assets, contactListener);
		
		ef.create("dead_duck", BodyType.DynamicBody, 1.4f, .5f, new DuckFixtureDef(assets), false);
		ef.create("wash_machine", BodyType.StaticBody, .5f, .1f, new EntityFixtureDef(assets, "wash_machine"), false);
		ef.create("cereal", BodyType.DynamicBody, 1.8f, 0.7f, new EntityFixtureDef(assets, "cereal"), false);
		ef.create("table", BodyType.StaticBody, 1.25f, .1f, new EntityFixtureDef(assets, "table"), false);
		ef.create("book_blue", BodyType.DynamicBody, 1.1f, 1.1f, new WoodFixtureDef(assets, "book_blue"), false);
		ef.create("book_red", BodyType.DynamicBody, 1.15f, 1.1f, new WoodFixtureDef(assets, "book_red"), false);
		ef.create("book_yellow", BodyType.DynamicBody, 1.2f, 1.1f, new WoodFixtureDef(assets, "book_yellow"), false);
		ef.create("shelf", BodyType.StaticBody, 1f, 1f, new EntityFixtureDef(assets, "shelf"), false);
		ef.create("pencil", BodyType.DynamicBody, 1.6f, 0.7f, new WoodFixtureDef(assets, "pencil"), false);
		ef.create("radio", BodyType.DynamicBody, 1.4f, 1.1f, new MetalFixtureDef(assets, "radio"), false);

		
		BodyDef groundDef = new BodyDef();
		groundDef.type = BodyType.StaticBody;
		groundDef.position.set(viewportWidth / 2, 0);
		PolygonShape groundBox = new PolygonShape();
		
		groundBox.setAsBox(camera.viewportWidth / 2, .1f);
		Body groundBody = physWorld.createBody(groundDef);
		groundBody.createFixture(groundBox, 0f);
		
		groundBox.dispose();
		
		playerInputProcessor.setPlayer(player);
		
	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		physWorld.dispose();
	}

}
