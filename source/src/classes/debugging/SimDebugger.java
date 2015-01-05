package classes.debugging;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.*;
import java.util.concurrent.Callable;

/**
 * Created by Rene on 05-01-2015.
 */
public class SimDebugger {
    private static Map<String, DebugStatistic> statistics;
    public static double fontSize;
    public static int verticalPixelOffset;

    static {
        statistics = new LinkedHashMap<>();
        fontSize = 12;
        verticalPixelOffset = 2;
    }

    public static void addStatistic(DebugStatistic statistic) {
        String newKey = statistic.getName();
        while(statistics.containsKey(newKey)) {
            newKey = '_' + newKey;
        }
        statistics.put(newKey, statistic);
    }

    public static void addStatistic(String name, Callable<String> displayedValue) {
        addStatistic(new DebugStatistic(name, displayedValue));
    }

    public static void setStatistic(String name, String value) {
        if (!statistics.containsKey(name))
            statistics.put(name, new DebugStatistic(name, null));
        statistics.get(name).setDisplayedValue(() -> value);
    }

    public static void draw(GraphicsContext context) {
        context.save();
        context.setFont(new Font(fontSize));
        context.setFill(Color.BLACK);

        // Draw all statistics.
        int i = 0;
        for (DebugStatistic stat : statistics.values()) {
            context.fillText(stat.displayText() , 2, fontSize * (i + 1) + (verticalPixelOffset * i));
            i++;
        }

        context.restore();
    }
}
