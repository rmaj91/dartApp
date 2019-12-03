package com.rmaj91.controller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


import com.rmaj91.Main;
import com.rmaj91.domain.Game01;
import com.rmaj91.utility.SoundPlayer;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class MainController implements Initializable {

	/*Dependencies*/
	private SoundPlayer soundPlayer;
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

	/*Controls etc.*/
	@FXML
	private AnchorPane rootPane;

	@FXML
	private StackPane mainStackPane;

	@FXML
	private StackPane mainTopPane;

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

	/*Variables*/
	private double xOffSet = 0;
	private double yOffSet = 0;

	/*Initalizing*/
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
		Game01.setMainController(this);
		Main.gamesRepositotyImpl.setBoardController(boardController);

		Main.soundPlayer.setSoundsActive(!volumeSlider.isDisable());
		Main.soundPlayer.setVolumeLevel(volumeSlider.getValue());

		// slider listeners
		volumeSlider.valueProperty().addListener((obs,oldValue,newValue)->{
			Main.soundPlayer.setVolumeLevel(volumeSlider.getValue());
		});
//		volumeSlider.disableProperty().addListener((obs,oldValue,newValue)->{
//			Main.soundPlayer.setSoundsActive(!volumeSlider.isDisable());
//		});

		//todo
		//Stage stage = (Stage) rootPane.getScene().getWindow();
	}

	/*Getters & Setters*/
	public Slider getVolumeSlider() {
		return volumeSlider;
	}

	/*Methods*/
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

	/*Events*/
	public void minimallizeApplication(MouseEvent event) {
		Main.stage.setIconified(true);
    }

	public void closeApplication() {
		Main.closeApplication(null);
	}

	public void newGameIconClicked(){

		if(!Main.gamesRepositotyImpl.isEmpty()){
			Alert a = new Alert(Alert.AlertType.NONE, "Are you sure you want to abandon game?", ButtonType.YES,
					ButtonType.NO);
			Optional<ButtonType> confirm = a.showAndWait();
			if (confirm.isPresent() && confirm.get() == ButtonType.NO)
				return;
		}

		Main.gamesRepositotyImpl.clear();
		boardController.getGame01PlayersTable().getChildren().clear();
		welcomeController.toFront();
	}

	public void saveGameIconClicked() {
		Main.gamesRepositotyImpl.saveGame();
	}

	public void volumeIconClicked(){
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
		Main.soundPlayer.setSoundsActive(!volumeSlider.isDisable());
	}

	public void paneIconHoverIn(MouseEvent event) {
		((Pane)event.getSource()).setStyle("-fx-background-color: white;");
	}
	public void paneIconHoverOut(MouseEvent event) {
		((Pane)event.getSource()).setStyle("-fx-background-color: #8f2f28;");
	}

}	
