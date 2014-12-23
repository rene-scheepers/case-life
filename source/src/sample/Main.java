package sample;

import classes.enumerations.LocationType;
import classes.world.Location;
import classes.world.World;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.awt.*;
import java.util.ArrayList;

public class Main extends Application {

    private Canvas canvas;
    private Simulator simulator;
    private int width;
    private int height;
    private World world;

    public Main() {
        this.world = new World("C:\\Users\\Rene\\Desktop\\kut.bmp");


        this.width = 1000;
        this.height = 1000;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane root = new Pane();

        canvas = new Canvas(width, height);
        this.simulator = new Simulator(world, canvas);
        Scene scene = new Scene(root);

        GraphicsContext context = canvas.getGraphicsContext2D();
        //draw(context);
        root.getChildren().add(canvas);

        //Timeline line = new Timeline(1000);

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(scene);
        primaryStage.show();

        this.simulator.setSpeed(1000);
        this.simulator.start();
    }




    public static void main(String[] args) {
        launch(args);
    }
}
