package com.sweatyreptile.losergame;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Console {

	private SpriteBatch spriteRenderer;
	private AssetManager assets;
	private Sprite bg;
	private Sprite textbg;
	private int height;
	private int width;
	private int textHeight;
	
	private boolean shown;
	
	public Console(SpriteBatch spriteRenderer, AssetManager assets, int width, int height) {
		this.spriteRenderer = new SpriteBatch();
		this.assets = assets;
		this.height = height;
		this.width = width;
		textHeight = 20;
		
	}
	
	public void init(){
		bg = new Sprite(assets.get("img/ui/console_bg.png", Texture.class));
		textbg = new Sprite(assets.get("img/ui/console_textfield.png", Texture.class));
		
		bg.setSize(width, height);
		textbg.setSize(width, textHeight);
	}
	
	public void render(){
		if (shown && bg != null && textbg != null){
			spriteRenderer.begin();
			bg.draw(spriteRenderer);
			textbg.draw(spriteRenderer);
			spriteRenderer.end();
		}
	}

	public void toggle() {
		shown = !shown;
	}
	

}
