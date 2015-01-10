package com.caselife.game.classes.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.caselife.game.classes.life.Animal;
import com.caselife.game.classes.world.LocationType;
import com.caselife.game.classes.world.Node;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

public class NodeModelInstance extends BaseModelInstance {

    private Node node;

    public NodeModelInstance(Model model, Node node) {
        super(model);

        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    static public NodeModelInstance createModelInstance(Node node) {
        ModelBuilder builder = new ModelBuilder();
        Color color = Color.GRAY;
        float height = 20f;

        if (node.getLocationType().equals(LocationType.Land)) {
            color = Color.LIGHT_GRAY;
            height = 10f;
        } else if(node.getLocationType().equals(LocationType.Water)) {
            color = new Color(0.49f, 0.85f, 1, 1);
            height = 0f;
        }

        Model model = builder.createBox(5f,height, 5f, new Material(ColorAttribute.createDiffuse(color)), VertexAttributes.Usage.Position);
        NodeModelInstance instance = new NodeModelInstance(model, node);

        instance.transform.setTranslation(node.getX() * 5, height / 2, node.getY() * 5);

        return instance;
    }

    public void update() {
        //this.transform.setTranslation(node.getX() * 5, node.getY() * 5, 0);
    }
}
