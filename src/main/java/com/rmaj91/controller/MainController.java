package com.rmaj91.controller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


import com.rmaj91.domain.*;
import com.rmaj91.repository.GamesRepositoryImpl;
import com.rmaj91.utility.SoundPlayer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainController implements Initializable {

    //==================================================================================================
    // Constants
    //==================================================================================================
    public static final int PLAYERS_NAME_MAX_LENGTH = 8;


    //==================================================================================================
    // Dependencies
    //==================================================================================================
    private GamesRepositoryImpl gamesRepository;
    private SoundPlayer soundPlayer;
    private static Stage stage;


    //==================================================================================================
    // JavaFX elements
    //==================================================================================================
    @FXML
    private WelcomeController welcomeController;
    @FXML
    private Game01Controller game01Controller;
    @FXML
    private CricketController cricketController;
    @FXML
    private MasterCricketController masterCricketController;
    @FXML
    private BoardController boardController;
    @FXML
    private StackPane mainTopPane;
    @FXML
    private ImageView volumeIcon;
    @FXML
    private Slider volumeSlider;
    @FXML
    private Polygon resizeButton;
    @FXML
    private StackPane mainStackPane;
    @FXML
    private AnchorPane rootPane;


    //==================================================================================================
    // Properties
    //==================================================================================================
    private double xOffSet = 0;
    private double yOffSet = 0;


    //==================================================================================================
    // Assesors
    //==================================================================================================
    public static void setStage(Stage stage) {
        MainController.stage = stage;
    }


    //==================================================================================================
    // Initializing
    //==================================================================================================
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        gamesRepository = new GamesRepositoryImpl();
        createSoundPlayer();

        injectGame01Dependencies();
        injectCricketDependencies();
        injectMasterCricketDependencies();
        injectPlayersDependencies();

        injectWelcomeControllerDependencies();
        injectGame01ControllerDependencies();
        injectCricketControllerDependencies();
        injectMasterCricketControllerDependencies();

        boardController.setGamesRepository(gamesRepository);
        stage.setOnCloseRequest((e) -> {
            closeApplication(e);
        });
        makeAppWindowDragable();
        //makeAppWindowResizable();
        setVolumeSliderListener();
    }


    //==================================================================================================
    // Events Methods
    //==================================================================================================
    public void maximalizeIconClicked(MouseEvent event) {
//        if(stage.isFullScreen())
//            stage.setFullScreen(false);
//        else
//            stage.setFullScreen(true);
    }

    public void minimalizeIconClicked(MouseEvent event) {
        stage.setIconified(true);
    }

    public void closeIconClicked(MouseEvent event) {
        closeApplication(null);
    }

    public void newGameIconClicked() {
        if (!gamesRepository.isEmpty()) {
            Alert a = new Alert(Alert.AlertType.NONE, "Are you sure you want to abandon game?", ButtonType.YES,
                    ButtonType.NO);
            Optional<ButtonType> confirm = a.showAndWait();
            if (confirm.isPresent() && confirm.get() == ButtonType.NO)
                return;
        }
        gamesRepository.clear();
        boardController.getGame01PlayersTable().getChildren().clear();
        welcomeController.toFront();
        welcomeController.getChooseGamePane().toFront();
    }

    public void saveGameIconClicked() {
        gamesRepository.saveGame();
    }

    public void loadGameIconClicked() {
        welcomeController.loadExistingGameButtonClicked();
    }

    public void volumeIconClicked() {
        if (volumeIcon.getOpacity() == 0.58) {
            volumeIcon.setOpacity(1);
            volumeIcon.setImage(new Image("images/volume_up.png"));
            volumeSlider.setDisable(false);
        } else {
            volumeIcon.setOpacity(0.58);
            volumeIcon.setImage(new Image("images/volume_off.png"));
            volumeSlider.setDisable(true);
        }
        soundPlayer.setSoundsActive(!volumeSlider.isDisable());
    }

    public void closeApplication(WindowEvent event) {
        Alert alert = new Alert(Alert.AlertType.NONE, "Are you sure you want to quit?", ButtonType.YES,
                ButtonType.NO);
        Optional<ButtonType> confirm = alert.showAndWait();
        if (confirm.isPresent() && confirm.get() == ButtonType.YES) {
            stage.close();
        } else if (event != null)
            event.consume();
    }

    public void paneIconHoverIn(MouseEvent event) {
        ((Pane) event.getSource()).setStyle("-fx-background-color: white;");
    }

    public void paneIconHoverOut(MouseEvent event) {
        ((Pane) event.getSource()).setStyle("-fx-background-color: #8f2f28;");
    }

    public void resizeIconHoverIn(MouseEvent event) {
        ((Polygon) event.getSource()).setFill(Color.rgb(30, 42, 200));
        ((Polygon) event.getSource()).setStroke(Color.rgb(30, 42, 200));
    }

    public void resizeIconHoverOut(MouseEvent event) {
        ((Polygon) event.getSource()).setFill(Color.BLACK);
        ((Polygon) event.getSource()).setStroke(Color.BLACK);
    }


    //==================================================================================================
    // Private Methods
    //==================================================================================================
    private void makeAppWindowDragable() {
        mainTopPane.setOnMousePressed((event) -> {
            xOffSet = event.getSceneX();
            yOffSet = event.getSceneY();
            mainTopPane.setCursor(Cursor.CLOSED_HAND);
        });
        mainTopPane.setOnMouseDragged((event) -> {
            stage.setX(event.getScreenX() - xOffSet);
            stage.setY(event.getScreenY() - yOffSet);
            stage.setOpacity(0.92);
        });
        mainTopPane.setOnMouseReleased((event) -> {
            mainTopPane.setCursor(Cursor.DEFAULT);
            stage.setOpacity(1.0);
        });
    }

    public void makeAppWindowResizable(){
        resizeButton.setOnMousePressed((event) -> {
            xOffSet = event.getSceneX();
            yOffSet = event.getSceneY();
            resizeButton.setCursor(Cursor.MOVE);
        });
        resizeButton.setOnMouseDragged((event) -> {
        });
        resizeButton.setOnMouseReleased((event) -> {
            resizeButton.setCursor(Cursor.SE_RESIZE);
        });
    }

    private void setVolumeSliderListener() {
        volumeSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            soundPlayer.setVolumeLevel(volumeSlider.getValue());
        });
    }

    private void injectMasterCricketControllerDependencies() {
        masterCricketController.setWelcomeController(welcomeController);
        masterCricketController.setBoardController(boardController);
        masterCricketController.setGamesRepository(gamesRepository);
    }

    private void injectCricketControllerDependencies() {
        cricketController.setWelcomeController(welcomeController);
        cricketController.setBoardController(boardController);
        cricketController.setGamesRepository(gamesRepository);
    }

    private void injectGame01ControllerDependencies() {
        game01Controller.setWelcomeController(welcomeController);
        game01Controller.setGamesRepository(gamesRepository);
        game01Controller.setBoardController(boardController);
    }

    private void injectWelcomeControllerDependencies() {
        welcomeController.setMainController(this);
        welcomeController.setGame01Controller(game01Controller);
        welcomeController.setGamesRepository(gamesRepository);
        welcomeController.setCricketController(cricketController);
        welcomeController.setMasterCricketController(masterCricketController);
    }

    private void injectGame01Dependencies() {
        Game01.setSoundPlayer(soundPlayer);
        Game01.setGamesRepository(gamesRepository);
        Game01.setGame01Controller(game01Controller);
        Game01.setBoardController(boardController);
    }

    private void injectPlayersDependencies() {
        Player01.setGamesRepository(gamesRepository);
        Player01.setBoardController(boardController);
        PlayerCricket.setGamesRepository(gamesRepository);
        PlayerCricket.setBoardController(boardController);
        PlayerMasterCricket.setGamesRepository(gamesRepository);
        PlayerMasterCricket.setBoardController(boardController);
    }

    private void injectMasterCricketDependencies() {
        MasterCricket.setGamesRepository(gamesRepository);
        MasterCricket.setSoundPlayer(soundPlayer);
        MasterCricket.setBoardController(boardController);
        MasterCricket.setCricketController(cricketController);
    }

    private void injectCricketDependencies() {
        Cricket.setGamesRepository(gamesRepository);
        Cricket.setSoundPlayer(soundPlayer);
        Cricket.setBoardController(boardController);
        Cricket.setCricketController(cricketController);
    }

    private void createSoundPlayer() {
        soundPlayer = new SoundPlayer();
        soundPlayer.setSoundsActive(!volumeSlider.isDisable());
        soundPlayer.setVolumeLevel(volumeSlider.getValue());
    }

}	
