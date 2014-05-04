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
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.sweatyreptile.losergame.Entity;
import com.sweatyreptile.losergame.EntityFactory;
import com.sweatyreptile.losergame.LoserContactListener;
import com.sweatyreptile.losergame.PlayerInputProcessor;
import com.sweatyreptile.losergame.entities.Player;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;

public abstract class LevelScreen implements Screen{

	private static final boolean DRAW_PHYSICS = false;
	
	protected int width;
	protected int height;
	protected float viewportWidth;
	protected float viewportHeight;
	protected Camera camera;
	protected SpriteBatch spriteRenderer;
	protected Box2DDebugRenderer physRenderer;
	protected World world;
	protected LoserContactListener contactListener;
	protected EntityFactory entityFactory;
	protected Map<String, Entity<?>> entities;
	protected Player player;
	private PlayerInputProcessor playerInputProcessor;
	protected AssetManagerPlus assets;
	protected Texture background;

	
	public LevelScreen(SpriteBatch batch, AssetManagerPlus assets, PlayerInputProcessor playerInputProcessor,
			int width, int height, float viewportWidth, float viewportHeight){
		this.spriteRenderer = batch;
		this.assets = assets;
		this.playerInputProcessor = playerInputProcessor;
		this.width = width;
		this.height = height;
		this.viewportWidth = viewportWidth;
		this.viewportHeight = viewportHeight;
		this.entities = new HashMap<String, Entity<?>>();
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
		
		for (Entity<?> entity : entities.values()){
			entity.render(spriteRenderer);
		}
		
		player.render(spriteRenderer);
	
		spriteRenderer.end();
		
		if (DRAW_PHYSICS){
			physRenderer.render(world, camera.combined);
		}
	}

	public void update(float delta) {
		world.step(1/60f, 6, 2); // TODO: Change step
		
		player.update(delta);
		for (Entity<?> entity : entities.values()){
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
		
		world = new World(new Vector2(0f, -9.8f), true);
		physRenderer = new Box2DDebugRenderer();
		
		entityFactory = new EntityFactory(assets, entities,
				world, contactListener, viewportWidth, Entity.DEFAULT_SCREEN_WIDTH);
		background = assets.get("background.png");
		
		contactListener = new LoserContactListener();
		world.setContactListener(contactListener);
		
		player = createPlayer();
		if (player == null){
			throw new IllegalStateException(this + " has not created a player.");
		}
		
		playerInputProcessor.setPlayer(player);
		setupWorld();
	}
	
	protected abstract Player createPlayer(); 
	protected abstract void setupWorld();

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
		world.dispose();
	}

}