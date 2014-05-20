package com.sweatyreptile.losergame.screens;

import java.util.HashMap;
import java.util.Map;

import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.sweatyreptile.losergame.Entity;
import com.sweatyreptile.losergame.EntityFactory;
import com.sweatyreptile.losergame.LevelManager;
import com.sweatyreptile.losergame.LevelTimer;
import com.sweatyreptile.losergame.LoserContactListener;
import com.sweatyreptile.losergame.PlayerInputProcessor;
import com.sweatyreptile.losergame.entities.Player;
import com.sweatyreptile.losergame.fixtures.EntityFixtureDef;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;
import com.sweatyreptile.losergame.loaders.BitmapFontGroup;

public abstract class LevelScreen implements FinishableScreen{

	private static final boolean DRAW_PHYSICS = false;
	protected final static float BORDER_WIDTH = 0.06f; //according to image size
	protected LevelManager levelManager;
	protected String alias;
	protected String levelName;
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
	protected PlayerInputProcessor playerInputProcessor;
	protected AssetManagerPlus assets;
	protected Texture background;
	float bgHeight;
	float bgWidth;
	protected BitmapFont defaultSpeechFont;
	protected LevelTimer levelTimer;
	protected float timeLimit;
	private boolean limitedTime;
	protected TweenManager tweenManager;

	public static final LevelScreen newInstance(String levelType, LevelManager manager, SpriteBatch batch, AssetManagerPlus assets, PlayerInputProcessor playerInputProcessor,
			int width, int height, float viewportWidth, float viewportHeight, float timeLimit, String alias, String levelName) {

		Class<?> screenClass = null;
		try {
			screenClass = Class.forName("com.sweatyreptile.losergame.screens." + levelType);
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
					"This may be because it is an abstract type, or does not contain a nullary constructor.", e);
		} catch (IllegalAccessException e) {
			Gdx.app.error("Level Instantiation",
					"Nullary constructor of " + levelType + " is not public");
		}

		levelScreen.init(manager, batch, assets,
				playerInputProcessor, width, height,
				viewportWidth, viewportHeight, timeLimit,
				alias, levelName);

		return levelScreen;
	}

	public LevelScreen(){

	}

	public LevelScreen(LevelManager manager, SpriteBatch batch, AssetManagerPlus assets, PlayerInputProcessor playerInputProcessor,
			int width, int height, float viewportWidth, float viewportHeight, float timeLimit, String alias, String levelName){
		init(manager, batch, assets, playerInputProcessor, width, height, viewportWidth, viewportHeight, timeLimit, alias, levelName);
	}

	public final void init(LevelManager manager, SpriteBatch batch, AssetManagerPlus assets, PlayerInputProcessor playerInputProcessor,
			int width, int height, float viewportWidth, float viewportHeight, float timeLimit, String alias, String levelName){
		this.levelManager = manager;
		this.spriteRenderer = batch;
		this.assets = assets;
		this.playerInputProcessor = playerInputProcessor;
		this.width = width;
		this.height = height;
		this.viewportWidth = viewportWidth;
		this.viewportHeight = viewportHeight;
		this.timeLimit = timeLimit;
		this.levelName = levelName;
		this.alias = alias;
	}

	@Override
	public void render(float delta) {
		update(delta);
		Gdx.gl.glClearColor(0.5f, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		spriteRenderer.begin();

		spriteRenderer.disableBlending();
		renderBackground(delta);
		spriteRenderer.enableBlending();

		renderBackground(delta);
		renderEntities(delta);
		renderPlayer(delta);

		renderSpeech(delta);

		spriteRenderer.end();

		if (limitedTime) levelTimer.render(shapeRenderer);

		if (DRAW_PHYSICS){
			physRenderer.render(world, camera.combined);
		}
	}

	protected void renderSpeech(float delta) {
		defaultSpeechFont.setScale(.0025f);
		defaultSpeechFont.setColor(Color.BLACK);
		for (Entity<?> entity : entities.values()){
			entity.renderSpeech(spriteRenderer, defaultSpeechFont);
		}

		player.renderSpeech(spriteRenderer, defaultSpeechFont);
	}

	protected void renderBackground(float delta){
		spriteRenderer.draw(background, 0f, 0f, bgWidth, bgHeight); //This background needs to be set by specific levels
	}
	protected void renderEntities(float delta) {
		for (Entity<?> entity : entities.values()){
			entity.render(spriteRenderer);
		}
	}
	protected void renderPlayer(float delta) {
		player.render(spriteRenderer);
	}

	public void update(float delta) {
		world.step(1/60f, 6, 2); // TODO: Change step

		player.update(delta);
		for (Entity<?> entity : entities.values()){
			entity.update(delta);
		}
		if (limitedTime) levelTimer.update(delta);
		tweenManager.update(delta);
	}

	@Override
	public void resize(int width, int height) {

	}

	public void setCameraPosition(float x, float y){
		camera.position.set(x, y, 0f);
		camera.update();
		spriteRenderer.setProjectionMatrix(camera.combined);
	}

	public Vector3 getCameraPosition(){
		return camera.position;
	}

	@Override
	public void show() {
		tweenManager = new TweenManager();

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

		background = assets.get("background_extended.png");
		bgHeight = ((float) background.getHeight() / height) * viewportHeight;
		bgWidth = ((float) background.getWidth() / width) * viewportWidth;

		contactListener = new LoserContactListener();
		world.setContactListener(contactListener);

		player = createPlayer();
		if (player == null){
			throw new IllegalStateException(this + " has not created a player.");
		}

		playerInputProcessor.setPlayer(player);

		setupFonts();
		setupWorld();

		if (timeLimit >= 0){
			limitedTime = true;
			levelTimer = new LevelTimer(this, viewportWidth, viewportHeight, timeLimit); //timeLimit in seconds
		}
		if (limitedTime) levelTimer.start();
	}

	@Override
	public void dispose() {
		//world.dispose();
		shapeRenderer.dispose();
		physRenderer.dispose();
	}

	protected void setupFonts() {
		BitmapFontGroup corbel = assets.get("corbelb.ttf"); 
		defaultSpeechFont = corbel.get("speech");
	}

	protected void setupBorders(boolean horizontal, boolean vertical){
		setupBorders(horizontal, horizontal, vertical, vertical);
	}

	protected void setupBorders(boolean horizontalTop, boolean horizontalBottom,
			boolean verticalLeft, boolean verticalRight){
		if (horizontalTop){
			entityFactory.create("horizontal_border", BodyType.StaticBody, 0f, viewportHeight, new EntityFixtureDef(assets, "horizontal_border"), false);
		}
		if (horizontalBottom){
			entityFactory.create("horizontal_border_2", BodyType.StaticBody, 0f, -BORDER_WIDTH, new EntityFixtureDef(assets, "horizontal_border"), false);
		}
		if (verticalLeft){
			entityFactory.create("vertical_border", BodyType.StaticBody, -BORDER_WIDTH, 0f, new EntityFixtureDef(assets, "vertical_border"), false);
		}
		if (verticalRight){
			entityFactory.create("vertical_border_2", BodyType.StaticBody, viewportWidth, 0f, new EntityFixtureDef(assets, "vertical_border"), false);
		}
	}

	protected abstract Player createPlayer();
	protected abstract void setupWorld();

	public void finish() {
		levelManager.level(alias);
	}

	@Override
	public void hide() {
		playerInputProcessor.clearPlayer();
    dispose();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	public String getNextLevel() {
		return nextLevel;
	}

	public void setNextLevel(String nextLevel) {
		this.nextLevel = nextLevel;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
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
