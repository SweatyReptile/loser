package com.sweatyreptile.losergame.screens;

import java.util.Stack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sweatyreptile.losergame.LevelChunk;
import com.sweatyreptile.losergame.PlayerInputProcessor;
import com.sweatyreptile.losergame.entities.Player;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;

public abstract class InfiniteScrollingLevelScreen extends ScrollingLevelScreen {

	private float originY;
	protected Stack<LevelChunk> chunks; //Should contain at least 2 chunks
	protected float totalChunkHeight;
	
	public InfiniteScrollingLevelScreen(ScreenFinishedListener listener,
			Screen nextScreen, SpriteBatch batch, AssetManagerPlus assets,
			PlayerInputProcessor playerInputProcessor, int width, int height,
			float viewportWidth, float viewportHeight, float timeLimit,
			boolean horizontalScrolling, boolean verticalScrolling) {
		super(listener, nextScreen, batch, assets, playerInputProcessor, width, height,
				viewportWidth, viewportHeight, timeLimit, horizontalScrolling, verticalScrolling);
		chunks = new Stack<LevelChunk>();
	}
	public InfiniteScrollingLevelScreen(ScreenFinishedListener listener,
			Screen nextScreen, SpriteBatch batch, AssetManagerPlus assets,
			PlayerInputProcessor playerInputProcessor, int width, int height,
			float viewportWidth, float viewportHeight, boolean horizontalScrolling,
			boolean verticalScrolling) {
		this(listener, nextScreen, batch, assets, playerInputProcessor, width, height,
				viewportWidth, viewportHeight, -1, horizontalScrolling, verticalScrolling); //Use this constructor for unlimited time
	}
	
	@Override
	protected void renderBackground(float delta) {
		super.renderBackground(delta);
		for (int i = 0; i < chunks.size(); i++){
			LevelChunk chunk = chunks.get(i);
			chunk.renderBackground(delta, spriteRenderer, viewportWidth, viewportHeight);
		}
	}
	
	@Override
	protected void renderEntities(float delta) {
		super.renderEntities(delta);
		for (int i = 0; i < chunks.size(); i++){
			chunks.get(i).renderEntities(delta, spriteRenderer);
		}
	}
	
	@Override
	public void update(float delta){
		super.update(delta);
		float chunkHeight = originY;
		for (int i = 0; i < chunks.size(); i++){
			LevelChunk chunk = chunks.get(i);
			chunk.setOriginY(chunkHeight);
			chunkHeight += chunk.getHeight(height, viewportHeight);
		}
		for (LevelChunk chunk : chunks) chunk.update(delta);
		float cam = camera.position.y;
		if (cam - viewportHeight/2 <= originY) extendDown();
		else if (cam + viewportHeight/2 >= originY + totalChunkHeight) extendUp();

	}
	
	private void extendDown(){
		originY -= chunks.peek().getHeight(height, viewportHeight);
		chunks.add(0, chunks.pop());
		updateChunkOrigins();
		chunks.firstElement().updateEntityPositions(viewportHeight);
	}
	
	private void extendUp(){
		originY += chunks.firstElement().getHeight(height, viewportHeight);
		chunks.add(chunks.firstElement());
		chunks.remove(chunks.firstElement());
		updateChunkOrigins();
		chunks.peek().updateEntityPositions(viewportHeight);
	}
	
	private void updateChunkOrigins(){
		float chunkHeight = originY;
		for (int i = 0; i < chunks.size(); i++){
			LevelChunk chunk = chunks.get(i);
			chunk.setOriginY(chunkHeight);
			chunkHeight += chunk.getHeight(height, viewportHeight);
		}
	}

	@Override
	protected Player createPlayer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setupWorld() {
		updateChunkOrigins();
		for (LevelChunk chunk : chunks){
			totalChunkHeight += chunk.getHeight(height, viewportHeight);
			chunk.updateEntityPositions(viewportHeight);
		}
		
	}

}
