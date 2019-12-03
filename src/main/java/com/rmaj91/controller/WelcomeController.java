package com.rmaj91.controller;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import com.rmaj91.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class WelcomeController implements Initializable {

    @FXML
    private StackPane mainStackPane;

    @FXML
    private VBox aboutPane;

    @FXML
    private Button aboutButton1;

    @FXML
    private VBox chooseGamePane;

    @FXML
    private VBox chooseGameFrame;

    @FXML
    private ComboBox<String> chooseGameComboBox;

    @FXML
    private Button chooseGameBackButton;

    @FXML
    private Button chooseGameLetsButton;

    @FXML
    private VBox welcomePane;

    @FXML
    private ImageView bigLogo;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button createNewGameButton;

    @FXML
    private Button loadGameButton;

    @FXML
    private Button aboutButton;
    
    @FXML
    private Button exitButton;

    private MainController mainController;


    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		chooseGameComboBox.getItems().addAll("'01 Game","Cricket","Master Cricket");
		chooseGameComboBox.getSelectionModel().selectFirst();
	}
    
    @FXML
    public void about() {
    	aboutPane.toFront();
    }
    
    @FXML
    public void exit() {
    	Main.closeApplication(null);
    }

    @FXML
    public void backToWelcome() {
    	welcomePane.toFront();
    }

    @FXML
    public void createNewGame() {
    	chooseGamePane.toFront();
    }

    @FXML
    public void loadExistingGame() {	
        Main.gamesRepositotyImpl.loadGame();
    }
    
    private Game01Controller game01Controller;	
	//private CricketController cricketController;
	//private MasterCricketController masterCricketController;

	public void setGame01Controller(Game01Controller game01Controller) {
		this.game01Controller = game01Controller;
	}

//	public void setCricketController(CricketController cricketController) {
//		this.cricketController = cricketController;
//	}
//	public void setMasterCricketController(MasterCricketController masterCricketController) {
//		this.masterCricketController = masterCricketController;
//	}
	
	
	@FXML
    public void letsPlayButton() {
    	if(chooseGameComboBox.getValue() == "'01 Game") {
    		game01Controller.toFront();
    	}    		
//    	else if(chooseGameComboBox.getValue() == "Cricket")
//    		cricketController.toFront();
//    	else if(chooseGameComboBox.getValue() == "Master Cricket")
//    		masterCricketController.toFront();
    	
    }
	
	public void toFront() {
		mainStackPane.toFront();
	}


    public void openGitHub() {

        try {
            Desktop.getDesktop().browse(new URL("http://www.github.com/rmaj91/dartApp").toURI());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
