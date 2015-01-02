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
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class Simulator {

    private Canvas canvas;
    private World world;
    private Timeline timeline;
    private int currentTurn = 0;
    private int speed;
    Thread thread;

    public Simulator(World world, Canvas canvas) {
        this.world = world;
        this.canvas = canvas;
        speed = 1;
        thread = new Thread();
        timeline = new Timeline();
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Animation.Status getStatus() {
        return timeline.getStatus();
    }

    public void play() {
        timeline.setRate(speed);

        KeyFrame frame = new KeyFrame(Duration.seconds(1), ev -> {
            world.simulate();
            currentTurn++;
            System.out.println(currentTurn);
            draw(canvas);
        });

        timeline.getKeyFrames().add(currentTurn, frame);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
//        timeline.setRate(speed);
//
//        while(true) {
//            long time =  System.currentTimeMillis();
//            long nano = System.nanoTime();
//            world.simulate();
//            currentTurn++;
//            draw(canvas);
//
//            time = System.currentTimeMillis() - time;
//            nano = System.nanoTime() - nano;
//            try {
//                Thread.sleep(16 - time);
//            } catch(Exception ex) {
//
//            }
//
//        }
    }

    public void pause() {
        timeline.pause();
    }

    public void stop() {
        thread.stop();
        timeline.stop(); currentTurn = 0;
    }

    private void draw(Canvas canvas) {
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        context.setFill(Color.BLACK);
        context.fillText(String.valueOf(currentTurn), 2, 12);

        double drawWidth = canvas.getWidth() / world.getWidth();
        if (drawWidth < 1) {
            drawWidth = 1;
        }

        double drawHeight = canvas.getHeight() / world.getHeight();
        if (drawHeight < 1) {
            drawHeight = 1;
        }

        for (Life life : world.getLife()) {
                life.draw(context, drawWidth, drawHeight);
        }
    }

}
