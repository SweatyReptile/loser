package com.sweatyreptile.losergame.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.sweatyreptile.losergame.Player;
import com.sweatyreptile.losergame.PlayerInputProcessor;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;

public class ScrollingLevelScreen extends LevelScreen {

	public ScrollingLevelScreen(SpriteBatch batch, AssetManagerPlus assets,
			PlayerInputProcessor playerInputProcessor, int width, int height,
			float viewportWidth, float viewportHeight) {
		super(batch, assets, playerInputProcessor, width, height,
				viewportWidth, viewportHeight);
		
	}

	@Override
	protected Player createPlayer() {
		BodyDef def = new BodyDef();
		def.position.set(.5f, .5f);
		def.fixedRotation = true;
		return new Player(world, contactListener, def, assets);
	}

	@Override
	protected void setupWorld() {
		
	}

}
