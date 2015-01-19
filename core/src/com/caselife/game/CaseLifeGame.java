package com.caselife.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.math.Vector3;
import com.caselife.game.camera.StrategyCamera;
import com.caselife.game.camera.StrategyCameraInputController;
import com.caselife.game.life.Life;
import com.caselife.game.world.Simulator;
import com.caselife.game.world.World;

import java.util.ArrayList;

public class CaseLifeGame extends ApplicationAdapter {
    private World world;
    private Simulator simulator;
    private WorldRenderableProvider worldRenderableProvider;

    private StrategyCameraInputController cameraInputController;
    private ModelBatch modelBatch;
    private AssetManager assets;
    private SpriteBatch spriteBatch;

    private float scale = 5f;

    public ModelInstance xyz;

    public void loadContent() {
        if (assets == null) {
            assets = new AssetManager();
        } else {
            assets.clear();
        }

        assets.load("maps/small.png", Texture.class);
        assets.finishLoading();
    }

    @Override
    public void create() {
        GLProfiler.enable();
        loadContent();
        Texture texture = assets.get("maps/small.png");

        world = World.instantiateWorldFromImage(texture);
        simulator = new Simulator(world);
        simulator.setSpeed(60);
        simulator.start();

        modelBatch = new ModelBatch();
        spriteBatch = new SpriteBatch();

        StrategyCamera camera = new StrategyCamera(
                90,
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight(),
                world,
                scale
        );
        cameraInputController = new StrategyCameraInputController(camera);
        Gdx.input.setInputProcessor(cameraInputController);

        ModelBuilder builder = new ModelBuilder();
        Model model = builder.createXYZCoordinates(200f, new Material(ColorAttribute.createDiffuse(Color.GREEN)), VertexAttributes.Usage.Position);
        xyz = new ModelInstance(model, 0f, 0f, 0f);

        worldRenderableProvider = new WorldRenderableProvider(world, scale);
    }


    @Override
    public void render() {
        GLProfiler.reset();
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glClearColor(0.9f, 0.9f, 0.9f, 0);

        cameraInputController.update();
        renderModels();
        renderDebug();
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
        modelBatch.render(xyz);
        modelBatch.end();
    }

    private void renderDebug() {
        String debugText = String.format("FPS: %s\r\nVertices: %s\r\nDraw calls: %s", Gdx.graphics.getFramesPerSecond(), GLProfiler.vertexCount.total, GLProfiler.drawCalls);
        BitmapFont font = new BitmapFont();
        font.setColor(Color.BLACK);
        spriteBatch.begin();
        font.drawMultiLine(spriteBatch, debugText, 10, 50);
        spriteBatch.end();
    }

}
