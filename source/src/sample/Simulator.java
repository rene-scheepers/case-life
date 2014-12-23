package sample;

import classes.enumerations.LocationType;
import classes.world.Location;
import classes.world.World;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Simulator {

    private Canvas canvas;
    private World world;
    private Timer timer = new Timer();
    private int speed;

    private boolean running;

    public Simulator(World world, Canvas canvas) {
        this.world = world;
        this.speed = 1000;
        this.canvas = canvas;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean isRunning() {
        return running;
    }

    public void start() {
        if (timer == null) {
            timer = new Timer();
        }

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                world.simulate();
                draw(canvas.getGraphicsContext2D());
            }
        }, 0, speed);

        running = true;
    }

    public boolean stop() {
        if (timer == null) {
            return false;
        }

        timer.cancel();
        running = false;
        return true;
    }

    public void draw(GraphicsContext context) {
        int drawWidth = (int)canvas.getWidth() / world.getWidth();
        if (drawWidth < 1) {
            drawWidth = 1;
        }

        int drawHeight = (int)canvas.getHeight() / world.getHeight();
        if (drawHeight < 1) {
            drawHeight = 1;
        }

        ArrayList<Location> locations = world.getLocations();

        for (Location location : locations) {
            if (location.getType().equals(LocationType.Land)) {
                context.setFill(Color.WHITE);
            } else if (location.getType().equals(LocationType.Obstacle)) {
                context.setFill(Color.BLACK);
            } else {
                context.setFill(Color.BLUE);
            }
            context.fillRect(location.getX() * drawWidth, location.getY() * drawHeight, drawWidth, drawHeight);
        }
    }

}
