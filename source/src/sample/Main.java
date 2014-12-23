package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {

    private int width;
    private int height;

    public Main() {
        this.width = 100;
        this.height = 100;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        GridPane root = new GridPane();
        Scene scene = new Scene(root, 1000, 1000);


        for (int x = 0; x < width; x++) {
            for (int y = 0; x < height; y++) {
                Label pane = new Label("wo");
                root.add(pane, x, y);
            }
        }

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
