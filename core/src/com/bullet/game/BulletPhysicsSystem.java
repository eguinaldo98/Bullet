package com.bullet.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcher;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.utils.Disposable;

public class BulletPhysicsSystem implements Disposable {
    

    // adiciona os objetos de colisao com ganho de performance
    private final btDynamicsWorld dynamicsWorld;

    private final btCollisionConfiguration collisionconfig;

    private final btDispatcher dispatcher;

    private final btBroadphaseInterface broadPhase;

    private final btConstraintSolver constraintSolver;

    private final DebugDrawer debugDrawer;

    private final float fixedTimeStep = 1/60f;
    public BulletPhysicsSystem(){
        collisionconfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionconfig);
        broadPhase = new btDbvtBroadphase();
        constraintSolver = new btSequentialImpulseConstraintSolver();
        dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadPhase, constraintSolver, collisionconfig);

        debugDrawer = new DebugDrawer();
        debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_DrawWireframe);

        dynamicsWorld.setDebugDrawer(debugDrawer);

    }

    public void render(Camera camera){
        debugDrawer.begin(camera);
        dynamicsWorld.debugDrawWorld();
        debugDrawer.end();
    }

    public void update(float delta){
        dynamicsWorld.stepSimulation(delta, 1, fixedTimeStep);
    }

    public void addBody(btRigidBody body){
        dynamicsWorld.addRigidBody(body);
    }

    @Override
    public void dispose(){
        collisionconfig.dispose();
        dispatcher.dispose();
        broadPhase.dispose();
        constraintSolver.dispose();
        dynamicsWorld.dispose();
    }
}
