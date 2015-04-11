package com.sweatyreptile.losergame.fixtures;

import aurelienribon.bodyeditor.FixedBodyEditorLoader;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;

public class EntityFixtureDef extends FixtureDef {

	private transient AssetManagerPlus assets;
	private transient FixedBodyEditorLoader loader;
	private transient String loaderName = "duck.json";
	private String name;
	
	public EntityFixtureDef(){
		density = 0.0001f;
		friction = 0.4f;
		restitution = 0f;
	}
	
	public EntityFixtureDef(String name){
		this();
		this.name = name;
	}
	
	public EntityFixtureDef(AssetManagerPlus assets, String name) {
		this(name);
		this.assets = assets;
		this.loader = assets.get(loaderName, FixedBodyEditorLoader.class);
	}
	
	public void attach(Body body, float scale, boolean flipped) {
		loader.attachFixture(body, name, this, scale, flipped);
	}

	public String getLoaderName() {
		return loaderName;
	}

	public String getName() {
		return name;
	}
	
	public Texture getTexture() {
		return assets.get(loader.getImagePath(name), Texture.class);
	}
	
	
	
}
