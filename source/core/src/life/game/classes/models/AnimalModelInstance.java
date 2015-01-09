package life.game.classes.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import life.game.classes.life.Animal;
import life.game.classes.world.Node;

public class AnimalModelInstance extends BaseModelInstance {

    private Animal animal;

    public AnimalModelInstance(Model model, Animal animal) {
        super(model);

        this.animal = animal;
    }

    static public AnimalModelInstance createModelInstance(Animal animal) {
        ModelBuilder builder = new ModelBuilder();
        Model model = builder.createBox(5f, 5f, 5f, new Material(ColorAttribute.createDiffuse(Color.RED)), VertexAttributes.Usage.Position);
        return new AnimalModelInstance(model, animal);
    }

    public Animal getAnimal() {
        return animal;
    }

    public void update() {
        Node node = animal.getNode();
        this.transform.setTranslation(node.getX() * 5, node.getY() * 5, -8);
    }
}
