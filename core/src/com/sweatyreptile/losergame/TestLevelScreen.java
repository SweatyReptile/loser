package com.sweatyreptile.losergame;

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
import com.sweatyreptile.losergame.fixtures.BookFixtureDef;
import com.sweatyreptile.losergame.fixtures.DuckFixtureDef;
import com.sweatyreptile.losergame.fixtures.EntityFixtureDef;
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
	
	private World physWorld;
	private Player player;
	private PlayerInputProcessor playerInputProcessor;
	
	private Map<String, Entity> entities;
	
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
				
		player.render(spriteRenderer);
		for (Entity entity : entities.values()){
			entity.render(spriteRenderer);
		}
		spriteRenderer.end();
		
		physRenderer.render(physWorld, camera.combined);
	}
	
	public void update(float delta) {
		physWorld.step(1/60f, 6, 2); // TODO: Change step
		
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
		setupWorld();
	}
	
	private void setupWorld() {
		
		BodyDef groundDef = new BodyDef();
		BodyDef duckDef = new BodyDef();
		BodyDef deadDuckDef = new BodyDef();
		BodyDef washMachineDef = new BodyDef();
		BodyDef cerealDef = new BodyDef();
		BodyDef tableDef = new BodyDef();
		BodyDef shelfDef = new BodyDef();
		BodyDef bookBlueDef = new BodyDef();
		BodyDef bookRedDef = new BodyDef();
		BodyDef bookYellowDef = new BodyDef();
		
		groundDef.type = BodyType.StaticBody;
		groundDef.position.set(viewportWidth / 2, 0);
		
		duckDef.type = BodyType.DynamicBody;
		duckDef.position.set(2f, viewportHeight/2);
		duckDef.fixedRotation = true;
		
		deadDuckDef.type = BodyType.DynamicBody;
		deadDuckDef.position.set(1.4f, .5f);
		
		washMachineDef.type = BodyType.StaticBody;
		washMachineDef.position.set(.5f, .1f);
		
		cerealDef.type = BodyType.DynamicBody;
		cerealDef.position.set(1.8f, 0.7f);
		
		tableDef.type = BodyType.StaticBody;
		tableDef.position.set(1.25f, .1f);
		
		shelfDef.type = BodyType.StaticBody;
		shelfDef.position.set(1f, 1f);
		
		bookBlueDef.type = BodyType.DynamicBody;
		bookBlueDef.position.set(1.1f, 1.1f);
		
		bookRedDef.type = BodyType.DynamicBody;
		bookRedDef.position.set(1.15f, 1.1f);
		
		bookYellowDef.type = BodyType.DynamicBody;
		bookYellowDef.position.set(1.2f, 1.1f);
		
		player = new Player(physWorld, duckDef, assets);
		entities.put("dead_duck", new Entity(physWorld, deadDuckDef, new DuckFixtureDef(assets), .2f, false));
		entities.put("wash_machine", new Entity(physWorld, washMachineDef, new EntityFixtureDef(assets, "wash_machine"), .35f, false));
		entities.put("cereal", new Entity(physWorld, cerealDef, new EntityFixtureDef(assets, "cereal"), .15f, false));
		entities.put("table", new Entity(physWorld, tableDef, new EntityFixtureDef(assets, "table"), 1.25f, false));
		entities.put("shelf", new Entity(physWorld, shelfDef, new EntityFixtureDef(assets, "shelf"), .5f, false));
		entities.put("book_blue", new Entity(physWorld, bookBlueDef, new BookFixtureDef(assets, "book_blue"), .05f, false));
		entities.put("book_red", new Entity(physWorld, bookRedDef, new BookFixtureDef(assets, "book_red"), .035f, false));
		entities.put("book_yellow", new Entity(physWorld, bookYellowDef, new BookFixtureDef(assets, "book_yellow"), false, width, viewportWidth));
		
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
