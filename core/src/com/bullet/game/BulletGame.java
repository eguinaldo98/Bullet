package com.bullet.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.bullet.game.screens.BaseScreen;
import com.bullet.game.screens.BasicCollisionDetection;
import com.bullet.game.screens.RigidBodyPhysics;

public class BulletGame extends Game {
	
	@Override
	public void create () {
		Bullet.init();
		setScreen(new RigidBodyPhysics(this));
	}

	@Override
	public void dispose () {

	}
}
