package com.bullet.game.screens;

import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcher;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.utils.Disposable;

public class BulletPhysicsSystem implements Disposable {
    

    // adiciona os objetos de colisao com ganho de performance
    private final btDynamicsWorld dynamicsWorld;

    private final btCollisionConfiguration collisionconfig;

    private final btDispatcher dispatcher;

    private final btBroadphaseInterface broadPhase;

    private final btConstraintSolver constraintSolver;

    public BulletPhysicsSystem(){
        collisionconfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionconfig);
        broadPhase = new btDbvtBroadphase();
        constraintSolver = new btSequentialImpulseConstraintSolver();
        dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadPhase, constraintSolver, collisionconfig);

    }

    @Override
    public void dispose(){

    }
}
