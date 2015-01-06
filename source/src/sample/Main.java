package sample;

import classes.world.World;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main extends Application {

    private Simulator simulator;
    private int width;
    private int height;
    private World world;
    private Stage primaryStage;
    private Scene scene;

    public Main() {
        File file = new File("resources/maps/medium.png");
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
        this.primaryStage = primaryStage;

        // Initialize window.
        Pane root = new Pane();

        Canvas worldCanvas = new Canvas(width, height);
        Canvas lifeCanvas = new Canvas(width, height);
        Canvas uiCanvas = new Canvas(width, height);

        worldCanvas.getGraphicsContext2D().scale(width / world.getWidth(), height / world.getHeight());
        lifeCanvas.getGraphicsContext2D().scale(width / world.getWidth(), height / world.getHeight());

        Scene scene = new Scene(root);
        this.scene = scene;

        root.getChildren().add(worldCanvas);
        root.getChildren().add(lifeCanvas);
        root.getChildren().add(uiCanvas);

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Draw world background.
        world.draw(worldCanvas.getGraphicsContext2D());

        // Run game.
        simulator = new Simulator(world, lifeCanvas.getGraphicsContext2D(), uiCanvas.getGraphicsContext2D(), width, height);
        simulator.registerKeys(this);
        simulator.setSpeed(60);
        simulator.start();
        primaryStage.setOnCloseRequest((ev) -> simulator.interrupt());
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public Scene getScene() {
        return scene;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
