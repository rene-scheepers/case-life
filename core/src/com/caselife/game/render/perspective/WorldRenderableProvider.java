package com.caselife.game.render.perspective;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.caselife.logic.life.Animal;
import com.caselife.logic.life.Life;
import com.caselife.logic.life.Plant;
import com.caselife.game.render.perspective.models.AnimalModelContainer;
import com.caselife.game.render.perspective.models.ModelContainer;
import com.caselife.logic.world.LocationType;
import com.caselife.logic.world.Node;
import com.caselife.logic.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WorldRenderableProvider implements RenderableProvider {

    private final float scale;

    private final Model modelNodeWater;
    private final Model modelNodeLand;
    private final Model modelNodeObstacle;
    private final Model modelLifePlant;
    private final Model modelLifeAnimal;

    private List<ModelInstance> instances = new ArrayList();
    private List<ModelContainer> dynamicInstances = new ArrayList();
    private ModelBuilder modelBuilder = new ModelBuilder();
    private World world;

    public WorldRenderableProvider(World world, float scale) {
        this.world = world;
        this.scale = scale;

        modelNodeWater = modelBuilder.createBox(world.getWidth() * 5, 1f, world.getHeight() * 5, new Material(ColorAttribute.createDiffuse(Color.WHITE)), VertexAttributes.Usage.Position);
        modelNodeLand = modelBuilder.createBox(5f, 10f, 5f, new Material(ColorAttribute.createDiffuse(Color.LIGHT_GRAY)), VertexAttributes.Usage.Position);
        modelNodeObstacle = modelBuilder.createBox(5f, 30f, 5f, new Material(ColorAttribute.createDiffuse(Color.BLACK)), VertexAttributes.Usage.Position);
        modelLifePlant = modelBuilder.createBox(5f, 15f, 5f, new Material(ColorAttribute.createDiffuse(Color.DARK_GRAY)), VertexAttributes.Usage.Position);
        modelLifeAnimal = modelBuilder.createBox(5f, 5f, 5f, new Material(ColorAttribute.createDiffuse(Color.RED)), VertexAttributes.Usage.Position);

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
        for (ModelContainer container : dynamicInstances) {
            instances.add(container.getModelInstance());
        }

        return instances;
    }

    private void buildWorld() {
        instances.add(new ModelInstance(modelNodeWater, world.getWidth() * scale / 2, 0.5f, world.getHeight() * scale / 2));

        Node[][] nodes = world.getNodes();
        for (int x = 0; x < nodes.length; x++) {
            for (int y = 0; y < nodes[x].length; y++) {
                Node node = nodes[x][y];

                if (node.getLocationType().equals(LocationType.Land)) {
                    instances.add(new ModelInstance(modelNodeLand, x * scale, 6f, y * scale));
                } else if (node.getLocationType().equals(LocationType.Obstacle)) {
                    instances.add(new ModelInstance(modelNodeObstacle, x * scale, 16f, y * scale));
                }
            }
        }

        for (Life life : world.getLives()) {
            Node node = life.getNode();
            if (life instanceof Animal) {
                Animal animal = (Animal) life;
                dynamicInstances.add(new AnimalModelContainer(animal, modelLifeAnimal, scale));
            } else if (life instanceof Plant) {
                Plant plant = (Plant) life;
                instances.add(new ModelInstance(modelLifePlant, node.getX() * scale, 18.5f, node.getY() * scale));
            }
        }
    }

    protected void update() {
        dynamicInstances.clear();

        for (Animal animal : world.getLives().stream().filter(x -> x.getClass() == Animal.class).map(y -> (Animal) y).collect(Collectors.toList())) {
            dynamicInstances.add(new AnimalModelContainer(animal, modelLifeAnimal, scale));
        }

        dynamicInstances.forEach(x -> x.update());
    }
}
