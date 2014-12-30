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

    public Simulator(World world, Canvas canvas) {
        this.world = world;
        this.canvas = canvas;
        speed = 1;
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
            draw(canvas);
        });

        timeline.getKeyFrames().add(currentTurn, frame);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void pause() {
        timeline.pause();
    }

    public void stop() {
        timeline.stop(); currentTurn = 0;
    }

    private void draw(Canvas canvas) {
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        context.setFill(Color.BLACK);
        context.fillText(String.valueOf(currentTurn), 2, 12);

        int drawWidth = (int) canvas.getWidth() / world.getWidth();
        if (drawWidth < 1) {
            drawWidth = 1;
        }

        int drawHeight = (int) canvas.getHeight() / world.getHeight();
        if (drawHeight < 1) {
            drawHeight = 1;
        }

        for (Life life : world.getLife()) {
            Node node = null;
            Color color = Color.BLACK;
            if (life instanceof Animal) {
                Animal animal = (Animal)life;
                Digestion digestion = animal.getGenetics().getDigestion();
                node = animal.getNode();

                if (digestion.equals(Digestion.Carnivore)) {
                    color = Color.RED;
                } else if (digestion.equals(Digestion.Herbivore)) {
                    color = Color.BROWN;
                } else {
                    color = Color.YELLOW;
                }
            } else if (life instanceof Plant) {
                Plant plant = (Plant)life;
                node = plant.getNode();
                color = Color.GREEN;
            }

            if (node != null) {
                context.setFill(color);
                context.fillRect(node.getX() * drawWidth, node.getY() * drawHeight, drawWidth, drawHeight);
            }
        }
    }

}
