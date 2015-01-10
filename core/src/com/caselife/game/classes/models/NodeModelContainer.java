package com.caselife.game.classes.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.caselife.game.classes.world.LocationType;
import com.caselife.game.classes.world.Node;
import com.badlogic.gdx.graphics.g3d.Model;

public class NodeModelContainer extends ModelContainer {

    private final Node node;

    private ModelInstance instance;

    public NodeModelContainer(Node node) {
        this.node = node;

        Color color = Color.GRAY;
        float height = 20f;

        if (node.getLocationType().equals(LocationType.Land)) {
            color = Color.LIGHT_GRAY;
            height = 10f;
        } else if(node.getLocationType().equals(LocationType.Water)) {
            color = new Color(0.49f, 0.85f, 1, 1);
            height = 0f;
        }

        Model model = modelBuilder.createBox(5f,height, 5f, new Material(ColorAttribute.createDiffuse(color)), VertexAttributes.Usage.Position);
        instance = new ModelInstance(model);

        instance.transform.setTranslation(node.getX() * 5, height / 2, node.getY() * 5);
    }

    public Node getNode() {
        return node;
    }

    public ModelInstance getModelInstance() {
        return instance;
    }

    public void update() {
    }
}
