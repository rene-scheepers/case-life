package sample;

import classes.world.World;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.PopupBuilder;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;

public class Main extends Application {

    private Simulator simulator;
    private int width;
    private int height;
    private World world;
    private Stage primaryStage;
    private Scene scene;

    public Main() {


        this.width = 1000;
        this.height = 1000;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        // Initialize window.
        Pane root = new Pane();

        Canvas worldCanvas = new Canvas(width, height);
        Canvas lifeCanvas = new Canvas(width, height);
        Canvas uiCanvas = new Canvas(width, height);

        Scene scene = new Scene(root);
        this.scene = scene;

        root.getChildren().add(worldCanvas);
        root.getChildren().add(lifeCanvas);
        root.getChildren().add(uiCanvas);

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(scene);


        try {
            world = World.instantiateWorldFromImage(selectMap());
        } catch (Exception ex) {
            return;
        }
        primaryStage.show();

        worldCanvas.getGraphicsContext2D().scale(width / world.getWidth(), height / world.getHeight());
        lifeCanvas.getGraphicsContext2D().scale(width / world.getWidth(), height / world.getHeight());

        // Draw world background.
        world.draw(worldCanvas.getGraphicsContext2D());

        // Run game.
        simulator = new Simulator(world, lifeCanvas.getGraphicsContext2D(), uiCanvas.getGraphicsContext2D(), width, height);
        simulator.registerKeys(this);
        simulator.setSpeed(60);
        simulator.start();
        primaryStage.setOnCloseRequest((ev) -> simulator.interrupt());
    }

    public BufferedImage selectMap() {
        File initialDirectory = new File("resources/maps/");
        BufferedImage image = null;
        while (image == null) {
            FileChooser selector = new FileChooser();
            selector.setInitialDirectory(initialDirectory);
            selector.setTitle("Select simulation map");
            File file = selector.showOpenDialog(primaryStage);
            if (file != null) {
                initialDirectory = file.getParentFile();
            }

            try {
                image = ImageIO.read(file);
            } catch (IOException exception) {
            }
        }
        return image;
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
