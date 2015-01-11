package com.caselife.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.caselife.game.camera.StrategyCamera;
import com.caselife.game.camera.StrategyCameraInputController;
import com.caselife.game.world.Simulator;
import com.caselife.game.world.World;

import java.util.ArrayList;

public class CaseLifeGame extends ApplicationAdapter {
    private World world;
    private Simulator simulator;
    private WorldRenderableProvider worldRenderableProvider;

    private Environment lights;
    private StrategyCameraInputController cameraInputController;
    private ModelBatch modelBatch;
    private AssetManager assets;

    public ModelInstance xyz;

    public void loadContent() {
        if (assets == null)
            assets = new AssetManager();
        else
            assets.clear();

        assets.load("maps/medium.png", Texture.class);
        assets.finishLoading();
    }

    ArrayList<ModelInstance> inst = new ArrayList();

    @Override
    public void create() {
        loadContent();
        Texture texture = assets.get("maps/medium.png");

        world = World.instantiateWorldFromImage(texture);
        simulator = new Simulator(world);
        simulator.setSpeed(60);
        simulator.start();

        lights = new Environment();
        lights.set(new ColorAttribute(ColorAttribute.AmbientLight, Color.WHITE));
        lights.add(new DirectionalLight().set(1f, 1f, 1f, -1f, -0.8f, -0.2f));

        modelBatch = new ModelBatch();

        StrategyCamera camera = new StrategyCamera(
                90,
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight(),
                0,
                world.getWidth() * 5,
                30,
                200,
                0,
                world.getHeight() * 5);

        camera.position.set(world.getWidth() * 5 / 2, 150, world.getHeight() * 5);
        camera.update();

        cameraInputController = new StrategyCameraInputController(camera);
        Gdx.input.setInputProcessor(cameraInputController);

        ModelBuilder builder = new ModelBuilder();
        Model model = builder.createXYZCoordinates(200f, new Material(ColorAttribute.createDiffuse(Color.GREEN)), VertexAttributes.Usage.Position);
        xyz = new ModelInstance(model, 0f, 0f, 0f);

        worldRenderableProvider = new WorldRenderableProvider(world);
    }


    @Override
    public void render() {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glClearColor(0.9f, 0.9f, 0.9f, 0);

        cameraInputController.update();
        modelBatch.begin(cameraInputController.camera);
        int countVisible = 0;
        worldRenderableProvider.update();
        for (ModelInstance instance : worldRenderableProvider.getModelInstances()) {
            Vector3 position = new Vector3();
            instance.transform.getTranslation(position);
            if (cameraInputController.camera.frustum.pointInFrustum(position)) {
                modelBatch.render(instance, lights);
                countVisible++;
            }
        }
        modelBatch.end();
    }

}
