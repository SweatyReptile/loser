package com.sweatyreptile.losergame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.sweatyreptile.losergame.fixtures.DuckFixtureDef;

public class TestLevelScreen implements Screen { 
	
	private int width;
	private int height;
	private float viewportWidth;
	private float viewportHeight;
	
	private Camera camera;
	private SpriteBatch spriteRenderer;
	private Box2DDebugRenderer physRenderer;
	
	private AssetManager assets;
	
	private World physWorld;
	private Player player;
	private PlayerInputProcessor playerInputProcessor;
	
	public TestLevelScreen(SpriteBatch batch, AssetManager assets, PlayerInputProcessor playerInputProcessor,
			int width, int height, float viewportWidth, float viewportHeight){
		spriteRenderer = batch;
		this.assets = assets;
		this.width = width;
		this.height = height;
		this.viewportWidth = viewportWidth;
		this.viewportHeight = viewportHeight;
		this.playerInputProcessor = playerInputProcessor;
	}
	
	@Override
	public void render(float delta) {
		update(delta);
		Gdx.gl.glClearColor(0.5f, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		physRenderer.render(physWorld, camera.combined);
	}
	
	public void update(float delta) {
		player.update(delta);
		physWorld.step(1/60f, 6, 2); // TODO: Change step
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
		
		groundDef.type = BodyType.StaticBody;
		groundDef.position.set(viewportWidth / 2, 0);
		
		Body groundBody = physWorld.createBody(groundDef);
		
		duckDef.type = BodyType.DynamicBody;
		duckDef.position.set(viewportWidth / 2 + .2f, viewportHeight);
		duckDef.fixedRotation = true;
		
		DuckFixtureDef duckFixtureDef = new DuckFixtureDef();
		
		player = new Player(physWorld, duckDef, duckFixtureDef);
		
		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(camera.viewportWidth / 2, .1f);
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
