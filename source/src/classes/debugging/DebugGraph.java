package classes.debugging;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.awt.geom.Point2D;
import java.util.ArrayDeque;

/**
 * Created by Rene on 1/5/2015.
 */
public class DebugGraph extends DebugStatistic {
    private static final int HOR_BAR_COUNT = 25;
    private static final Point2D OVERLAY_OFFSET = new Point2D.Double(0, 0);

    private static final Color HOR_BAR_COLOR = Color.GRAY;
    private static final Color NAME_COLOR = Color.RED;
    private static final Color DATA_BAR_COLOR = Color.RED;
    private static final Color DATA_BACKGROUND_COLOR = Color.BLACK;

    public static final int BOX_WIDTH = 200;
    public static final int BOX_HEIGHT = 100;
    private static final double DATA_WIDTH_RELATIVE = 0.9;
    private static final double DATA_HEIGHT_RELATIVE = 0.6;
    private static final int DATA_SIZE = 20;

    private ArrayDeque<Double> data;
    private double currentMaxValue;

    public DebugGraph(String name) {
        super(name);
        data = new ArrayDeque<>(DATA_SIZE);
    }

    private double getMaxValue() {
        if (data == null || data.size() == 0) return 0;
        return data.stream().max((d1, d2) -> Double.compare(d1, d2)).get();
    }

    public void draw(GraphicsContext context, int x, int y) {
        context.setFont(new Font(12));

        // Background.
        context.setGlobalAlpha(0.8);
        context.setFill(Color.BLACK);
        context.fillRect(x + OVERLAY_OFFSET.getX(), y + OVERLAY_OFFSET.getY(), BOX_WIDTH, BOX_HEIGHT);

        // Horizontal background bars.
        context.setGlobalAlpha(0.1);
        context.setFill(HOR_BAR_COLOR);
        int barHeight = BOX_HEIGHT / HOR_BAR_COUNT;
        int barSpace = (int) (BOX_HEIGHT * 0.5 / HOR_BAR_COUNT);
        for (int i = 0; i < HOR_BAR_COUNT; i++) {
            context.fillRect(x + OVERLAY_OFFSET.getX(), y + (i * barHeight) + OVERLAY_OFFSET.getY(), BOX_WIDTH, barHeight - barSpace);
        }

        // Name.
        context.setGlobalAlpha(1);
        context.setFill(NAME_COLOR);
        context.fillText(name, x + 5, y + 14);

        // Background data.
        context.setGlobalAlpha(1);
        context.setFill(DATA_BACKGROUND_COLOR);
        double dataBoxWidth = BOX_WIDTH * DATA_WIDTH_RELATIVE;
        double dataBoxHeight = BOX_HEIGHT * DATA_HEIGHT_RELATIVE;
        double dataBoxMarginHorizontal = (BOX_WIDTH - dataBoxWidth) / 2;
        double dataBoxMarginVertical = (BOX_HEIGHT - dataBoxHeight) / 6;
        context.fillRect(x + dataBoxMarginHorizontal, y + dataBoxMarginVertical * 5, dataBoxWidth, dataBoxHeight);

        // Vertical data bars.
        context.setGlobalAlpha(0.7);
        context.setFill(DATA_BAR_COLOR);
        currentMaxValue = getMaxValue() == 0 ? 1 : getMaxValue();
        Double[] dataArray = new Double[data.size()];
        data.toArray(dataArray);
        for (int i = 0; i < dataArray.length; i++) {
            double barLength = BOX_HEIGHT * DATA_HEIGHT_RELATIVE / currentMaxValue * dataArray[i];
            context.fillRect(x + 5 + (i * 3) + OVERLAY_OFFSET.getX(), y + BOX_HEIGHT - barLength + OVERLAY_OFFSET.getY(), 2, barLength);
        }
    }

    public void addValue(double value) {
        data.addLast(value);
        if (data.size() > DATA_SIZE) {
            data.pop();
        }
    }
}
