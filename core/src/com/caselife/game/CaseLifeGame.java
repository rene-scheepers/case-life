package com.caselife.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.caselife.game.classes.Simulator;
import com.caselife.game.classes.life.Animal;
import com.caselife.game.classes.life.Life;
import com.caselife.game.classes.life.Plant;
import com.caselife.game.classes.models.AnimalModelInstance;
import com.caselife.game.classes.models.BaseModelInstance;
import com.caselife.game.classes.models.NodeModelInstance;
import com.caselife.game.classes.models.PlantModelInstance;
import com.caselife.game.classes.world.Node;
import com.caselife.game.classes.world.World;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class CaseLifeGame extends ApplicationAdapter {
	private World world;
	private Simulator simulator;

	public CaseLifeGame() {

	}

	public ArrayList<BaseModelInstance> instances;
	public AnimalModelInstance follow;

	public Environment lights;
    public CameraInputController cameraInputController;
	public PerspectiveCamera camera;
	public ModelBatch modelBatch;

	@Override
	public void create() {
		File file = Gdx.files.internal("maps\\small.png").file();
		BufferedImage image = null;
		try {
			image = ImageIO.read(file);
		} catch (Exception ex) {

		}

		world = World.instantiateWorldFromImage(image);
		simulator = new Simulator(world);
		simulator.setSpeed(60);
		simulator.start();

		lights = new Environment();
		lights.set(new ColorAttribute(ColorAttribute.AmbientLight, Color.WHITE));
		lights.add(new DirectionalLight().set(1f, 1f, 1f, -1f, -0.8f, -0.2f));

		modelBatch = new ModelBatch();

		camera = new PerspectiveCamera(70, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(105f, 50f, -80f);
		camera.lookAt(50f, 50f, 0);
		camera.rotate(270, 1, 0, 0);
		camera.near = 1f;
		camera.far = 3000f;
		camera.update();

        cameraInputController = new CameraInputController(camera);
        Gdx.input.setInputProcessor(cameraInputController);
        cameraInputController.scrollFactor = 5;

		instances = getModelInstances();
	}


	@Override
	public void render() {
		Vector3 vector = new Vector3();
		follow.transform.getTranslation(vector);

		//camera.position.set(vector.x -20, vector.y - 20, -30);
		camera.lookAt(vector);
		camera.update();

        Gdx.gl.glClearColor(0.9f,0.9f,0.9f,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


		for (BaseModelInstance instance : instances) {
			instance.update();
		}

		modelBatch.begin(camera);
		modelBatch.render(instances);
		modelBatch.end();
	}

	private ArrayList<BaseModelInstance> getModelInstances() {
		ArrayList<BaseModelInstance> instances = new ArrayList();
		ModelBuilder builder = new ModelBuilder();

		Node[][] nodes = world.getNodes();
		for (int x = 0; x < nodes.length; x++) {
			for (int y = 0; y < nodes[x].length; y++) {
				BaseModelInstance instance = NodeModelInstance.createModelInstance(nodes[x][y]);
				instances.add(instance);
			}
		}

		for (Life life : world.getLives()) {
			BaseModelInstance instance = null;
			if (life instanceof Animal) {
				instance = AnimalModelInstance.createModelInstance((Animal) life);
				if (follow == null) {
					follow = (AnimalModelInstance)instance;
				}
			} else if (life instanceof Plant) {
				instance = PlantModelInstance.createModelInstance((Plant) life);
			}

			instances.add(instance);
		}

		return instances;
	}
}
