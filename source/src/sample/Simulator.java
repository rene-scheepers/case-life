package sample;

import classes.enumerations.Digestion;
import classes.life.Animal;
import classes.life.Life;
import classes.life.Plant;
import classes.world.Node;
import classes.world.World;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Simulator extends Thread {

    private GraphicsContext worldContext;
    private GraphicsContext lifeContext;

    private World world;
    private boolean isPlaying;
    private int currentTurn = 0;
    private int width;
    private int height;

    /**
     * Desired speed in FPS.
     */
    private double speed;

    public Simulator(World world, GraphicsContext lifeContext, int width, int height) {
        this.world = world;
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


        isPlaying = true;
        super.start();
    }

    /**
     * Game loop (running each frame).
     */
    @Override
    public void run() {
        while(isPlaying && !isInterrupted()) {
            long time =  System.currentTimeMillis();

            world.simulate();
            Platform.runLater(() -> draw(lifeContext));
            currentTurn++;

            time = System.currentTimeMillis() - time;
            try {
                long sleepTime = (long) (1000 / speed - time);
                sleep(sleepTime < 0 ? 0 : sleepTime);
            } catch(Exception ex) {
                System.out.println(ex.toString());
            }

        }
    }

//    public void play() {
//        timeline.setRate(speed);
//
//        KeyFrame frame = new KeyFrame(Duration.seconds(1), ev -> {
//            world.simulate();
//            currentTurn++;
//            System.out.println(currentTurn);
//            draw(canvas);
//        });
//
//        timeline.getKeyFrames().add(frame);
//        timeline.setCycleCount(Animation.INDEFINITE);
//        timeline.play();
//
//
//    }

    public void pause() {
        isPlaying = false;
    }

    @Override
    public void interrupt() {
        isPlaying = false;
        super.interrupt();
    }

    private void draw(GraphicsContext context) {
        context.clearRect(0, 0, width, height);

        context.setFill(Color.BLACK);
        context.fillText(String.valueOf(currentTurn), 2, 12);

        for (Life life : world.getLife()) {
            life.draw(context);
        }
    }

}
