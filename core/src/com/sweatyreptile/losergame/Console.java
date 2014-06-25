package com.sweatyreptile.losergame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class Console {
	
	private SpriteBatch spriteRenderer;
	private AssetManager assets;
	private Sprite bg;
	private Sprite textbg;
	private int height;
	private int width;
	private int textHeight;
	
	private Stage stage;
	private InputMultiplexer inputMultiplexer;
	private TextField textEntry;
	private TextArea log;
	
	private boolean shown;
	
	public Console(SpriteBatch spriteRenderer, AssetManager assets,
			InputMultiplexer inputMultiplexer, int width, int height) {
		this.spriteRenderer = new SpriteBatch();
		this.assets = assets;
		this.inputMultiplexer = inputMultiplexer;
		this.height = height;
		this.width = width;
		textHeight = 20;
	}
	
	public void init(){
		stage = new Stage();
		
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
		if (shown){
			// Index 1 is the first position after the global inputProcessor
			inputMultiplexer.addProcessor(1, stage);
		}
		else{
			inputMultiplexer.removeProcessor(stage);
		}
		for (InputProcessor p : inputMultiplexer.getProcessors()){
			Gdx.app.log("Console", p.getClass().toString());
		}
	}
	

}
