package com.sweatyreptile.losergame;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public class Entity {

	protected Sprite sprite;
	protected Body currentBody;
	
	public Entity(World world, BodyDef bodyDef){
		this.sprite = new Sprite();
		currentBody = world.createBody(bodyDef);
	}
	
	public void render(SpriteBatch renderer){
		sprite.draw(renderer);
	}
	
	public void update(float delta){
		updateSprite(delta);
	}
	
	private void updateSprite(float delta){
		Vector2 position = currentBody.getPosition();
		sprite.setPosition(position.x, position.y);
		sprite.setRotation(MathUtils.radiansToDegrees * currentBody.getAngle());
	}
	
	public void setX(float x){
		sprite.setX(x);
	}
	
	public void setY(float y){
		sprite.setY(y);
	}
}
