package com.sweatyreptile.losergame.loaders;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Array;

public class FreeTypeFontLoader extends AsynchronousAssetLoader<BitmapFont, FreeTypeFontParameters> {

	private FreeTypeFontGenerator currentGenerator;
	
	public FreeTypeFontLoader(FileHandleResolver resolver) {
		super(resolver);
	}

	@Override
	public void loadAsync(AssetManager manager, String fileName,
			FileHandle file, FreeTypeFontParameters parameter) {
		currentGenerator = new FreeTypeFontGenerator(file);
	}

	@Override
	public BitmapFont loadSync(AssetManager manager, String fileName,
			FileHandle file, FreeTypeFontParameters parameter) {
		BitmapFont font = currentGenerator.generateFont(parameter.parameters);
		currentGenerator.dispose();
		currentGenerator = null;
		return font;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public Array<AssetDescriptor> getDependencies(String fileName,
			FileHandle file, FreeTypeFontParameters parameter) {
		return null; // No dependencies required
	}
	
}
