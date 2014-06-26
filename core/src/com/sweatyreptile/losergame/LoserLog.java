package com.sweatyreptile.losergame;

import com.badlogic.gdx.Gdx;

public class LoserLog{

	private Console console;
	
	public LoserLog(Console console){
		this.console = console;
	}
	
	public void debug(String tag, String message) {
		Gdx.app.debug(tag, message);
		console.debug(tag, message);
	}

	public void debug(String tag, String message, Exception exception) {
		Gdx.app.debug(tag, message, exception);
		console.debug(tag, message, exception);
	}
	
	public void error(String tag, String message) {
		Gdx.app.error(tag, message);
		console.error(tag, message);
	}

	public void error(String tag, String message, Throwable exception) {
		Gdx.app.error(tag, message, exception);
		console.error(tag, message, exception);
	}

	public void setLogLevel(int level) {
		Gdx.app.setLogLevel(level);
	}

	public int getLogLevel() {
		return Gdx.app.getLogLevel();
	}
	
	

}
