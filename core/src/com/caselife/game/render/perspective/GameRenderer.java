package com.caselife.game.render.perspective;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.math.Vector3;
import com.caselife.game.Renderer;
import com.caselife.game.render.perspective.camera.StrategyCamera;
import com.caselife.game.render.perspective.camera.StrategyCameraInputController;
import com.caselife.logic.Simulator;
import com.caselife.logic.world.World;

import java.util.ArrayList;

public class GameRenderer implements Renderer {
    private WorldRenderableProvider worldRenderableProvider;
    private StrategyCameraInputController cameraInputController;
    private ModelBatch modelBatch;

    private float scale = 5f;

    public GameRenderer(World world, Simulator simulator)
    {
        modelBatch = new ModelBatch();

        StrategyCamera camera = new StrategyCamera(90, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), world, 5f);
        cameraInputController = new StrategyCameraInputController(camera);

        Gdx.input.setInputProcessor(cameraInputController);

        worldRenderableProvider = new WorldRenderableProvider(world, scale);

    }

    @Override
    public void render() {
        cameraInputController.update();
        renderModels();
    }

    @Override
    public void activate() {
        Gdx.input.setInputProcessor(cameraInputController);
    }

    private void renderModels()
    {
        ArrayList<ModelInstance> visible = new ArrayList();
        worldRenderableProvider.update();
        for (ModelInstance instance : worldRenderableProvider.getModelInstances()) {
            Vector3 position = new Vector3();
            instance.transform.getTranslation(position);
            if (cameraInputController.camera.frustum.pointInFrustum(position)) {
                visible.add(instance);
            }
        }

        modelBatch.begin(cameraInputController.camera);
        modelBatch.render(visible);
        modelBatch.end();
    }



}
