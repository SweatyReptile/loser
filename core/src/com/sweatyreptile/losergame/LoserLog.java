package com.sweatyreptile.losergame;

import com.badlogic.gdx.Gdx;

public class LoserLog{

	protected static Console console;
	
	public static void debug(String tag, String message) {
		Gdx.app.debug(tag, message);
		console.debug(tag, message);
	}

	public static void debug(String tag, String message, Exception exception) {
		Gdx.app.debug(tag, message, exception);
		console.debug(tag, message, exception);
	}
	
	public static void error(String tag, String message) {
		Gdx.app.error(tag, message);
		console.error(tag, message);
	}

	public static void error(String tag, String message, Throwable exception) {
		Gdx.app.error(tag, message, exception);
		console.error(tag, message, exception);
	}
	
	public static void log(String tag, String message){
		Gdx.app.log(tag, message);
		console.log(tag, message);
	}

	public static void log(String tag, String message, Throwable exception){
		Gdx.app.log(tag, message, exception);
		console.log(tag, message, exception);
	}

	public static void setLogLevel(int level) {
		Gdx.app.setLogLevel(level);
	}

	public static int getLogLevel() {
		return Gdx.app.getLogLevel();
	}
	
	

}
