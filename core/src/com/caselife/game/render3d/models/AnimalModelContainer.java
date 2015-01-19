package com.caselife.game.render3d.models;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.caselife.logic.life.Animal;
import com.caselife.logic.life.Life;
import com.caselife.logic.world.Node;

public class AnimalModelContainer extends ModelContainer {

    private final Animal animal;

    private ModelInstance instance;
    private float scale;

    public AnimalModelContainer(Animal animal, Model model, float scale) {
        this.animal = animal;
        Node node = animal.getNode();
        this.scale = scale;

        this.instance = new ModelInstance(model, node.getX() * scale, 13.5f, node.getY() * scale);
    }

    public Life getLife() {
        return animal;
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
        instance.transform.setTranslation(node.getX() * scale, vector.y, node.getY() * scale);
    }
}
