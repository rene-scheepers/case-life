package sample;

import classes.enumerations.Digestion;
import classes.life.Animal;
import classes.life.Plant;
import classes.world.Location;
import classes.world.World;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Simulator {

    private Canvas canvas;
    private World world;
    private Timeline timeline;
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
            draw(canvas);
        });
        timeline.getKeyFrames().add(frame);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void pause() {
        timeline.pause();
    }

    public void stop() {
        timeline.stop();
    }

    private void draw(Canvas canvas) {
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        int drawWidth = (int) canvas.getWidth() / world.getWidth();
        if (drawWidth < 1) {
            drawWidth = 1;
        }

        int drawHeight = (int) canvas.getHeight() / world.getHeight();
        if (drawHeight < 1) {
            drawHeight = 1;
        }

        for (Object object : world.getObjects()) {
            Location location = null;
            Color color = Color.BLACK;
            if (object instanceof Animal) {
                Animal animal = (Animal)object;
                Digestion digestion = animal.getGenetics().getDigestion();
                location = animal.getLocation();

                if (digestion.equals(Digestion.Carnivore)) {
                    color = Color.RED;
                } else if (digestion.equals(Digestion.Herbivore)) {
                    color = Color.BROWN;
                } else {
                    color = Color.YELLOW;
                }
            } else if (object instanceof Plant) {
                Plant plant = (Plant)object;
                location = plant.getLocation();
                color = Color.GREEN;
            }

            if (location != null) {
                context.setFill(color);
                context.fillRect(location.getX() * drawWidth, location.getY() * drawHeight, drawWidth, drawHeight);
            }
        }
    }

}
