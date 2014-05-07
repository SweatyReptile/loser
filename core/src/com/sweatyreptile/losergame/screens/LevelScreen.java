package com.sweatyreptile.losergame.screens;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.sweatyreptile.losergame.Entity;
import com.sweatyreptile.losergame.EntityFactory;
import com.sweatyreptile.losergame.LevelManager;
import com.sweatyreptile.losergame.LevelTimer;
import com.sweatyreptile.losergame.LoserContactListener;
import com.sweatyreptile.losergame.PlayerInputProcessor;
import com.sweatyreptile.losergame.entities.Player;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;

public abstract class LevelScreen implements Screen{

	private static final boolean DRAW_PHYSICS = false;
	protected LevelManager levelManager;
	protected String nextLevel;
	protected int width;
	protected int height;
	protected float viewportWidth;
	protected float viewportHeight;
	protected Camera camera;
	protected SpriteBatch spriteRenderer;
	protected ShapeRenderer shapeRenderer;
	protected Box2DDebugRenderer physRenderer;
	protected World world;
	protected LoserContactListener contactListener;
	protected EntityFactory entityFactory;
	protected Map<String, Entity<?>> entities;
	protected Player player;
	private PlayerInputProcessor playerInputProcessor;
	protected AssetManagerPlus assets;
	protected Texture background;
	protected BitmapFont defaultSpeechFont;
	protected LevelTimer levelTimer;
	protected float timeLimit;
	
	public static final LevelScreen newInstance(String levelType, LevelManager manager, SpriteBatch batch, AssetManagerPlus assets, PlayerInputProcessor playerInputProcessor,
			int width, int height, float viewportWidth, float viewportHeight, float timeLimit) {
		
		Class<?> screenClass = null;
		try {
			screenClass = Class.forName(levelType);
		} catch (ClassNotFoundException e) {
			Gdx.app.error("Level Instantiation", 
					"Level type " + levelType + " not found!", e);
		}
		
		LevelScreen levelScreen = null;
		try {
			levelScreen = (LevelScreen) screenClass.newInstance();
		} catch (InstantiationException e) {
			Gdx.app.error("Level Instantiation", 
					"Level type " + levelType + " could not be instantiated!");
			Gdx.app.error("Level Instantiation", 
					"This may be because it does not containt a nullary constructor, or is an abstract type", e);
		} catch (IllegalAccessException e) {
			Gdx.app.error("Level Instantiation", 
					"Nullary constructor of " + levelType + " is not public");
		}
		
		levelScreen.init(manager, batch, assets,
				playerInputProcessor, width, height,
				viewportWidth, viewportHeight, timeLimit);
		
		return null;
	}
	
	public LevelScreen(){
		
	}
	
	public LevelScreen(LevelManager manager, SpriteBatch batch, AssetManagerPlus assets, PlayerInputProcessor playerInputProcessor,
			int width, int height, float viewportWidth, float viewportHeight, float timeLimit){
		init(manager, batch, assets, playerInputProcessor, width, height, viewportWidth, viewportHeight, timeLimit);
	}
	
	public final void init(LevelManager manager, SpriteBatch batch, AssetManagerPlus assets, PlayerInputProcessor playerInputProcessor,
			int width, int height, float viewportWidth, float viewportHeight, float timeLimit){
		this.levelManager = manager;
		this.spriteRenderer = batch;
		this.assets = assets;
		this.playerInputProcessor = playerInputProcessor;
		this.width = width;
		this.height = height;
		this.viewportWidth = viewportWidth;
		this.viewportHeight = viewportHeight;
		this.timeLimit = timeLimit;
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
	
		for (Entity<?> entity : entities.values()){
			entity.renderSpeech(spriteRenderer, defaultSpeechFont);
		}
		
		player.renderSpeech(spriteRenderer, defaultSpeechFont);
		
		spriteRenderer.end();
		
		levelTimer.render(shapeRenderer);
		
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
		levelTimer.update();
	}

	@Override
	public void resize(int width, int height) {
		
	}
	
	public void setCameraPosition(float x, float y){
		camera.position.set(x, y, 0f);
		camera.update();
		spriteRenderer.setProjectionMatrix(camera.combined);
	}

	@Override
	public void show() {
		entities = new HashMap<String, Entity<?>>();
		shapeRenderer = new ShapeRenderer();
		
		camera = new OrthographicCamera(width, height);
		camera.viewportHeight = viewportHeight;
		camera.viewportWidth = viewportWidth;
		setCameraPosition(viewportWidth/ 2, viewportHeight/ 2);
		
		shapeRenderer.setProjectionMatrix(camera.combined);
		
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
		
		setupFonts();
		setupWorld();

		levelTimer = new LevelTimer(this, viewportWidth, viewportHeight, timeLimit); //timeLimit in seconds
		
		levelTimer.start();
	}
	
	protected void setupFonts() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("corbelb.ttf"));
		FreeTypeFontParameter fontParameter = new FreeTypeFontParameter();
		fontParameter.size = 18;
		defaultSpeechFont = generator.generateFont(fontParameter);
		defaultSpeechFont.setScale(.0025f);
		defaultSpeechFont.setColor(Color.BLACK);
		generator.dispose();
	}

	protected abstract Player createPlayer(); 
	protected abstract void setupWorld();
	
	protected void finish() {
		
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
		world.dispose();
	}

	public String getNextLevel() {
		return nextLevel;
	}

	public void setNextLevel(String nextLevel) {
		this.nextLevel = nextLevel;
	}

	public void setLevelManager(LevelManager levelManager) {
		this.levelManager = levelManager;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setViewportWidth(float viewportWidth) {
		this.viewportWidth = viewportWidth;
	}

	public void setViewportHeight(float viewportHeight) {
		this.viewportHeight = viewportHeight;
	}

	public void setSpriteRenderer(SpriteBatch spriteRenderer) {
		this.spriteRenderer = spriteRenderer;
	}

	public void setPlayerInputProcessor(PlayerInputProcessor playerInputProcessor) {
		this.playerInputProcessor = playerInputProcessor;
	}

	public void setAssets(AssetManagerPlus assets) {
		this.assets = assets;
	}

	public void setTimeLimit(float timeLimit) {
		this.timeLimit = timeLimit;
	}

}
