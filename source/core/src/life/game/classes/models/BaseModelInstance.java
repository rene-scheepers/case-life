package life.game.classes.models;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

abstract public class BaseModelInstance extends ModelInstance {

    protected BaseModelInstance(Model model) {
        super(model);
    }

    abstract public void update();

}
