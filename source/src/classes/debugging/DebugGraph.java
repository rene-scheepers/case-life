package classes.debugging;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.omg.CORBA.PUBLIC_MEMBER;

/**
 * Created by Anton on 1/5/2015.
 */
public class DebugGraph extends DebugStatistic {
    public static final int WIDTH = 200;
    public static final int HEIGHT = 60;
    private static final Color NAME_COLOR = Color.RED;

    public DebugGraph(String name) {
        super(name);
    }

    public void draw(GraphicsContext context, int x, int y) {
        context.setFont(new Font(12));

        // Background.
        context.setGlobalAlpha(0.8);
        context.setFill(Color.BLACK);
        context.fillRect(x, y, WIDTH, HEIGHT);

        // Name.
        context.setGlobalAlpha(1);
        context.setFill(NAME_COLOR);
        context.fillText(name, x + 5, y + 14);
    }
}
