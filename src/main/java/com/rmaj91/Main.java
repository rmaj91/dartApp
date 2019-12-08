package com.rmaj91;

import com.rmaj91.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * JavaFX App main class
 */
public class Main extends Application {

    public static Stage stage = null;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            MainController.setStage(primaryStage);
            BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("Main.fxml"));
            Scene scene = new Scene(root);
//            scene.getStylesheets().add(getClass().getResource("css/main.css").toExternalForm());
//            primaryStage.getIcons().add(new Image("images/dartBoard.png"));
            stage=primaryStage;
            stage.setMinWidth(1000);
            stage.setMinHeight(600);



            primaryStage.setTitle("Dart Maju v.1.0.");
            primaryStage.setScene(scene);
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}