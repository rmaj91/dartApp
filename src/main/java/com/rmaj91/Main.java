package com.rmaj91;

import com.rmaj91.domain.Game01;
import com.rmaj91.domain.Player;
import com.rmaj91.repository.GamesRepositoryImpl;
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
 * JavaFX App
 */
public class Main extends Application {

    public static Stage stage = null;
    public static GamesRepositoryImpl gamesRepositotyImpl = new GamesRepositoryImpl();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("Main.fxml"));
            Scene scene = new Scene(root);
            //scene.getStylesheets().add(getClass().getResource("css/main.css").toExternalForm());
            stage = primaryStage;
            Game01.setGamesRepositoryImpl(gamesRepositotyImpl);
            Player.gamesRepositoryImpl = gamesRepositotyImpl;

            primaryStage.getIcons().add(new Image("images/dartBoard.png"));
            primaryStage.setTitle("Dart Maju v.1.0.");
            primaryStage.setScene(scene);
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setOnCloseRequest((e)->{closeApplication(e);});
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }



    public static void closeApplication(WindowEvent event) {
        Alert a = new Alert(Alert.AlertType.NONE, "Are you sure you want to quit?", ButtonType.YES,
                ButtonType.NO);
        Optional<ButtonType> confirm = a.showAndWait();
        if (confirm.isPresent() && confirm.get() == ButtonType.YES) {
            Main.stage.close();
        }
        else if (event !=null)
            event.consume();
    }

//    private static Scene scene;
//
//    @Override
//    public void start(Stage stage) throws IOException {
//        scene = new Scene(loadFXML("primary"));
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    static void setRoot(String fxml) throws IOException {
//        scene.setRoot(loadFXML(fxml));
//    }
//
//    private static Parent loadFXML(String fxml) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml + ".fxml"));
//        return fxmlLoader.load();
//    }
//
//    public static void main(String[] args) {
//        launch();
//    }

}