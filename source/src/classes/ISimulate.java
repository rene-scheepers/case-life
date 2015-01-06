package classes;

import classes.world.Node;
import javafx.scene.canvas.GraphicsContext;

public interface ISimulate {

    public void simulate();

    public void draw(GraphicsContext context, int offsetX, int offsetY);
}
