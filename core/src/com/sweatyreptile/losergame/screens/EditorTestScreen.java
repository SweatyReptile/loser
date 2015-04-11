package com.sweatyreptile.losergame.screens;

import com.badlogic.gdx.math.MathUtils;
import com.sweatyreptile.losergame.Entity;
import com.sweatyreptile.losergame.LoserLog;

public class EditorTestScreen extends LevelScreen {
	
	private Entity<?> redbook;
	
	@Override
	public void update(float delta) {
		super.update(delta);
		float r = redbook.getRotation();
		r = r + MathUtils.PI / 32f;
		redbook.setRotation(r);
		LoserLog.log("EditorTestScreen", "Setting rotation: " + r);
	}

	@Override
	protected void setupWorld() {
		redbook = entities.get("book_red");
	}
}
