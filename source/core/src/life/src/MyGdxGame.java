package life.src;

import classes.Simulator;
import classes.life.Animal;
import classes.life.Life;
import classes.world.LocationType;
import classes.world.Node;
import classes.world.World;
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
import com.badlogic.gdx.scenes.scene2d.ui.List;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class MyGdxGame extends ApplicationAdapter {
    private World world;
    private Simulator simulator;

    public MyGdxGame()
    {
        File file = new File("C:\\Users\\Rene\\Downloads\\test\\core\\assets\\maps\\small.png");
        BufferedImage image = null;
        try {
            image = ImageIO.read(file);
        } catch(Exception ex) {

        }

        world = World.instantiateWorldFromImage(image);
        simulator = new Simulator(world);
        simulator.setSpeed(5);
        simulator.start();
    }

    public ArrayList<ModelInstance> models;
    public HashMap<Life, ModelInstance> modelsLife = new HashMap();

    public Environment lights;
    public PerspectiveCamera cam;
    public ModelBatch modelBatch;
    public ModelInstance instance;

    @Override
    public void create() {
        models = new ArrayList();

        lights = new Environment();
        lights.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        lights.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        modelBatch = new ModelBatch();

        cam = new PerspectiveCamera(70, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(105f, 50f, -80f);
        cam.lookAt(50f,50f,0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        ModelBuilder modelBuilder = new ModelBuilder();
        Node[][] nodes = world.getNodes();
        for (int x = 0; x < nodes.length; x++) {
            for (int y = 0; y < nodes[x].length; y++) {
                Node node = nodes[x][y];

                float height;
                Color color;
                if (node.getLocationType().equals(LocationType.Land)) {
                    height = 4f;
                    color = Color.GREEN;
                } else if(node.getLocationType().equals(LocationType.Obstacle)) {
                    height = 6f;
                    color = Color.RED;
                } else {
                    height = 1f;
                    color = Color.BLUE;
                }

                Model model = modelBuilder.createBox(2f, 2f, height,
                        new Material(ColorAttribute.createDiffuse(color)),
                        Usage.Position | Usage.Normal);
                ModelInstance modelInstance = new ModelInstance(model, x , y , 0);
                models.add(modelInstance);
            }
        }
    }

    @Override
    public void render() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            cam.translate(0, 0, -5);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            cam.translate(0, 0, 5);
        }

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        ArrayList<ModelInstance> lifeinstances = new ArrayList();
        ModelBuilder modelBuilder = new ModelBuilder();

        for (Life life : world.getLives()) {
            Node node = life.getNode();
            ModelInstance inst = modelsLife.get(life);

            if (inst == null) {
                Color color;
                if (life instanceof Animal) {
                    color = Color.PURPLE;
                } else {
                    color = Color.CYAN;
                }

                Model model = modelBuilder.createBox(1f, 1f, 5f,
                        new Material(ColorAttribute.createDiffuse(color)),
                        Usage.Position | Usage.Normal);
                ModelInstance modelInstance = new ModelInstance(model, node.getX() , node.getY() , 0);

                modelsLife.put(life, modelInstance);
            } else {
                if (life == null) {
                    inst.model.dispose();
                }
                Color color;
                if (life.getEnergy() < 1) {
                    color = Color.BLUE;
                } else {
                    if (life instanceof Animal) {
                        color = Color.LIGHT_GRAY;
                    } else {
                        color = Color.GRAY;
                    }
                }

                inst = new ModelInstance(inst.model, node.getX(), node.getY(), 0);
                inst.model.getMaterial(inst.model.materials.get(0).id).set(ColorAttribute.createDiffuse(color));
                modelsLife.put(life, inst);
            }
        }

        ArrayList<ModelInstance> temp = new ArrayList();
        temp.addAll(models);
        temp.addAll(modelsLife.values());
        modelBatch.begin(cam);
        modelBatch.render(temp, lights);
        //modelBatch.render(models, lights);
        modelBatch.end();
    }
//
//    @Override
//    public void dispose() {
//        modelBatch.dispose();
//        model.dispose();
//    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
}
