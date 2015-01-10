package com.caselife.game.classes.debugging;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.awt.geom.Point2D;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by Rene on 1/5/2015.
 */
public class DebugGraph extends DebugStatistic {
    private static final int HOR_BAR_COUNT = 25;
    private static final Point2D OVERLAY_OFFSET = new Point2D.Double(0, 0);

    private static final Color HOR_BAR_COLOR = Color.GRAY;
    private static final Color DATA_BAR_COLOR = Color.RED;
    private static final Color DATA_BACKGROUND_COLOR = Color.BLACK;
    private static final Color TEXT_NAME_COLOR = Color.WHITESMOKE;
    private static final Color TEXT_MINIMUM_COLOR = Color.CYAN;
    private static final Color TEXT_AVERAGE_COLOR = Color.YELLOW;
    private static final Color TEXT_MAXIMUM_COLOR = Color.INDIANRED;

    public static final int BOX_WIDTH = 250;
    public static final int BOX_HEIGHT = 100;
    private static final float DATA_WIDTH_RELATIVE = 0.9f;
    private static final float DATA_HEIGHT_RELATIVE = 0.6f;
    private static final int DATA_BAR_SPACE = 1;

    private BlockingDeque<Double> data;
    private double currentMaxValue;
    private int dataSize;
    private String preFix;
    private String postFix;

    public DebugGraph(String name, int captureSize) {
        this(name, captureSize, null, null);
    }

    public DebugGraph(String name, int captureSize, String preFix, String postFix) {
        super(name);
        this.dataSize = captureSize;
        this.preFix = preFix == null ? "" : preFix;
        this.postFix = postFix == null ? "" : postFix;
        data = new LinkedBlockingDeque<>(captureSize);
    }

    private double getMin() {
        if (data == null || data.size() == 0) return 0;
        return data.stream().min((d1, d2) -> Double.compare(d1, d2)).get();
    }

    private double getAverage() {
        if (data == null || data.size() == 0) return 0;
        return data.stream().mapToDouble((d) -> d).average().getAsDouble();
    }

    private double getMax() {
        if (data == null || data.size() == 0) return 0;
        return data.stream().max((d1, d2) -> Double.compare(d1, d2)).get();
    }

    public void draw(GraphicsContext context, int x, int y) {
        double dataBoxWidth = BOX_WIDTH * DATA_WIDTH_RELATIVE;
        double dataBoxHeight = BOX_HEIGHT * DATA_HEIGHT_RELATIVE;
        double dataBoxMarginHorizontal = (BOX_WIDTH - dataBoxWidth) / 2;
        double dataBoxMarginVertical = (BOX_HEIGHT - dataBoxHeight) / 6;
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
        context.setTextAlign(TextAlignment.LEFT);
        context.setGlobalAlpha(1);
        context.setFont(new Font(10));
        context.setFill(TEXT_NAME_COLOR);
        context.fillText(name, x + 5, y + 14);

        // Min, Avg, Max.
        context.setTextAlign(TextAlignment.LEFT);
        context.setFill(TEXT_MINIMUM_COLOR);
        context.fillText(String.format("%sMIN: %.1f%s", preFix, getMin(), postFix), x + dataBoxMarginHorizontal, y + 26);

        context.setTextAlign(TextAlignment.CENTER);
        context.setFill(TEXT_AVERAGE_COLOR);
        context.fillText(String.format("%sAVG: %.1f%s", preFix, getAverage(), postFix), x + BOX_WIDTH / 2, y + 26);

        context.setFill(TEXT_MAXIMUM_COLOR);
        context.setTextAlign(TextAlignment.RIGHT);
        context.fillText(String.format("%sMAX: %.1f%s", preFix, getMax(), postFix), x + BOX_WIDTH - dataBoxMarginHorizontal, y + 26);
        context.setTextAlign(TextAlignment.LEFT);

        // Background data.
        context.setGlobalAlpha(1);
        context.setFill(DATA_BACKGROUND_COLOR);
        context.fillRect(x + dataBoxMarginHorizontal, y + dataBoxMarginVertical * 5, dataBoxWidth, dataBoxHeight);

        // Vertical data bars.
        context.setGlobalAlpha(0.7);
        context.setFill(DATA_BAR_COLOR);
        currentMaxValue = getMax() == 0 ? 1 : getMax();

        // Get array from ArrayDequeue<>.
        Double[] dataArray = new Double[data.size()];
        synchronized (this) {
            data.toArray(dataArray);
        }

        double barWidth = dataBoxWidth / dataSize - DATA_BAR_SPACE;
        for (int i = 0; i < dataArray.length; i++) {
            double barLength = BOX_HEIGHT * DATA_HEIGHT_RELATIVE / currentMaxValue * dataArray[i];
            context.fillRect(x + (i * (barWidth + DATA_BAR_SPACE)) + dataBoxMarginHorizontal + OVERLAY_OFFSET.getX(), y + BOX_HEIGHT - barLength - dataBoxMarginVertical + OVERLAY_OFFSET.getY(), barWidth, barLength);
        }
    }

    public void addValue(double value) {
        if (data.size() == dataSize) {
            data.pop();
        }
        data.addLast(value);
    }
}
