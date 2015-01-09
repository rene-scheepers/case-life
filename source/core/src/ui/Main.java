//package ui;
//
//import classes.Simulator;
//import classes.debugging.SimDebugger;
//import classes.world.World;
//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.scene.canvas.Canvas;
//import javafx.scene.layout.*;
//import javafx.stage.FileChooser;
//import javafx.stage.Stage;
//
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//
//public class Main extends Application {
//
//    private Simulator simulator;
//    private int width;
//    private int height;
//    private World world;
//    private Stage primaryStage;
//    private Scene scene;
//
//    private BufferedImage selectedMap;
//    private Pane root;
//    private Canvas worldCanvas;
//    private Canvas uiCanvas;
//
//    public Main() {
//        this.width = 1000;
//        this.height = 1000;
//    }
//
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        this.primaryStage = primaryStage;
//        primaryStage.setOnCloseRequest((ev) -> {
//            if (simulator == null) return;
//            simulator.interrupt();
//        });
//
//        // Initialize window.
//        root = new Pane();
//
//        Scene scene = new Scene(root);
//        this.scene = scene;
//        primaryStage.setScene(scene);
//        primaryStage.setTitle("Ecosystem simulation");
//
//        restart();
//        primaryStage.show();
//    }
//
//    public void restart() {
//        if (simulator != null) {
//            simulator.interrupt();
//            try {
//                simulator.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//        SimDebugger.reset();
//        try {
//            if (selectedMap == null)
//                selectedMap = selectMap();
//            world = World.instantiateWorldFromImage(selectedMap);
//
//            if (simulator != null) {
//                root.getChildren().removeAll(worldCanvas, uiCanvas);
//            }
//
//            worldCanvas = new Canvas(width, height);
//            uiCanvas = new Canvas(width, height);
//
//            root.getChildren().add(worldCanvas);
//            root.getChildren().add(uiCanvas);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        worldCanvas.getGraphicsContext2D().scale(width / world.getWidth(), height / world.getHeight());
//        uiCanvas.getGraphicsContext2D().clearRect(0, 0, uiCanvas.getWidth(), uiCanvas.getHeight());
//
//        // Draw world background.
//        WorldRenderer renderer = new WorldRenderer(worldCanvas, world);
//
//        // Run game.
//        simulator = new Simulator(world, renderer, uiCanvas.getGraphicsContext2D(), width, height);
//        simulator.registerKeys(this);
//        simulator.setSpeed(60);
//        simulator.start();
//    }
//
//    public BufferedImage selectMap() {
//        File initialDirectory = new File("resources/maps/");
//        BufferedImage image = null;
//        while (image == null) {
//            FileChooser selector = new FileChooser();
//            selector.setInitialDirectory(initialDirectory);
//            selector.setTitle("Select simulation map");
//            File file = selector.showOpenDialog(primaryStage);
//
//            if (file == null || !file.exists()) continue;
//            try {
//                image = ImageIO.read(file);
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }
//        return image;
//    }
//
//    public void setSelectedMap(BufferedImage image) {
//        selectedMap = image;
//    }
//
//    public Stage getPrimaryStage() {
//        return primaryStage;
//    }
//
//    public Scene getScene() {
//        return scene;
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
