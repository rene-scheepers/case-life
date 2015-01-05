package classes.debugging;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Rene on 05-01-2015.
 */
public class SimDebugger {
    private static List<DebugStatistic> statistics;
    public static double fontSize;
    public static int verticalPixelOffset;

    static {
        statistics = new ArrayList<>();
        fontSize = 12;
        verticalPixelOffset = 2;
    }

    public static void addStatistic(DebugStatistic statistic) {
        statistics.add(statistic);
    }

    public static void addStatistic(String name, Callable<String> displayedValue) {
        statistics.add(new DebugStatistic(name, displayedValue));
    }

    public static void draw(GraphicsContext context) {
        context.save();
        context.setFont(new Font(fontSize));
        context.setFill(Color.BLACK);

        for (int i = 0; i < statistics.size(); i++) {
            DebugStatistic stat = statistics.get(i);
            context.fillText(stat.displayText() , 2, fontSize * (i + 1) + (verticalPixelOffset * i));
        }

        context.restore();
    }
}
