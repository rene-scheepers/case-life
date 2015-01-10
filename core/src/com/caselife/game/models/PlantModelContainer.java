package com.caselife.game.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.caselife.game.life.Plant;
import com.caselife.game.world.Node;

public class PlantModelContainer extends ModelContainer {

    private ModelInstance instance;

    private final Plant plant;

    public PlantModelContainer(Plant plant) {

        this.plant = plant;

        Model model = modelBuilder.createBox(5f, 35f, 5f, new Material(ColorAttribute.createDiffuse(Color.GREEN)), VertexAttributes.Usage.Position);
        instance = new ModelInstance(model);

        instance.transform.trn(0, 27.5f, 0);
    }

    public Plant getPlant() {
        return plant;
    }

    public ModelInstance getModelInstance() {
        return instance;
    }

    public void update() {
        float percentage = (float)plant.getEnergy() / (float)Plant.MAX_ENERGY;
        float height = 35 * percentage;

        if (height > 0) {
            Node node = plant.getNode();
            instance = new ModelInstance(modelBuilder.createBox(5f, height, 5f, new Material(ColorAttribute.createDiffuse(Color.GREEN)), VertexAttributes.Usage.Position));

            instance.transform.setTranslation(node.getX() * 5, 10 + height / 2, node.getY() * 5);
        } else {
            instance = null;
        }
    }
}
