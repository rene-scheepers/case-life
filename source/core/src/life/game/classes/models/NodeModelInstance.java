package life.game.classes.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import life.game.classes.life.Animal;
import life.game.classes.world.LocationType;
import life.game.classes.world.Node;
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
        Color color = Color.DARK_GRAY;
        float height = 35f;

        if (node.getLocationType().equals(LocationType.Land)) {
            color = Color.GRAY;
            height = 10f;
        } else if(node.getLocationType().equals(LocationType.Water)) {
            color = Color.LIGHT_GRAY;
            height = 0f;
        }

        Model model = builder.createBox(5f, 5f, height, new Material(ColorAttribute.createDiffuse(color)), VertexAttributes.Usage.Position);
        return new NodeModelInstance(model, node);
    }

    public void update() {
        this.transform.setTranslation(node.getX() * 5, node.getY() * 5, 0);
    }
}
