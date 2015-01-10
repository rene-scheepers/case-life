package com.caselife.game.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;
import com.caselife.game.life.Plant;
import com.caselife.game.world.Node;

public class PlantModelContainer extends ModelContainer {

    private ModelInstance instance;

    private final Plant plant;

    public PlantModelContainer(Plant plant) {

        this.plant = plant;

        Model model = modelBuilder.createBox(5f, 5f, 5f, new Material(ColorAttribute.createSpecular(Color.GREEN)), VertexAttributes.Usage.Position);
        instance = new ModelInstance(model);
        instance.transform.trn(0, 12.5f, 0);
    }

    public Plant getPlant() {
        return plant;
    }

    public ModelInstance getModelInstance() {
        return instance;
    }

    public void update() {
        Color color = new Color(0, (float) plant.getEnergy() / (float) Plant.MAX_ENERGY, 0, 1);
        Node node = plant.getNode();
        Vector3 vector = new Vector3();

        instance.transform.getTranslation(vector);
        instance.materials.get(0).set(ColorAttribute.createDiffuse(color));
        instance.transform.setTranslation(node.getX() * 5, vector.y, node.getY() * 5);
    }
}
