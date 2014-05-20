package com.sweatyreptile.losergame.loaders;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class FontGroupParameters extends AssetLoaderParameters<BitmapFontGroup> {
	
	public Map<String, FreeTypeFontParameter> fontTypes = new HashMap<String, FreeTypeFontParameter>();
	
}
