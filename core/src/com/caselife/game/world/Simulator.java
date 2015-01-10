package com.caselife.game.world;

import com.caselife.game.debugging.DebugGraph;
import com.caselife.game.debugging.SimDebugger;
import com.caselife.game.world.World;
import javafx.scene.canvas.GraphicsContext;

public class Simulator extends Thread {

    private GraphicsContext uiContext;


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

    public Simulator(World world) {
        this.world = world;

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

    public void input() {
        // Do your input.
    }

    /**
     * Update logic being executed each frame.
     */
    private void simulate() {
        long perfStart = System.nanoTime();
        input();
        world.simulate();
        perfomanceSimulateMs = (System.nanoTime() - perfStart) / 1000000.0;
        SimDebugger.<DebugGraph>getDebugObject("SIMULATE").addValue(perfomanceSimulateMs);
    }

    /**
     * Draw logic being executed each frame.
     */
    private void draw() {
//        Platform.runLater(() -> {
//            /// Debug UI.
//            uiContext.clearRect(0, 0, width, height);
//            SimDebugger.draw(uiContext);
//
//            // Measure performance.
//            long perfStart = System.nanoTime();
//
//
//            // UI.
//            if (isPaused) {
//                uiContext.save();
//                uiContext.setEffect(new DropShadow(5, 2, 2, Color.BLACK));
//                uiContext.setTextAlign(TextAlignment.CENTER);
//                uiContext.setGlobalAlpha(0.33);
//                uiContext.setFont(new Font(36));
//                uiContext.fillText("PAUSED", width / 2, height / 2);
//                uiContext.restore();
//            }
//
//
//            perfomanceDrawMs = (System.nanoTime() - perfStart) / 1000000.0;
//            SimDebugger.<DebugGraph>getDebugObject("DRAW").addValue(perfomanceDrawMs);
//        });
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


}
