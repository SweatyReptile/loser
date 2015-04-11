package com.sweatyreptile.losergame.screens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
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
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sweatyreptile.losergame.Entity;
import com.sweatyreptile.losergame.EntityButton;
import com.sweatyreptile.losergame.EntityData;
import com.sweatyreptile.losergame.EntityFactory;
import com.sweatyreptile.losergame.LevelData;
import com.sweatyreptile.losergame.LevelManager;
import com.sweatyreptile.losergame.LevelTimer;
import com.sweatyreptile.losergame.LoserContactListener;
import com.sweatyreptile.losergame.LoserLog;
import com.sweatyreptile.losergame.PlayerInputProcessor;
import com.sweatyreptile.losergame.entities.Player;
import com.sweatyreptile.losergame.fixtures.EntityFixtureDef;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;
import com.sweatyreptile.losergame.loaders.BitmapFontGroup;

public abstract class LevelScreen implements FinishableScreen{

	private static final boolean DRAW_PHYSICS = true;
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
	protected List<EntityData> entityData;
	protected Map<String, Entity<?>> entities;
	protected Player player;
	protected PlayerInputProcessor playerInputProcessor;
	protected AssetManagerPlus assets;
	protected Texture background;
	protected float bgHeight;
	protected float bgWidth;
	protected String bgname;
	protected BitmapFont defaultSpeechFont;
	protected LevelTimer levelTimer;
	protected float timeLimit;
	private boolean limitedTime;
	
	protected CameraScroller scroller;
	protected TweenManager tweenManager;
	
	private boolean editMode;
	private Body selectedBody;
	private Camera editCamera;
	private Stage editStage;
	private Map<String, EntityButton> editButtons;

	public static final LevelScreen newInstance(String levelType, LevelManager manager, SpriteBatch batch, AssetManagerPlus assets, PlayerInputProcessor playerInputProcessor,
			int width, int height, float viewportWidth, float viewportHeight, float timeLimit, String alias, String levelName) {

		Class<?> screenClass = null;
		try {
			screenClass = Class.forName("com.sweatyreptile.losergame.screens." + levelType);
		} catch (ClassNotFoundException e) {
			LoserLog.error("Level Instantiation",
					"Level type " + levelType + " not found!", e);
		}

		LevelScreen levelScreen = null;
		try {
			levelScreen = (LevelScreen) screenClass.newInstance();
		} catch (InstantiationException e) {
			LoserLog.error("Level Instantiation",
					"Level type " + levelType + " could not be instantiated!");
			LoserLog.error("Level Instantiation",
					"This may be because it is an abstract type, or does not contain a nullary constructor.", e);
		} catch (IllegalAccessException e) {
			LoserLog.error("Level Instantiation",
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
		entityData = new ArrayList<EntityData>();
	}

	@Override
	public void render(float delta) {
		update(delta);
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		spriteRenderer.setProjectionMatrix(camera.combined);
		
		spriteRenderer.begin();

		spriteRenderer.disableBlending();
		renderBackground(delta);
		spriteRenderer.enableBlending();

		renderEntities(delta);
		renderPlayer(delta);

		renderSpeech(delta);

		spriteRenderer.end();

		if (limitedTime) levelTimer.render(shapeRenderer);

		if (DRAW_PHYSICS){
			physRenderer.render(world, camera.combined);
		}
		
		if (editMode){
			spriteRenderer.setProjectionMatrix(editCamera.combined);
			if (tweenManager.getRunningTweensCount() == 0){
				editStage.draw(); // Only draw if not moving something (like the camera) 
			}
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
		if (background != null){
			spriteRenderer.draw(background, 0f, 0f, bgWidth, bgHeight); //This background needs to be set by specific levels
		}
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
		if (limitedTime && !editMode) levelTimer.update(delta);
		tweenManager.update(delta);
		
		if (editMode){
			editStage.act(delta);
		}
		if (!editMode){
			scroller.update(delta);
		}
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
		LoserLog.log(this.getClass().getSimpleName(), "Edit mode: " + editMode);
		
		tweenManager = new TweenManager();

		editButtons = new HashMap<String, EntityButton>();
		entities = new HashMap<String, Entity<?>>();
		shapeRenderer = new ShapeRenderer();

		camera = new OrthographicCamera(width, height);
		camera.viewportHeight = viewportHeight;
		camera.viewportWidth = viewportWidth;
		setCameraPosition(viewportWidth/ 2, viewportHeight/ 2);

		shapeRenderer.setProjectionMatrix(camera.combined);

		world = new World(new Vector2(0f, getGravity()), true);
		physRenderer = new Box2DDebugRenderer();
		physRenderer.setDrawAABBs(true);

		entityFactory = new EntityFactory(assets, entities,
				world, contactListener, viewportWidth, Entity.DEFAULT_SCREEN_WIDTH);

		contactListener = new LoserContactListener();
		world.setContactListener(contactListener);

		player = createPlayer();
		if (player == null){
			throw new IllegalStateException(this + " has not created a player.");
		}
		
		scroller = new CameraScroller(this, camera, player, tweenManager);

		setupFonts();
		for (EntityData edata : entityData){
			entityFactory.create(assets, edata);
		}
		setupWorld();
		
		if (editMode){
			setupEditMode();
		}
		
		playerInputProcessor.setPlayer(getPlayerForInput());

		if (timeLimit >= 0){
			limitedTime = true;
			levelTimer = new LevelTimer(levelManager, viewportWidth, viewportHeight, timeLimit); //timeLimit in seconds
		}
		if (limitedTime) levelTimer.start();
	}
	
	private void setupEditMode(){
		editCamera = new OrthographicCamera();
		Viewport editViewport = new StretchViewport(width, height, editCamera);
		editViewport.update(width, height, true);
		editCamera.update();
		
		editStage = new Stage(editViewport, spriteRenderer);
		for (String entityName : entities.keySet()){
			
			Entity<?> entity = entities.get(entityName);
			createEditButton(entity);
		}
	}

	private void createEditButton(Entity<?> entity) {
		entity.setFixedRotation(true);
		
		Skin skin = assets.get("img/ui/skins/gdxtest/uiskin.json");
		final EntityButton button = new EntityButton(entity, skin);
		editButtons.put(entity.getName(), button);
		
		Vector3 screencoords = camera.project(new Vector3(entity.getX(), entity.getY(), 0));
		Vector3 widthheight = camera.project(new Vector3(entity.getWidth(), entity.getHeight(), 0));
		
		button.setPosition(screencoords.x, screencoords.y);
		//button.setSize(widthheight.x, widthheight.y);
		
		editStage.addActor(button);
		
		button.addListener(new ClickListener(){

			@Override
			public void touchDragged(InputEvent event, float x, float y,
					int pointer) {
				Entity<?> entity = button.getEntity();
				buttonEditorPosition.x = Gdx.input.getX();
				buttonEditorPosition.y = Gdx.input.getY();
				buttonEditorPosition = camera.unproject(buttonEditorPosition);
				entity.setPosition(buttonEditorPosition.x, buttonEditorPosition.y);
				super.touchDragged(event, x, y, pointer);
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button2) {
				updateEditButton(button);
				Entity<?> entity = button.getEntity();
				LoserLog.log("Editor", 
						entity.getName() + 
						" location: (" + entity.getX() + ", " + entity.getY() + ")");
				super.touchUp(event, x, y, pointer, button2);
			}
			
			
			
		});
	}

	private Vector3 buttonEditorPosition = new Vector3();
	
	private Player getPlayerForInput() {
		if (editMode){
			return null;
		}
		else{
			return player;
		}
	}
	
	public float getGravity(){
		if (editMode){
			return 0f;
		}
		else{
			return -9.8f;
		}
		
	}
	
	public void setBackground(String bgname) {
		this.bgname = bgname;
		background = assets.get(bgname);
		bgHeight = ((float) background.getHeight() / height) * viewportHeight;
		bgWidth = ((float) background.getWidth() / width) * viewportWidth;
	}

	@Override
	public void dispose() {
		//world.dispose();
		shapeRenderer.dispose();
		//physRenderer.dispose();
	}

	protected void setupFonts() {
		BitmapFontGroup corbel = assets.get("fonts/corbelb.ttf"); 
		defaultSpeechFont = corbel.get("speech");
	}

	protected void setupBorders(boolean horizontal, boolean vertical){
		setupBorders(horizontal, horizontal, vertical, vertical);
	}

	protected void setupBorders(boolean horizontalTop, boolean horizontalBottom,
			boolean verticalLeft, boolean verticalRight){
		if (horizontalTop){
			entityFactory.create("horizontal_border", 
					BodyType.StaticBody, 0f, viewportHeight,
					new EntityFixtureDef(assets, "horizontal_border"), false)
					.setSpecial(true);
		}
		if (horizontalBottom){
			entityFactory.create("horizontal_border_2",
					BodyType.StaticBody, 0f, -BORDER_WIDTH,
					new EntityFixtureDef(assets, "horizontal_border"), false)
					.setSpecial(true);
		}
		if (verticalLeft){
			entityFactory.create("vertical_border",
					BodyType.StaticBody, -BORDER_WIDTH, 0f,
					new EntityFixtureDef(assets, "vertical_border"), false)
					.setSpecial(true);
		}
		if (verticalRight){
			entityFactory.create("vertical_border_2",
					BodyType.StaticBody, viewportWidth, 0f,
					new EntityFixtureDef(assets, "vertical_border"), false)
					.setSpecial(true);
		}
	}

	protected Player createPlayer(){
		BodyDef def = new BodyDef();
		def.position.set(viewportWidth / 2, viewportHeight / 2);
		def.fixedRotation = true;
		def.type = BodyType.DynamicBody;
		return new Player(world, contactListener, def, assets);
	}
	
	protected abstract void setupWorld();

	public void finish() {
		// Do things that should be done after the 
		// player accomplishes whatever needs to
		// be accomplished in order to complete
		// the level
		levelManager.nextlvl();
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
	
	public LevelData getLevelData() {
		int numNonSpecial = 0;
		for (String entityName : entities.keySet()){
			if (!entities.get(entityName).isSpecial()){
				numNonSpecial++;
			}
		}
		EntityData[] entityData = new EntityData[numNonSpecial];
		int i = 0;
		for (String name : entities.keySet()){
			Entity<?> entity = entities.get(name);
			if (!entity.isSpecial()){
				entityData[i] = entities.get(name).getEntityData();
				i++;
			}
		}
		return new LevelData(alias, getClass().getSimpleName(), levelName, viewportWidth, viewportHeight, timeLimit, bgname, entityData);
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

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	public InputProcessor getEditInputProcessor() {
		return editStage;
	}

	public Map<String, Entity<?>> getEntities() {
		return entities;
	}
	
	public void addEntity(EntityData entityData){
		this.entityData.add(entityData);
	}
	
	public void addEntity(String name, BodyType bodyType){
		Entity<?> entity = entityFactory.create(assets, name, bodyType);
		createEditButton(entity);
	}

	public void removeEntity(String name) {
		entities.get(name).destroy();
		entities.remove(name);
		removeButton(name);
	}

	private void removeButton(String name) {
		EntityButton button = editButtons.get(name);
		button.remove();
		editButtons.remove(name);
	}

	public CameraScroller getCameraScroller() {
		return scroller;
	}

	public Camera getCamera() {
		return camera;
	}

	public TweenManager getTweenManager() {
		return tweenManager;
	}

	public void updateEditButtons() {
		for (String name : editButtons.keySet()){
			EntityButton button = editButtons.get(name);
			Entity<?> entity = button.getEntity();
			Vector3 entityPos = new Vector3(entity.getX(), entity.getY(), 0);
			Vector3 buttonPos = camera.project(entityPos);
			updateEditButton(button, buttonPos.x, buttonPos.y);
		}
	}
	
	public void updateEditButton(EntityButton button, float x, float y){
		button.setPosition(
				 x - button.getWidth()/2,
				 y - button.getHeight()/2);
	}
	
	public void updateEditButton(EntityButton button){
		updateEditButton(button, Gdx.input.getX(), height - Gdx.input.getY());
	}

}
