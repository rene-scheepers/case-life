package com.caselife.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.caselife.game.life.Animal;
import com.caselife.game.life.Life;
import com.caselife.game.life.Plant;
import com.caselife.game.models.AnimalModelContainer;
import com.caselife.game.models.ModelContainer;
import com.caselife.game.world.LocationType;
import com.caselife.game.world.Node;
import com.caselife.game.world.World;

import java.util.ArrayList;
import java.util.List;

public class WorldRenderableProvider implements RenderableProvider {

    static public final float SCALE = 5f;

    private List<ModelInstance> instances = new ArrayList();
    private List<ModelContainer> dynamicInstances = new ArrayList();
    private ModelBuilder modelBuilder = new ModelBuilder();
    private World world;

    public WorldRenderableProvider(World world) {
        this.world = world;

        buildWorld();
    }

    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
        for (ModelInstance instance : instances) {
            instance.getRenderables(renderables, pool);
        }
    }

    public List<ModelInstance> getModelInstances() {
        List<ModelInstance> instances = new ArrayList(this.instances);
        for(ModelContainer container : dynamicInstances) {
            instances.add(container.getModelInstance());
        }

        return instances;
    }

    private void buildWorld() {
        Model modelNodeWater = modelBuilder.createBox(world.getWidth() * 5, 1f, world.getHeight() * 5, new Material(ColorAttribute.createDiffuse(Color.WHITE)), VertexAttributes.Usage.Position);
        Model modelNodeLand = modelBuilder.createBox(5f, 10f, 5f, new Material(ColorAttribute.createDiffuse(Color.LIGHT_GRAY)), VertexAttributes.Usage.Position);
        Model modelNodeObstacle = modelBuilder.createBox(5f, 30f, 5f, new Material(ColorAttribute.createDiffuse(Color.BLACK)), VertexAttributes.Usage.Position);
        Model modelLifePlant = modelBuilder.createBox(5f, 15f, 5f, new Material(ColorAttribute.createDiffuse(Color.DARK_GRAY)), VertexAttributes.Usage.Position);
        Model modelLifeAnimal = modelBuilder.createBox(5f, 5f, 5f, new Material(ColorAttribute.createDiffuse(Color.RED)), VertexAttributes.Usage.Position);

        instances.add(new ModelInstance(modelNodeWater, world.getWidth() * SCALE / 2, 0.5f, world.getHeight() * SCALE / 2));

        Node[][] nodes = world.getNodes();
        for (int x = 0; x < nodes.length; x++) {
            for (int y = 0; y < nodes[x].length; y++) {
                Node node = nodes[x][y];

                if (node.getLocationType().equals(LocationType.Land)) {
                    instances.add(new ModelInstance(modelNodeLand, x * SCALE, 6f, y * SCALE));
                } else if (node.getLocationType().equals(LocationType.Obstacle)) {
                    instances.add(new ModelInstance(modelNodeObstacle, x * SCALE, 16f, y * SCALE));
                }
            }
        }

        for (Life life : world.getLives()) {
            Node node = life.getNode();
            if (life instanceof Animal) {
                Animal animal = (Animal)life;
                dynamicInstances.add(new AnimalModelContainer(animal, modelLifeAnimal, SCALE));
            } else if(life instanceof Plant) {
                Plant plant = (Plant)life;
                instances.add(new ModelInstance(modelLifePlant, node.getX() * SCALE, 18.5f, node.getY() * SCALE));
            }
        }
    }

    protected void update() {
        for(ModelContainer container : dynamicInstances) {
            container.update();
        }
    }
}
