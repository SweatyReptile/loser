package com.sweatyreptile.losergame;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class EntityButton extends Button {

	private Entity<?> entity;
	private Camera levelCamera;
	Vector3 reusableVector = new Vector3();
	
	public EntityButton(Entity<?> entity, Camera levelCamera){
		this.entity = entity;
		this.levelCamera = levelCamera;
	}
	
	public EntityButton(Entity<?> entity, Camera levelCamera, Skin skin){
		super(skin);
		this.entity = entity;
		this.levelCamera = levelCamera;
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		reusableVector.x = entity.getX();
		reusableVector.y = entity.getY();
		reusableVector = levelCamera.project(reusableVector);
		setPosition(reusableVector.x, reusableVector.y);
	}

	public Entity<?> getEntity() {
		return entity;
	}
	
	
}
