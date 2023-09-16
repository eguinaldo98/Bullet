package com.bullet.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class BasicCollisionDetection extends BaseScreen {
    btCollisionConfiguration collisionConfig;
    btDispatcher dispatcher;

    Array<GameObject> gameObjects;
    GameObject floorObject;

    static class GameObject extends ModelInstance implements Disposable {
        public final btCollisionObject body;
        public boolean hasCollided = false;

        public GameObject(Model model, btCollisionShape shape) {
            super(model);
            body = new btCollisionObject();
            body.setCollisionShape(shape);
        }

        @Override
        public void dispose() {
            body.dispose();
        }
    }

    public BasicCollisionDetection(Game game) {
        super(game);
        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
        gameObjects = new Array<>();

        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        Material material = new Material();
        material.set(ColorAttribute.createDiffuse(getRandomColor()));
        MeshPartBuilder builder = modelBuilder.part("box", GL20.GL_TRIANGLES,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, material);

        BoxShapeBuilder.build(builder, 0, 0, 0, 1f, 1f, 1f);
        Model model = modelBuilder.end();

        btBoxShape boxShape = new btBoxShape(new Vector3(0.5f, 0.5f, 0.5f));
        GameObject gameObject = new GameObject(model, boxShape);

        renderInstances.add(gameObject);
        gameObjects.add(gameObject);

        gameObject.transform.trn(0,10,0);
        gameObject.body.setWorldTransform(gameObject.transform);
        
        createFloor();
    }

    @Override
    public void render(float delta){
        for (GameObject gameObject : gameObjects){
            gameObject.transform.trn(0, -2f * delta, 0f);
            gameObject.body.setWorldTransform(gameObject.transform);
        }
        super.render(delta);
    }

    private void createFloor() {
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        MeshPartBuilder meshBuilder = modelBuilder.part("floor", GL20.GL_TRIANGLES,
                VertexAttribute.Position().usage | VertexAttribute.Normal().usage | VertexAttribute.TexCoords(0).usage,
                new Material());

        BoxShapeBuilder.build(meshBuilder, 20, 1, 20);
        Model floor = modelBuilder.end();
        btBoxShape btBoxShape = new btBoxShape(new Vector3(10f, 0.5f, 10f));
        floorObject = new GameObject(floor, btBoxShape);

        floorObject.transform.trn(0f, -0.5f, 0f);
        // sempre que alterar o modelo deve alterar a posição do body para colisoes
        floorObject.body.setWorldTransform(floorObject.transform);

        renderInstances.add(floorObject);
    }
}
