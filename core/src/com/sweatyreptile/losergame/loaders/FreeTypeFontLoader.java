package com.sweatyreptile.losergame.loaders;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Array;

public class FreeTypeFontLoader extends AsynchronousAssetLoader<BitmapFontGroup, FreeTypeFontParameters> {

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
	public BitmapFontGroup loadSync(AssetManager manager, String fileName,
			FileHandle file, FreeTypeFontParameters parameter) {
		
		BitmapFontGroup fontTypes = new BitmapFontGroup();
		
		for (String fontTypeName : parameter.fontTypes.keySet()){
			FreeTypeFontParameter fontParams = parameter.fontTypes.get(fontTypeName);
			BitmapFont font = currentGenerator.generateFont(fontParams);
			fontTypes.put(fontTypeName, font);
		}
		currentGenerator.dispose();
		currentGenerator = null;
		return fontTypes;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public Array<AssetDescriptor> getDependencies(String fileName,
			FileHandle file, FreeTypeFontParameters parameter) {
		return null; // No dependencies required
	}
	
}
