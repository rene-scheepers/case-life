package classes;

import classes.debugging.DebugGraph;
import classes.debugging.SimDebugger;
import classes.life.Animal;
import classes.life.Life;
import classes.world.Node;
import classes.world.World;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import ui.Main;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Simulator extends Thread {

    private GraphicsContext uiContext;
    private GraphicsContext worldContext;

    private Life following;
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

    public Simulator(World world, GraphicsContext worldContext, GraphicsContext uiContext, int width, int height) {
        this.world = world;
        this.worldContext = worldContext;
        this.uiContext = uiContext;
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
        SimDebugger.addDebugValue("Frames", () -> String.valueOf(currentTurn));
        SimDebugger.addDebugValue("Target FPS", () -> String.valueOf(speed));

        SimDebugger.addDebugGraph("FPS", 30);
        SimDebugger.addStatistic(new DebugGraph("SIMULATE", 150, null, " (ms)"));
        SimDebugger.addStatistic(new DebugGraph("DRAW", 150, null, " (ms)"));

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
        long overSleptTime = 0;

        while(isPlaying && !isInterrupted()) {
            long time =  System.currentTimeMillis();

            if (!isPaused) simulate();
            draw();
            if (!isPaused) currentTurn++;

            currentFPS++;
            if (System.currentTimeMillis() - 1000 >= startFPSCountTime) {
                SimDebugger.<DebugGraph>getDebugObject("FPS").addValue(currentFPS);
                startFPSCountTime = System.currentTimeMillis();
                currentFPS = 0;
            }

            time = System.currentTimeMillis() - time;
            try {
                long sleepTime = (long) (1000 / speed - time);
                sleepTime = sleepTime < 0 ? 0 : sleepTime;

                long actualSleep = System.currentTimeMillis();
                // Going to fast. Needs to sleep.
                if (sleepTime + overSleptTime > 0)
                    sleep(sleepTime + overSleptTime);

                // Adding up under- oversleep for next frame.
                actualSleep = System.currentTimeMillis() - actualSleep;
                overSleptTime += sleepTime - actualSleep;
            } catch(Exception ex) {
                System.out.println(ex.toString());
            }
        }
    }

    /**
     * Update logic being executed each frame.
     */
    private void simulate() {
        long perfStart = System.nanoTime();
        world.simulate();
        perfomanceSimulateMs = (System.nanoTime() - perfStart) / 1000000.0;
        SimDebugger.<DebugGraph>getDebugObject("SIMULATE").addValue(perfomanceSimulateMs);
    }

    /**
     * Draw logic being executed each frame.
     */
    private void draw() {
        Platform.runLater(() -> {
            /// Debug UI.
            uiContext.clearRect(0, 0, width, height);
            SimDebugger.draw(uiContext);

            // Measure performance.
            long perfStart = System.nanoTime();

            worldContext.clearRect(0, 0, width, height);

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

            List<Life> lives = world.getLives();
            List<Life> animals = lives.stream().filter(x -> x.getClass() == Animal.class).collect(Collectors.toList());
            if (following == null || !animals.contains(following)) {
                Random rand = new Random();
                following = animals.get(rand.nextInt(animals.size()));
            }

            Node center = following.getNode();
            int offsetX = center.getX() - world.getWidth() / 2;
            int offsetY = center.getY() - world.getHeight() / 2;

            world.draw(worldContext, offsetX, offsetY);

            perfomanceDrawMs = (System.nanoTime() - perfStart) / 1000000.0;
            SimDebugger.<DebugGraph>getDebugObject("DRAW").addValue(perfomanceDrawMs);
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
                case R:
                    if (key.isShiftDown())
                        main.setSelectedMap(null);
                    main.restart();
                    break;
                case D:
                    SimDebugger.setVisible(!SimDebugger.isVisible());
                    break;
            }
        });
    }
}
