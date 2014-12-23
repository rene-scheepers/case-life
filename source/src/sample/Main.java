package sample;

import classes.enumerations.LocationType;
import classes.world.Location;
import classes.world.World;
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

    private int width;
    private int height;
    private World world;

    public Main() {
        this.width = 1000;
        this.height = 1000;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane root = new Pane();
        Scene scene = new Scene(root);

        world = new World("C:\\Users\\Rene\\Desktop\\kut.bmp");

        Canvas canvas = new Canvas(width, height);
        GraphicsContext context = canvas.getGraphicsContext2D();
        draw(context);
        root.getChildren().add(canvas);


        primaryStage.setTitle("Hello World");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void draw(GraphicsContext context) {
        int drawWidth = width / world.getWidth();
        if (drawWidth < 1) {
            drawWidth = 1;
        }

        int drawHeight = height / world.getHeight();
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


    public static void main(String[] args) {
        launch(args);
    }
}
