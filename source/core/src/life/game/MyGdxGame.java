package life.game;

import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import life.game.classes.Simulator;
import life.game.classes.life.Animal;
import life.game.classes.life.Life;
import life.game.classes.life.Plant;
import life.game.classes.models.AnimalModelInstance;
import life.game.classes.models.BaseModelInstance;
import life.game.classes.models.NodeModelInstance;
import life.game.classes.models.PlantModelInstance;
import life.game.classes.world.LocationType;
import life.game.classes.world.Node;
import life.game.classes.world.World;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class MyGdxGame extends ApplicationAdapter {
    private World world;
    private Simulator simulator;

    public MyGdxGame() {
        File file = new File("C:\\Users\\Rene\\Downloads\\test\\core\\assets\\maps\\small.png");
        BufferedImage image = null;
        try {
            image = ImageIO.read(file);
        } catch (Exception ex) {

        }

        world = World.instantiateWorldFromImage(image);
        simulator = new Simulator(world);
        simulator.setSpeed(60);
        simulator.start();
    }

    public ArrayList<BaseModelInstance> instances;
    public AnimalModelInstance follow;

    public Environment lights;
    public PerspectiveCamera camera;
    public ModelBatch modelBatch;

    @Override
    public void create() {
        lights = new Environment();
        lights.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        lights.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        modelBatch = new ModelBatch();

        camera = new PerspectiveCamera(70, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(105f, 50f, -80f);
        camera.lookAt(50f, 50f, 0);
        camera.near = 1f;
        camera.far = 300f;
        camera.update();

        instances = getModelInstances();
    }


    @Override
    public void render() {
        Vector3 vector = new Vector3();
        follow.transform.getTranslation(vector);

        camera.position.set(vector.x +10, vector.y+10, -10);
        camera.lookAt(vector);
        camera.update();
//        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
//            camera.translate(5,0,0);
//            camera.update();
//        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
//            camera.translate(-5,0,0);
//            camera.update();
//        }

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

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
