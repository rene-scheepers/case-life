package com.caselife.game.classes.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.caselife.game.classes.life.Animal;
import com.caselife.game.classes.world.Node;

public class AnimalModelContainer extends ModelContainer {

    private final Animal animal;

    private ModelInstance instance;

    public AnimalModelContainer(Animal animal) {
        this.animal = animal;

        Model model = modelBuilder.createBox(5f, 5f, 5f, new Material(ColorAttribute.createDiffuse(Color.RED)), VertexAttributes.Usage.Position);

        instance = new ModelInstance(model);
        instance.transform.trn(0, 12.5f, 0);
    }

    public Animal getAnimal() {
        return animal;
    }

    public ModelInstance getModelInstance() {
        return instance;
    }

    public void update() {
        Vector3 vector = new Vector3();
        instance.transform.getTranslation(vector);

        Node node = animal.getNode();
        instance.transform.setTranslation(node.getX() * 5, vector.y, node.getY() * 5);
    }
}
