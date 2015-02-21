package com.conways.game;

import com.badlogic.gdx.Game;

public class ConwaysGame extends Game {

	@Override
	public void create() {
		setScreen(new ConwaysGameWorld(100, 100));
	}
}
