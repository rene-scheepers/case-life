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
        File file = new File("resources/maps/small.png");
        BufferedImage image;
        try {
            image = ImageIO.read(file);
        } catch (IOException exception) {
            return;
        }

        world = generateWorldFromImage(image);

        this.width = 1000;
        this.height = 1000;
    }

    public World generateWorldFromImage(BufferedImage image) {
        width = image.getWidth();
        height = image.getHeight();
        Node[][] nodes = new Node[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                java.awt.Color color = new java.awt.Color(image.getRGB(x, y));
                String hex = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());

                LocationType type;
                if (hex.equals("#000000")) {
                    type = LocationType.Water;
                } else if (hex.equals("#ed1c1c")) {
                    type = LocationType.Obstacle;
                } else {
                    type = LocationType.Land;
                }

                nodes[x][y] = new Node(x, y, type);
            }
        }

        World world = new World(nodes);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                java.awt.Color color = new java.awt.Color(image.getRGB(x, y));
                String hex = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());

                Life life = null;
                if (hex.equals("#00ff00")) {
                    life = new Plant(world, 100);
                } else if (hex.equals("#ff6a00")) {
                    life = new Animal(world, new Genetics(Digestion.Carnivore, 4, 50, 100, 100, 20, 20, 20));
                } else if (hex.equals("#0026ff")) {
                    life = new Animal(world, new Genetics(Digestion.Omnivore, 4, 50, 100, 100, 20, 20, 20));
                } else if (hex.equals("#ff00ff")) {
                    life = new Animal(world, new Genetics(Digestion.Herbivore, 4, 50, 100, 100, 20, 20, 20));
                }

                if (life != null) {
                    try {
                        world.addLife(life, nodes[x][y]);
                    }
                     catch (LocationAlreadyOccupiedException exception) {

                     }
                }
            }
        }

        return world;
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

        int drawWidth = (int) canvas.getWidth() / world.getWidth();
        if (drawWidth < 1) {
            drawWidth = 1;
        }

        int drawHeight = (int) canvas.getHeight() / world.getHeight();
        if (drawHeight < 1) {
            drawHeight = 1;
        }

        Node[][] nodes = world.getNodes();

        for (int x = 0; x < nodes.length; x++) {
            for (int y = 0; y < nodes[x].length; y++) {
                Node node = nodes[x][y];

                if (node.getLocationType().equals(LocationType.Land)) {
                    context.setFill(Color.WHITE);
                } else if (node.getLocationType().equals(LocationType.Obstacle)) {
                    context.setFill(Color.LIGHTGRAY);
                } else {
                    context.setFill(Color.LIGHTBLUE);
                }

                context.fillRect(node.getX() * drawWidth, node.getY() * drawHeight, drawWidth, drawHeight);
            }
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
