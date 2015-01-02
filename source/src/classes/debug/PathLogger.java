package classes.debug;

import classes.world.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Rene on 2-1-2015.
 */
public class PathLogger {

    private GraphicsContext context;
    private double drawWidth;
    private double drawHeight;
    private HashMap<Node, Integer> nodesActivityMap = new HashMap();

    public PathLogger(GraphicsContext context) {
        this.context = context;
        drawHeight = context.getCanvas().getHeight();
        drawWidth = context.getCanvas().getWidth();
    }

    public void draw() {
        context.clearRect(0, 0, drawWidth, drawHeight);

        Iterator it = nodesActivityMap.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<Node, Integer> pair = (Map.Entry)it.next();

            context.setFill(Color.RED);
            context.fillRect();
        }
    }

    public void clear() {

    }

}
