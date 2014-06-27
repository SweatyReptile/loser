package com.sweatyreptile.losergame;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

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
	private TextArea textLog;
	
	private String textLogString;
	
	private boolean shown;
	
	public Console(SpriteBatch spriteRenderer, AssetManager assets,
			InputMultiplexer inputMultiplexer, int width, int height) {
		this.spriteRenderer = new SpriteBatch();
		this.assets = assets;
		this.inputMultiplexer = inputMultiplexer;
		this.height = height;
		this.width = width;
		textLogString = "";
		textHeight = 20;
	}
	
	public void init(){
		// Non-scene2d setup
		bg = new Sprite(assets.get("img/ui/console_bg.png", Texture.class));
		textbg = new Sprite(assets.get("img/ui/console_textfield.png", Texture.class));
		bg.setSize(width, height);
		textbg.setSize(width, textHeight);
		
		// Scene-2d setup
		stage = new Stage(new ScreenViewport(), spriteRenderer);
		
		Table table = new Table();
		table.setFillParent(true);
		
		Skin testSkin = assets.get("img/ui/skins/gdxtest/uiskin.json");
		
		textEntry = new TextField("", testSkin);
		textLog = new TextArea("", testSkin);
		
		textLog.setDisabled(true);
		
		stage.addActor(table);
		table.bottom();
		table.add(textLog).size(width, height).bottom();
		table.row();
		table.add(textEntry).size(width, textHeight).bottom();
		
		textEntry.setTextFieldListener(new ConsoleInputListener());
		
	}
	
	public void render(){
		if (shown && bg != null && textbg != null){
			spriteRenderer.begin();
			bg.draw(spriteRenderer);
			textbg.draw(spriteRenderer);
			spriteRenderer.end();
			stage.draw();
		}
	}
	
	public void processInput(String input){
		
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
	}

	public void debug(String tag, String message) {
		log(tag, message);
	}

	public void debug(String tag, String message, Exception exception) {
		log(tag, message, exception);
	}

	public void error(String tag, String message) {
		log(tag, message);
	}

	public void error(String tag, String message, Throwable exception) {
		log(tag, message, exception);
	}

	public void log(String tag, String message) {
		log(tag, message, null);
	}

	public void log(String tag, String message, Throwable exception) {
		textLogString = textLogString + "\n[" + tag + "] " + message;
		if (textLog != null){
			textLog.setText(textLogString);
		}
	}
	
	private class ConsoleInputListener implements TextFieldListener {
		@Override
		public void keyTyped(TextField textField, char c) {
			if (c == "\r".toCharArray()[0] || c == "\n".toCharArray()[0]){
				String fieldText = textField.getText();
				textField.setText("");
				LoserLog.log("Console", fieldText);
				processInput(fieldText);
			}
		}
	}
}
