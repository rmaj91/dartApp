package com.rmaj91.controller;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import com.rmaj91.Main;
import com.rmaj91.interfaces.GamesRepository;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class WelcomeController implements Initializable {

    /*Dependencies*/
    private GamesRepository gamesRepository;
    private Game01Controller game01Controller;
    private MainController mainController;
    private CricketController cricketController;
    private MasterCricketController masterCricketController;

    /*JavaFX elements*/
    @FXML
    private StackPane mainStackPane;

    @FXML
    private VBox aboutPane;

    @FXML
    private VBox chooseGamePane;

    @FXML
    private ComboBox<String> chooseGameComboBox;

    @FXML
    private VBox welcomePane;

    /*Getters & Setters*/
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setGamesRepository(GamesRepository gamesRepository) {
        this.gamesRepository = gamesRepository;
    }

    public void setGame01Controller(Game01Controller game01Controller) {
        this.game01Controller = game01Controller;
    }

	public void setCricketController(CricketController cricketController) {
		this.cricketController = cricketController;
	}
	public void setMasterCricketController(MasterCricketController masterCricketController) {
		this.masterCricketController = masterCricketController;
	}


    /*Initializing*/
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		chooseGameComboBox.getItems().addAll("'01 Game","Cricket","Master Cricket");
		chooseGameComboBox.getSelectionModel().selectFirst();
	}

	/*Events*/
    public void aboutButtonClicked() {
    	aboutPane.toFront();
    }

    public void exitButtonClicked() {
    	mainController.closeApplication(null);
    }

    public void backToWelcomeButtonClicked() {
    	welcomePane.toFront();
    }

    public void createNewGameButtonClicked() {
    	chooseGamePane.toFront();
    }

    public void loadExistingGameButtonClicked() {
        gamesRepository.loadGame();
    }

    public void letsPlayButtonClicked() {
    	if(chooseGameComboBox.getValue() == "'01 Game") {
    		game01Controller.toFront();
    	}    		
    	else if(chooseGameComboBox.getValue() == "Cricket")
    		cricketController.toFront();
    	else if(chooseGameComboBox.getValue() == "Master Cricket")
    		masterCricketController.toFront();
    	
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
