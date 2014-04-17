package com.sweatyreptile.losergame.screens;

import com.badlogic.gdx.Screen;

public interface ScreenFinishedListener {

	void onFinish(FinishableScreen finished, Screen next);
	
}
