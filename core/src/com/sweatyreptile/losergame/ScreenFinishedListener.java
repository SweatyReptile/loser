package com.sweatyreptile.losergame;

import com.badlogic.gdx.Screen;

public interface ScreenFinishedListener {

	void onFinish(FinishableScreen finished, Screen next);
	
}
