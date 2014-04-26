package com.sweatyreptile.losergame.sensors;

import java.util.Stack;

import com.badlogic.gdx.physics.box2d.Body;

public interface ContentSensorListener {
	public void bodyAdded(Stack<Body> contents);
	public void bodyRemoved(Stack<Body> contents);
}
