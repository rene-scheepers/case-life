package com.caselife.logic;

import com.caselife.logic.world.World;
import javafx.scene.canvas.GraphicsContext;

public class Simulator extends Thread {
    private World world;
    private boolean isPlaying;
    private boolean isPaused;

    private int currentTurn = 0;

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

    public int getCurrentTurn() {
        return currentTurn;
    }

    /**
     * On first start (load content and initialize).
     */
    @Override
    public synchronized void start() {
        isPlaying = true;
        super.start();
    }

    /**
     * Game loop (running each frame).
     */
    @Override
    public void run() {
        long startFPSCountTime = System.currentTimeMillis();
        long overSleptTime = 0;

        while(isPlaying && !isInterrupted()) {
            long time =  System.currentTimeMillis();

            if (!isPaused) {
                simulate();
                currentTurn++;
            }

            if (System.currentTimeMillis() - 1000 >= startFPSCountTime) {
                startFPSCountTime = System.currentTimeMillis();
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
        world.simulate();
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
