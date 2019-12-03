package com.rmaj91;

import com.rmaj91.controller.MainController;
import com.rmaj91.domain.Game01;
import com.rmaj91.domain.Player;
import com.rmaj91.repository.GamesRepositoryImpl;
import com.rmaj91.utility.SoundPlayer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.util.Optional;

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

            AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("Main.fxml"));
            Scene scene = new Scene(root);
//            scene.getStylesheets().add(getClass().getResource("css/main.css").toExternalForm());
            primaryStage.getIcons().add(new Image("images/dartBoard.png"));
            primaryStage.setTitle("Dart Maju v.1.0.");
            primaryStage.setScene(scene);
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}