package classes.debugging;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * Created by Rene on 05-01-2015.
 */
public class SimDebugger {
    private static Map<String, DebugStatistic> statistics;
    public static double fontSize;
    public static final int VERTICAL_PIXEL_OFFSET = 4;

    public static boolean isVisible() {
        return visible;
    }

    public static void setVisible(boolean visible) {
        SimDebugger.visible = visible;
    }

    private static boolean visible;

    static {
        statistics = new LinkedHashMap<>();
        fontSize = 12;
    }

    public static void addStatistic(DebugStatistic statistic) {
        String newKey = statistic.getName();
        while(statistics.containsKey(newKey)) {
            newKey = '_' + newKey;
        }
        statistics.put(newKey, statistic);
    }

    public static void addDebugValue(String name, Callable<String> displayedValue) {
        addStatistic(new DebugValue(name, displayedValue));
    }

    public static void setDebugValue(String name, String value) {
        if (!statistics.containsKey(name))
            statistics.put(name, new DebugValue(name, null));
        ((DebugValue)statistics.get(name)).setDisplayedValue(() -> value);
    }

    public static void addDebugGraph(String name, int captureCount) {
        addStatistic(new DebugGraph(name, captureCount));
    }

    public static <T> T getDebugObject(String name) {
        if (statistics.containsKey(name))
            return (T)statistics.get(name);
        return null;
    }

    public static void draw(GraphicsContext context) {
        if (!isVisible()) return;

        context.save();
        context.setFont(new Font(fontSize));
        context.setFill(Color.BLACK);

        // Draw all DebugValue statistics.
        int i = 0;
        for (DebugStatistic stat : statistics.values().stream().filter((s) -> s.getClass() == DebugValue.class && !s.isHidden()).collect(Collectors.toList())) {
            context.fillText(((DebugValue)stat).displayText() , 2, fontSize * (i + 1) + (VERTICAL_PIXEL_OFFSET * i));
            i++;
        }
        context.restore();

        context.save();
        context.setFont(new Font(fontSize));
        context.setFill(Color.BLACK);

        // Draw all DebugGraph statistics.
        i = 0;
        for (DebugStatistic stat : statistics.values().stream().filter((s) -> s.getClass() == DebugGraph.class && !s.isHidden()).collect(Collectors.toList())) {
            ((DebugGraph)stat).draw(
                    context,
                    DebugGraph.BOX_WIDTH * i +  (i + 1) * 5,
                    (int)context.getCanvas().getHeight() - DebugGraph.BOX_HEIGHT - 5
            );
            i++;
        }
        context.restore();
    }

    /**
     * Clears all debug information.
     */
    public static void reset() {
        statistics.clear();
    }
}
