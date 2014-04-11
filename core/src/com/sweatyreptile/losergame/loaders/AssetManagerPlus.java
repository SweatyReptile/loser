package com.sweatyreptile.losergame.loaders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aurelienribon.bodyeditor.FixedBodyEditorLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;

/**
 * 
 * Includes physics body editor loader
 *
 */
public class AssetManagerPlus extends AssetManager {

	private List<String> loaderPaths;
	private Map<String, FixedBodyEditorLoader> bodyEditorLoaders;
	private boolean bodiesLoaded;
	
	
	public AssetManagerPlus() {
		super();
		loaderPaths = new ArrayList<String>();
		bodyEditorLoaders = new HashMap<String, FixedBodyEditorLoader>();
	}

	public AssetManagerPlus(FileHandleResolver resolver) {
		super(resolver);
		loaderPaths = new ArrayList<String>();
		bodyEditorLoaders = new HashMap<String, FixedBodyEditorLoader>();
	}
	
	public FileHandle getBodyImage(String loaderName, String bodyName) {
		return Gdx.files.internal(bodyEditorLoaders.get(loaderName).getImagePath(bodyName));
	}
	
	@Override
	public synchronized <T> void load(String fileName, Class<T> type) {
		if (type.equals(FixedBodyEditorLoader.class)) {
			loaderPaths.add(fileName);
		}
		else{
			super.load(fileName, type);
		}
	}

	@Override
	public synchronized boolean update() {
		if (!bodiesLoaded) {
			for (String loaderNameString : loaderPaths) {
				bodyEditorLoaders.put(loaderNameString,
						new FixedBodyEditorLoader(Gdx.files.internal(loaderNameString)));
			}
			bodiesLoaded = true;
		}
		return super.update();
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized <T> T get(String fileName, Class<T> type) {
		if (type.equals(FixedBodyEditorLoader.class)) {
			return (T) bodyEditorLoaders.get(fileName);
		}
		return super.get(fileName, type);
	}
	
	
	
}
