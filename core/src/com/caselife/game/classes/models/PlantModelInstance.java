package com.caselife.game.classes.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.model.Animation;
import com.badlogic.gdx.graphics.g3d.model.NodeAnimation;
import com.badlogic.gdx.graphics.g3d.model.NodeKeyframe;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.caselife.game.classes.life.Animal;
import com.caselife.game.classes.life.Plant;
import com.caselife.game.classes.world.Node;

public class PlantModelInstance extends BaseModelInstance {

    private Plant plant;

    public PlantModelInstance(Model model, Plant plant) {
        super(model);

        this.plant = plant;
    }

    public Plant getPlant() {
        return plant;
    }

    static public PlantModelInstance createModelInstance(Plant plant) {
        ModelBuilder builder = new ModelBuilder();
        Model model = builder.createBox(5f, 5f, 5f, new Material(ColorAttribute.createSpecular(Color.GREEN)), VertexAttributes.Usage.Position);
        return new PlantModelInstance(model, plant);
    }

    public void update() {
        Color color = new Color(0, (float)plant.getEnergy() / (float)Plant.MAX_ENERGY, 0, 1);
        Node node = plant.getNode();

        this.materials.get(0).set(ColorAttribute.createDiffuse(color));
        this.transform.setTranslation(node.getX() * 5, node.getY() * 5, -8f);
    }
}
