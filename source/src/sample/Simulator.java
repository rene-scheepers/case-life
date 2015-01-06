package sample;

import classes.debugging.DebugGraph;
import classes.debugging.SimDebugger;
import classes.life.Life;
import classes.world.World;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class Simulator extends Thread {

    private GraphicsContext uiContext;
    private GraphicsContext lifeContext;

    private World world;
    private boolean isPlaying;
    private boolean isPaused;

    private int currentTurn = 0;
    private int width;
    private int height;

    private double perfomanceSimulateMs;
    private double totalSimulateMs;
    private double performanceAverageSimulateMs;
    private double perfomanceDrawMs;

    /**
     * Desired speed in FPS.
     */
    private double speed;

    public Simulator(World world, GraphicsContext lifeContext, GraphicsContext uiContext, int width, int height) {
        this.world = world;
        this.uiContext = uiContext;
        this.lifeContext = lifeContext;
        this.width = width;
        this.height = height;

        this.speed = 60;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * On first start (load content and initialize).
     */
    @Override
    public synchronized void start() {
        // Load content here.
        // Load debugger information.
        SimDebugger.addDebugValue("Simulate elapsed", () -> String.valueOf(perfomanceSimulateMs) + " (ms)");
        SimDebugger.addDebugValue("Average simulate", () -> String.valueOf(performanceAverageSimulateMs) + " (ms)");
        SimDebugger.addDebugValue("Draw elapsed", () -> String.valueOf(perfomanceDrawMs) + " (ms)");
        SimDebugger.addDebugValue("Frames", () -> String.valueOf(currentTurn));
        SimDebugger.addDebugValue("Target FPS", () -> String.valueOf(speed));

        SimDebugger.addDebugGraph("FPS_");
        DebugGraph graph = SimDebugger.getDebugObject("FPS_");

        isPlaying = true;
        super.start();
    }

    /**
     * Game loop (running each frame).
     */
    @Override
    public void run() {
        // FPS counter.
        int currentFPS = 0;
        long startFPSCountTime = System.currentTimeMillis();

        while(isPlaying && !isInterrupted()) {
            long time =  System.currentTimeMillis();

            if (!isPaused) simulate();
            draw(lifeContext);
            if (!isPaused) currentTurn++;

            currentFPS++;
            if (System.currentTimeMillis() - 1000 >= startFPSCountTime) {
                SimDebugger.setDebugValue("FPS", String.valueOf(currentFPS));
                SimDebugger.<DebugGraph>getDebugObject("FPS_").addValue(currentFPS);
                startFPSCountTime = System.currentTimeMillis();
                currentFPS = 0;
            }

            time = System.currentTimeMillis() - time;
            try {
                long sleepTime = (long) (1000 / speed - time);
                sleep(sleepTime < 0 ? 0 : sleepTime);
            } catch(Exception ex) {
                System.out.println(ex.toString());
            }
        }
    }

    /**
     * Update logic being executed each frame.
     */
    private void simulate() {
        long perfStart = System.currentTimeMillis();
        world.simulate();
        perfomanceSimulateMs = System.currentTimeMillis() - perfStart;

        totalSimulateMs += perfomanceSimulateMs;
        performanceAverageSimulateMs = Math.round(totalSimulateMs / currentTurn * 10) / 10;
    }

    /**
     * Draw logic being executed each frame.
     * @param context Context used for drawing the game.
     */
    private void draw(GraphicsContext context) {
        Platform.runLater(() -> {
            long perfStart = System.currentTimeMillis();

            context.clearRect(0, 0, width, height);
            uiContext.clearRect(0, 0, width, height);

            /// Debug UI.
            uiContext.setFill(Color.BLACK);
            SimDebugger.draw(uiContext);

            // UI.
            if (isPaused) {
                uiContext.save();
                uiContext.setEffect(new DropShadow(5, 2, 2, Color.BLACK));
                uiContext.setTextAlign(TextAlignment.CENTER);
                uiContext.setGlobalAlpha(0.33);
                uiContext.setFont(new Font(36));
                uiContext.fillText("PAUSED", width / 2, height / 2);
                uiContext.restore();
            }

            /// Draw game mechanics.
            for (Object life : world.getLives().toArray()) {
                ((Life)life).draw(context);
            }

            perfomanceDrawMs = System.currentTimeMillis() - perfStart;
        });
    }

    public void pause() { isPlaying = false; }

    public void unPause() { isPaused = true; }

    public void togglePause() { isPaused = !isPaused; }

    /**
     * Stops execution of the game thread.
     */
    @Override
    public void interrupt() {
        isPlaying = false;
        super.interrupt();
    }

    public void registerKeys(Main main) {
        main.getScene().setOnKeyPressed((key) -> {
            switch (key.getCode()) {
                case P:
                    // Pause game.
                    togglePause();
                    break;
                case EQUALS:
                    // Increase FPS cap.
                    if (key.isShiftDown()) speed += 5;
                    else speed++;
                    break;
                case MINUS:
                    // Decrease FPS cap.
                    if (key.isShiftDown()) {
                        if (speed <= 5) speed = 1;
                        else speed -= 5;
                    } else {
                        if (speed <= 1) break;
                        speed--;
                    }
                    break;
                case ENTER:
                    // Toggle fullscreen.
                    if (!key.isAltDown()) break;
                    main.getPrimaryStage().setFullScreen(!main.getPrimaryStage().isFullScreen());
                    break;
            }
        });
    }
}
