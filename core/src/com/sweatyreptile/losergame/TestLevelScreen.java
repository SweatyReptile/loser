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
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class TestLevelScreen implements Screen {

	private int width;
	private int height;
	
	private Camera camera;
	private SpriteBatch spriteRenderer;
	private Box2DDebugRenderer physRenderer;
	
	private AssetManager assets;
	
	private World physWorld;
	
	public TestLevelScreen(SpriteBatch batch, AssetManager assets, int width, int height){
		spriteRenderer = batch;
		this.assets = assets;
		this.width = width;
		this.height = height;
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.5f, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		physWorld.step(1/60f, 6, 2); // TODO: Change step
		
		physRenderer.render(physWorld, camera.combined);
	}

	@Override
	public void resize(int width, int height) {
		camera.position.set(new Vector3(width / 2, height / 2, 0));
		camera.viewportHeight = height;
		camera.viewportWidth = width;
		camera.update();
		spriteRenderer.setProjectionMatrix(camera.combined);
	}

	@Override
	public void show() {
		camera = new OrthographicCamera(width, height);
		
		resize(width, height);
		
		physWorld = new World(new Vector2(0, -392f), true);
		physRenderer = new Box2DDebugRenderer();
		setupWorld();
	}
	
	private void setupWorld() {
		BodyDef bodyDef = new BodyDef();
		BodyDef groundDef = new BodyDef();
		
		bodyDef.type = BodyType.DynamicBody;
		groundDef.type = BodyType.StaticBody;
		
		bodyDef.position.set(0, 100f);
		groundDef.position.set(0, 0);
		
		Body body = physWorld.createBody(bodyDef);
		Body groundBody = physWorld.createBody(groundDef);
		
		FixtureDef fixtureDef = new FixtureDef();
		CircleShape circle = new CircleShape();
		circle.setRadius(20f);
		fixtureDef.shape = circle;
		fixtureDef.density = 2f;
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = .7f;
		body.createFixture(fixtureDef);
		
		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(camera.viewportWidth, 10f);
		groundBody.createFixture(groundBox, 0f);
		
		circle.dispose();
		groundBox.dispose();
		
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
