package com.bullet.game.entitys;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Disposable;
import net.mgsx.gltf.loaders.glb.GLBLoader;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

public class Player implements Disposable, AnimationController.AnimationListener {
    private SceneAsset sceneAsset;
    private Scene scene;
    private ModelInstance player;
    private Vector3 playerPosition;
    private BoundingBox boundingBox;

    public Player(){
        sceneAsset = new GLBLoader().load(Gdx.files.internal("Druid.glb"));
        scene = new Scene(sceneAsset.scene);
        player = scene.modelInstance;
        playerPosition = new Vector3(0f,0f,0f);
    }
    public void update(){
        
    }
    public ModelInstance getModelInstance() {
        return player;
    }

    @Override
    public void dispose() {
        sceneAsset.dispose();
    }

    @Override
    public void onEnd(AnimationController.AnimationDesc animation) {

    }

    @Override
    public void onLoop(AnimationController.AnimationDesc animation) {

    }
}
