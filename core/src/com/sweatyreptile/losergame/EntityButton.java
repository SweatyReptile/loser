package com.sweatyreptile.losergame;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class EntityButton extends Button {

	private Entity<?> entity;
	
	public EntityButton(Entity<?> entity, Skin skin){
		super(skin);
		this.entity = entity;
	}


	public Entity<?> getEntity() {
		return entity;
	}
	
	
}
