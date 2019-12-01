package com.rmaj91.controller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


import com.rmaj91.Main;
import com.rmaj91.domain.Game01;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MainController implements Initializable {

	@FXML
	private AnchorPane rootPane;

	@FXML
	private StackPane mainStackPane;

	@FXML
	private AnchorPane mainTopPane;

	@FXML
	private ImageView closeIcon;

	@FXML
	private ImageView maxIcon;

	@FXML
	private ImageView minIcon;

	@FXML
	private ImageView smallLogo;

	@FXML
	private Label gameName;

	@FXML
	private HBox mainBottomPane;

	@FXML
	private ImageView volumeIcon;

	@FXML
	private Slider volumeSlider;

	@FXML
	private ImageView newGameIcon;

	@FXML
	private ImageView saveGameIcon;

	private double xOffSet = 0;
	private double yOffSet = 0;
	
	// Injecting controllers
	@FXML
	private WelcomeController welcomeController;
	@FXML
	private Game01Controller game01Controller;
	//@FXML
	//private CricketController cricketController;
	//@FXML
	//private MasterCricketController masterCricketController;
	@FXML
	private BoardController boardController;


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		makePaneDragable();
		welcomeController.setGame01Controller(game01Controller);
		//welcomeController.setCricketController(cricketController);
		//welcomeController.setMasterCricketController(masterCricketController);
		game01Controller.setWelcomeController(welcomeController);
		//cricketController.setWelcomeController(welcomeController);
		//masterCricketController.setWelcomeController(welcomeController);
		game01Controller.setBoardController(boardController);
		Game01.setGame01Controller(game01Controller);
		Game01.setBoardController(boardController);

		initSoundFiles();

	}

	private void initSoundFiles() {

//		volumeSlider.valueProperty().addListener((observable,oldValue,newValue) ->{
//			Main.mediaPlayer.setVolume(volumeSlider.getValue());
//		});
	}

	private void makePaneDragable() {
		mainTopPane.setOnMousePressed((event) -> {
			xOffSet = event.getSceneX();
			yOffSet = event.getSceneY();
			mainTopPane.setCursor(Cursor.CLOSED_HAND);
		});
		mainTopPane.setOnMouseDragged((event) -> {
			Main.stage.setX(event.getScreenX() - xOffSet);
			Main.stage.setY(event.getScreenY() - yOffSet);
			Main.stage.setOpacity(0.92);
		});
		mainTopPane.setOnMouseReleased((event) -> {
			mainTopPane.setCursor(Cursor.DEFAULT);
			Main.stage.setOpacity(1.0);
		});
	}

	public void minimallizeApplication(MouseEvent event) {
		Main.stage.setIconified(true);
    }
	
	@FXML
	public void closeApplication() {
		Main.closeApplication(null);
	}

	public void newGameIconClicked(){
		boardController.getGame01PlayersTable().getChildren().clear();
		welcomeController.toFront();
	}

	public void saveGameIconClicked() {
		// todo zapis do pliku
	}

	public void volumeIconClicked(){
		// todo dodac dzwieki
		if(volumeIcon.getOpacity() == 0.58){
			volumeIcon.setOpacity(1);
			volumeIcon.setImage(new Image("images/volume_up.png"));
			volumeSlider.setDisable(false);
		}
		else{
			volumeIcon.setOpacity(0.58);
			volumeIcon.setImage(new Image("images/volume_off.png"));
			volumeSlider.setDisable(true);
		}
	}


}	
