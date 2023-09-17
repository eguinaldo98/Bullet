package com.bullet.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.ConeShapeBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.CylinderShapeBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.SphereShapeBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Quaternion;
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

        for (int i = -6; i < 6; i += 2) {
            for (int j = -6; j < 6; j += 2) {
                ModelBuilder modelBuilder = new ModelBuilder();
                modelBuilder.begin();
                Material material = new Material();
                material.set(ColorAttribute.createDiffuse(getRandomColor()));
                MeshPartBuilder builder = modelBuilder.part("box", GL20.GL_TRIANGLES,
                        VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, material);

                btCollisionShape shape;

                int random = MathUtils.random(1, 4);
                switch (random) {
                    case 1:
                        BoxShapeBuilder.build(builder, 0, 0, 0, 1f, 1f, 1f);
                        shape = new btBoxShape(new Vector3(0.5f, 0.5f, 0.5f));
                        break;
                    case 2:
                        ConeShapeBuilder.build(builder, 1, 1, 1, 8);
                        shape = new btConeShape(0.5f, 1f);
                        break;
                    case 3:
                        SphereShapeBuilder.build(builder, 1, 1, 1, 8, 8);
                        shape = new btSphereShape(0.5f);
                        break;
                    case 4:
                    default:
                        CylinderShapeBuilder.build(builder, 1, 1, 1, 8);
                        shape = new btCylinderShape(new Vector3(0.5f, 0.5f, 0.5f));
                        break;
                }

                GameObject box = new GameObject(modelBuilder.end(), shape);
                box.transform.setToTranslation(i, MathUtils.random(10, 20), j);
                box.transform.rotate(new Quaternion(Vector3.Z, MathUtils.random(0f, 270f)));
                box.body.setWorldTransform(box.transform);

                renderInstances.add(box);
                gameObjects.add(box);
            }
        }
        createFloor();
    }

    @Override
    public void render(float delta) {
        for (GameObject gameObject : gameObjects) {

            if (checkCollision(gameObject.body, floorObject.body)) {
                if (gameObject.hasCollided)
                    continue;

                gameObject.materials.get(0).set(ColorAttribute.createDiffuse(Color.RED));
                gameObject.hasCollided = true;

                continue;
            }
            gameObject.transform.trn(0, -2f * delta, 0f);
            gameObject.body.setWorldTransform(gameObject.transform);
        }
        super.render(delta);
    }

    public boolean checkCollision(btCollisionObject b1, btCollisionObject b2) {
        CollisionObjectWrapper co0 = new CollisionObjectWrapper(b1);
        CollisionObjectWrapper co1 = new CollisionObjectWrapper(b2);

        btCollisionAlgorithm algorithm = dispatcher.findAlgorithm(co0.wrapper, co1.wrapper, null,
                ebtDispatcherQueryType.BT_CONTACT_POINT_ALGORITHMS);

        btDispatcherInfo info = new btDispatcherInfo();
        btManifoldResult result = new btManifoldResult(co0.wrapper, co1.wrapper);

        algorithm.processCollision(co0.wrapper, co1.wrapper, info, result);
        dispatcher.freeCollisionAlgorithm(algorithm.getCPointer());

        boolean r = false;

        btPersistentManifold man = result.getPersistentManifold();

        if (man != null) {
            r = man.getNumContacts() > 0;
        }

        result.dispose();
        info.dispose();
        co0.dispose();
        co1.dispose();

        return r;
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
