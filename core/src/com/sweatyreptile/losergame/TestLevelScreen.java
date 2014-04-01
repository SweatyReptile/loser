package com.sweatyreptile.losergame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
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
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class TestLevelScreen implements Screen {

	private Camera camera;
	private SpriteBatch spriteRenderer;
	private Box2DDebugRenderer physRenderer;
	
	private AssetManager assets;
	private Texture image;
	
	private World physWorld;
	
	public TestLevelScreen(SpriteBatch batch, AssetManager assets){
		spriteRenderer = batch;
		this.assets = assets;
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.5f, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		physWorld.step(1/60f, 6, 2); // TODO: Change step
		
		physRenderer.render(physWorld, camera.combined);
		
		spriteRenderer.begin();
		//spriteRenderer.draw(image, 0, 0);
		spriteRenderer.end();
	}

	@Override
	public void resize(int width, int height) {
		camera.position.set(new Vector3(width / 2, height / 2, 0));
		Gdx.app.log("wow", "height: " + height + ", width: " + width);
		camera.viewportHeight = 144;
		camera.viewportWidth = 256;
		spriteRenderer.setProjectionMatrix(camera.combined);
	}

	@Override
	public void show() {
		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();
		camera = new OrthographicCamera(width, height);
		resize(width, height);
		image = assets.get("badlogic.jpg", Texture.class);
		physWorld = new World(new Vector2(0, -9.8f), true);
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
		fixtureDef.restitution = 2f;
		Fixture fixture = body.createFixture(fixtureDef);
		
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

	// Don't dispose of the renderer here, so that 
	// it may be used by other Screens
	@Override
	public void dispose() {

	}

}
