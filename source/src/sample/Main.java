package sample;

import classes.Exceptions.LocationAlreadyOccupiedException;
import classes.enumerations.Digestion;
import classes.enumerations.LocationType;
import classes.life.Animal;
import classes.life.Genetics;
import classes.world.Location;
import classes.world.World;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Main extends Application {

    private Simulator simulator;
    private int width;
    private int height;
    private World world;

    public Main() {
        this.world = new World("C:\\Users\\Rene\\Desktop\\kut.bmp");

        ArrayList<Location> locations = (ArrayList<Location>)world.getLocations().clone();
        Genetics genetics = new Genetics(Digestion.Carnivore, 4, 50, 100, 100, 20, 20, 20);

        Collections.shuffle(locations);

        Random random = new Random();
        for(int i = 0; i < 50; i++) {
            for(Location location : locations) {
                if (random.nextInt(10) > 8) {
                    try {
                        Animal animal = new Animal(genetics, location);

                    world.addObject(animal);
                    } catch(LocationAlreadyOccupiedException exception) {

                    }
                    break;
                }
            }
        }


        this.width = 2000;
        this.height = 2000;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane root = new Pane();

        Canvas worldCanvas = new Canvas(width, height);
        Canvas lifeCanvas = new Canvas(width, height);
        simulator = new Simulator(world, lifeCanvas);
        Scene scene = new Scene(root);

        root.getChildren().add(worldCanvas);
        root.getChildren().add(lifeCanvas);

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(scene);
        primaryStage.show();

        draw(worldCanvas);

        this.simulator.setSpeed(5);
        this.simulator.play();
    }

    public void draw(Canvas canvas) {
        GraphicsContext context = canvas.getGraphicsContext2D();

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
            if (location.getLocationType().equals(LocationType.Land)) {
                context.setFill(Color.WHITE);
            } else if (location.getLocationType().equals(LocationType.Obstacle)) {
                context.setFill(Color.BLACK);
            } else {
                context.setFill(Color.BLUE);
            }

            context.fillRect(location.getX() * drawWidth, location.getY() * drawHeight, drawWidth, drawHeight);
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
