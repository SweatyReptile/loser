package com.sweatyreptile.losergame;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

	private LevelManager levelManager;
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
		
		// The first argument is treated as the command name,
		// and will always be left as a string.
		List<String> fullargs = Arrays.asList(input.split(" "));
		String command = fullargs.get(0);
		
		List<String> cmdstringargs = fullargs.subList(1, fullargs.size());
		
		
		// The rest of the arguments should be tested to see
		// if they should be treated as int or float or boolean
		List<Class<?>> argclasses = new ArrayList<Class<?>>();
		List<Object> cmdobjargs = new ArrayList<Object>();
		for (String arg : cmdstringargs){
			try{
				Integer argInt = Integer.parseInt(arg);
				argclasses.add(int.class);
				cmdobjargs.add(argInt);
			}
			catch(NumberFormatException e){
				try{
					Float argFloat = Float.parseFloat(arg);
					argclasses.add(float.class);
					cmdobjargs.add(argFloat);
				}
				catch(NumberFormatException ex){
					if("true".equals(arg)){
						argclasses.add(boolean.class);
						cmdobjargs.add(new Boolean(true));
					}
					else if ("false".equals(arg)){
						argclasses.add(boolean.class);
						cmdobjargs.add(new Boolean(false));
					}
					else{
						argclasses.add(String.class);
						cmdobjargs.add(arg);
					}
				}
			}
		}
		
		// Finally, find a method that matches 
		// the command name and arg types,
		// then call it with the args
		
		// For now, we will only search levelManager.
		Class[] parameterTypes = Arrays.copyOf(argclasses.toArray(), argclasses.size(), Class[].class);
		Object[] parameters = cmdobjargs.toArray();
		Method method = null;
		
		try {
			method = LevelManager.class.getMethod(command, parameterTypes);
			
			try {
				method.invoke(levelManager, parameters);
			} catch (IllegalAccessException e) {
				LoserLog.error("Console", "Unable to access command [" + command + " (args: " + argclasses + ")]."
						+ "Perhaps it was declared with the wrong access modifier?");
			} catch (IllegalArgumentException e) {
				LoserLog.error("Console", "This shouldn't happen.", e);
			} catch (InvocationTargetException e) {
				LoserLog.error("Console", "Something went wrong with the command ["
						+ command + " (args: " + argclasses + ")].", e);
			}
			
		} catch (NoSuchMethodException e) {
			LoserLog.error("Console", "The command [" + command + " (args: " + argclasses + ")] does not exist.");
		} catch (SecurityException e) {
			LoserLog.error("Console", "Unable to access command [" + command + " (args: " + argclasses + ")]."
					+ "Perhaps it was declared with the wrong access modifier?");
		}
		
	}

	public void toggle() {
		shown = !shown;
		if (shown){
			// Index 1 is the first position after the global inputProcessor
			inputMultiplexer.addProcessor(1, stage);
			stage.setKeyboardFocus(textEntry);
		}
		else{
			inputMultiplexer.removeProcessor(stage);
		}
	}

	public void toggle(boolean on) {
		shown = on;
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
			textLog.setCursorPosition(textLog.getText().length());
		}
	}
	
	private class ConsoleInputListener implements TextFieldListener {
		@Override
		public void keyTyped(TextField textField, char c) {
			if (c == '\r' || c == '\n'){
				String fieldText = textField.getText();
				textField.setText("");
				LoserLog.log("Console", fieldText);
				processInput(fieldText);
			}
			else if (c == '`'){
				String fieldText = textField.getText();
				textField.setText(fieldText.replace("`", ""));
			}
		}
	}
	
	public void setLevelManager(LevelManager levelManager){
		this.levelManager = levelManager;
	}

	public boolean isShown() {
		return shown;
	}
}
