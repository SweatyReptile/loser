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
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.sweatyreptile.losergame.Entity;
import com.sweatyreptile.losergame.EntityFactory;
import com.sweatyreptile.losergame.LevelTimer;
import com.sweatyreptile.losergame.LoserContactListener;
import com.sweatyreptile.losergame.PlayerInputProcessor;
import com.sweatyreptile.losergame.entities.Player;
import com.sweatyreptile.losergame.fixtures.EntityFixtureDef;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;

public abstract class LevelScreen extends FinishableScreen{

	private static final boolean DRAW_PHYSICS = true;
	
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
	private LevelTimer levelTimer;
	private boolean limitedTime;

	
	public LevelScreen(ScreenFinishedListener listener, Screen nextScreen, SpriteBatch batch, AssetManagerPlus assets, PlayerInputProcessor playerInputProcessor,
			int width, int height, float viewportWidth, float viewportHeight, float timeLimit){
		super(listener, nextScreen);
		this.spriteRenderer = batch;
		this.assets = assets;
		this.playerInputProcessor = playerInputProcessor;
		this.width = width;
		this.height = height;
		this.viewportWidth = viewportWidth;
		this.viewportHeight = viewportHeight;
		this.entities = new HashMap<String, Entity<?>>();
		shapeRenderer = new ShapeRenderer();
		if (timeLimit >= 0){
			limitedTime = true;
			levelTimer = new LevelTimer(this, viewportWidth, viewportHeight, timeLimit); //timeLimit in seconds
		}
	}

	@Override
	public void render(float delta) {
		update(delta);
		Gdx.gl.glClearColor(0.5f, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		noClearRender(delta);
	}
	
	public void noClearRender(float delta){
		update(delta);
		spriteRenderer.begin();
		
		spriteRenderer.disableBlending();		
		//spriteRenderer.draw(background, 0f, 0f, viewportWidth, viewportHeight); //This background needs to be set by specific levels
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
		
		if (limitedTime) levelTimer.render(shapeRenderer);
		
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
		if (limitedTime) levelTimer.update();
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

		if (limitedTime) levelTimer.start();
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
	
	protected void setupBorders(boolean horizontal, boolean vertical){
		setupBorders(horizontal, horizontal, vertical, vertical);
	}
	
	protected void setupBorders(boolean horizontalTop, boolean horizontalBottom, 
			boolean verticalLeft, boolean verticalRight){ //0.06 is the width of borders
		if (horizontalTop){
			entityFactory.create("horizontal_border", BodyType.StaticBody, 0f, viewportHeight, new EntityFixtureDef(assets, "horizontal_border"), false);
		}
		if (horizontalBottom){
			entityFactory.create("horizontal_border_2", BodyType.StaticBody, 0f, -0.06f, new EntityFixtureDef(assets, "horizontal_border"), false);
		}
		if (verticalLeft){
			entityFactory.create("vertical_border", BodyType.StaticBody, -0.06f, 0f, new EntityFixtureDef(assets, "vertical_border"), false);
		}
		if (verticalRight){
			entityFactory.create("vertical_border_2", BodyType.StaticBody, viewportWidth, 0f, new EntityFixtureDef(assets, "vertical_border"), false);
		}
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
	
	public void finish(){
		super.finish();
	}

}
