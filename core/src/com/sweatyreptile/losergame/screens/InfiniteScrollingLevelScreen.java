package com.sweatyreptile.losergame.screens;

import java.util.Stack;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sweatyreptile.losergame.LevelChunk;
import com.sweatyreptile.losergame.LevelManager;
import com.sweatyreptile.losergame.PlayerInputProcessor;
import com.sweatyreptile.losergame.entities.Player;
import com.sweatyreptile.losergame.loaders.AssetManagerPlus;

public abstract class InfiniteScrollingLevelScreen extends ScrollingLevelScreen {

	private float originY;
	protected Stack<LevelChunk> chunks; //Should contain at least 2 chunks
	protected float totalChunkHeight;
	
	public InfiniteScrollingLevelScreen() {
		super();
		chunks = new Stack<LevelChunk>();
	}
	
	public InfiniteScrollingLevelScreen(LevelManager levelManager, SpriteBatch batch, AssetManagerPlus assets,
			PlayerInputProcessor playerInputProcessor, int width, int height,
			float viewportWidth, float viewportHeight, float timeLimit, String alias, String levelName, 
			boolean horizontalScrolling, boolean verticalScrolling) {
		super(levelManager, batch, assets, playerInputProcessor, width, height,
				viewportWidth, viewportHeight, timeLimit, alias, levelName, horizontalScrolling, verticalScrolling);
		chunks = new Stack<LevelChunk>();
	}
	public InfiniteScrollingLevelScreen(LevelManager levelManager, SpriteBatch batch, AssetManagerPlus assets,
			PlayerInputProcessor playerInputProcessor, int width, int height,
			float viewportWidth, float viewportHeight, String alias, String levelName, boolean horizontalScrolling,
			boolean verticalScrolling) {
		this(levelManager, batch, assets, playerInputProcessor, width, height,
				viewportWidth, viewportHeight, -1, alias, levelName, horizontalScrolling, verticalScrolling); //Use this constructor for unlimited time
	}
	
	@Override
	protected void renderBackground(float delta) {
		super.renderBackground(delta);
		for (int i = 0; i < chunks.size(); i++){
			LevelChunk chunk = chunks.get(i);
			chunk.renderBackground(delta, spriteRenderer);
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
			chunkHeight += chunk.getHeight();
		}
		for (LevelChunk chunk : chunks) chunk.update(delta);
		float cam = camera.position.y;
		if (cam - viewportHeight/2 <= originY) extendDown();
		else if (cam + viewportHeight/2 >= originY + totalChunkHeight) extendUp();

	}
	
	private void extendDown(){
		originY -= chunks.peek().getHeight();
		chunks.add(0, chunks.pop());
		updateChunkOrigins();
		chunks.firstElement().updateEntityPositions();
	}
	
	private void extendUp(){
		originY += chunks.firstElement().getHeight();
		chunks.add(chunks.firstElement());
		chunks.remove(chunks.firstElement());
		updateChunkOrigins();
		chunks.peek().updateEntityPositions();
	}
	
	private void updateChunkOrigins(){
		float chunkHeight = originY;
		for (int i = 0; i < chunks.size(); i++){
			LevelChunk chunk = chunks.get(i);
			chunk.setOriginY(chunkHeight);
			chunkHeight += chunk.getHeight();
		}
	}

	@Override
	protected void setupWorld() {
		updateChunkOrigins();
		for (LevelChunk chunk : chunks){
			totalChunkHeight += chunk.getHeight();
			chunk.updateEntityPositions();
		}
		
	}

}
