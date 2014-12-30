package sample;

import classes.Exceptions.LocationAlreadyOccupiedException;
import classes.enumerations.Digestion;
import classes.enumerations.LocationType;
import classes.life.Animal;
import classes.life.Genetics;
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
        File file = new File("resources/maps/map.png");
        BufferedImage image;
        try {
            image = ImageIO.read(file);
        } catch (IOException exception) {
            return;
        }

        this.world = new World(image);

        Genetics genetics = new Genetics(Digestion.Carnivore, 4, 50, 100, 100, 20, 20, 20);

        Node[][] worldNodes = world.getNodes();
        ArrayList<Node> nodes = new ArrayList();
        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                Node node = worldNodes[x][y];
                if (node.getLocationType().equals(LocationType.Land)) {
                    nodes.add(node);
                }
            }
        }

        Collections.shuffle(nodes);

        Random random = new Random();
        for(int i = 0; i < 50; i++) {
            for(Node node : nodes) {
                if (random.nextInt(10) > 8) {
                    try {
                        Animal animal = new Animal(world, genetics);

                        world.addLife(animal, node);
                    } catch(LocationAlreadyOccupiedException exception) {

                    }
                    break;
                }
            }
        }


        this.width = 1000;
        this.height = 1000;
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

        this.simulator.setSpeed(1);
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
