package com.bullet.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.bullet.game.screens.BaseScreen;

public class BulletGame extends Game {
	
	@Override
	public void create () {
		Bullet.init();
		setScreen(new BasicCollisionDetection(this));
	}

	@Override
	public void dispose () {

	}
}
