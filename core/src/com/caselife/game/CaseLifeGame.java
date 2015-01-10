package com.caselife.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.caselife.game.camera.StrategyCamera;
import com.caselife.game.camera.StrategyCameraInputController;
import com.caselife.game.life.Animal;
import com.caselife.game.life.Life;
import com.caselife.game.life.Plant;
import com.caselife.game.models.AnimalModelContainer;
import com.caselife.game.models.ModelContainer;
import com.caselife.game.models.NodeModelContainer;
import com.caselife.game.models.PlantModelContainer;
import com.caselife.game.world.Node;
import com.caselife.game.world.Simulator;
import com.caselife.game.world.World;

import java.util.ArrayList;
import java.util.List;

public class CaseLifeGame extends ApplicationAdapter {
	private World world;
	private Simulator simulator;

	public List<ModelContainer> containers;
	public AnimalModelContainer follow;

	public Environment lights;
    public StrategyCameraInputController cameraInputController;
	public StrategyCamera camera;
	public ModelBatch modelBatch;
	public AssetManager assets;

	public void loadContent() {
		if (assets == null)
			assets = new AssetManager();
		else
			assets.clear();

		assets.load("maps/small.png", Texture.class);
		assets.finishLoading();
	}

	@Override
	public void create() {
		loadContent();
		Texture texture = assets.get("maps/small.png");

		world = World.instantiateWorldFromImage(texture);
		simulator = new Simulator(world);
		simulator.setSpeed(60);
		simulator.start();

		lights = new Environment();
		lights.set(new ColorAttribute(ColorAttribute.AmbientLight, Color.WHITE));
		lights.add(new DirectionalLight().set(1f, 1f, 1f, -1f, -0.8f, -0.2f));

		modelBatch = new ModelBatch();

		camera = new StrategyCamera(90, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(world.getWidth() * 5 / 2, 150, world.getHeight() * 5);

		camera.update();

        cameraInputController = new StrategyCameraInputController(camera);
        Gdx.input.setInputProcessor(cameraInputController);

        containers = getModelInstances();
	}


	@Override
	public void render() {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glClearColor(0.9f,0.9f,0.9f,0);

		for (ModelContainer instance : containers) {
			instance.update();
		}

        cameraInputController.update();
		modelBatch.begin(camera);
		modelBatch.render(containers);
		modelBatch.end();
	}

	private List<ModelContainer> getModelInstances() {
		List<ModelContainer> instances = new ArrayList();

		Node[][] nodes = world.getNodes();
		for (int x = 0; x < nodes.length; x++) {
			for (int y = 0; y < nodes[x].length; y++) {
				// Add node.
				ModelContainer instance = new NodeModelContainer(nodes[x][y]);
				instances.add(instance);

				// Add life.
				Life life = nodes[x][y].getHolder();
				if (life != null) {
					if (life instanceof Animal) {
						instances.add(new AnimalModelContainer((Animal) life));
					} else if (life instanceof Plant) {
						instances.add(new PlantModelContainer((Plant) life));
					}
				}
			}
		}

		return instances;
	}
}
