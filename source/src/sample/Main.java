package sample;

import classes.Exceptions.LocationAlreadyOccupiedException;
import classes.enumerations.Digestion;
import classes.enumerations.LocationType;
import classes.life.Animal;
import classes.life.Genetics;
import classes.life.Life;
import classes.life.Plant;
import classes.world.Node;
import classes.world.World;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Main extends Application {

    private Simulator simulator;
    private int width;
    private int height;
    private World world;

    public Main() {
        File file = new File("resources/maps/Darwin map(empty).png");
        BufferedImage image;
        try {
            image = ImageIO.read(file);
        } catch (IOException exception) {
            return;
        }

        world = World.instantiateWorldFromImage(image);

        this.width = 2000;
        this.height = 2000;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane root = new Pane();

        Canvas worldCanvas = new Canvas(width, height);
        Canvas lifeCanvas = new Canvas(width, height);
        Canvas logCanvas = new Canvas(width, height);

        simulator = new Simulator(world, lifeCanvas);
        Scene scene = new Scene(root);

        root.getChildren().add(worldCanvas);
        root.getChildren().add(lifeCanvas);
        root.getChildren().add(logCanvas);

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(scene);
        primaryStage.show();

        world.draw(worldCanvas.getGraphicsContext2D(), width, height);

        this.simulator.setSpeed(5);
        this.simulator.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
