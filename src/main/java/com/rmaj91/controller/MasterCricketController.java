package com.rmaj91.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

public class MasterCricketController implements Initializable{

    @FXML
    private VBox newMasterPane;

    @FXML
    private TitledPane roundsGame01Pane2;

    @FXML
    private Spinner<?> masterRoundsBox;

    @FXML
    private RadioButton masterRadioRounds25;

    @FXML
    private ToggleGroup group01_21;

    @FXML
    private RadioButton masterRadioRoundsOther;

    @FXML
    private TitledPane playersGame01Pane2;

    @FXML
    private RadioButton masterRadioPlayers1;

    @FXML
    private ToggleGroup group01_31;

    @FXML
    private RadioButton masterRadioPlayers2;

    @FXML
    private RadioButton masterRadioPlayers3;

    @FXML
    private RadioButton masterRadioPlayers4;

    @FXML
    private CheckBox masterDoubleOut;

    @FXML
    private TextField masterNamePlayer1;

    @FXML
    private TextField masterNamePlayer2;

    @FXML
    private TextField masterNamePlayer3;

    @FXML
    private TextField masterNamePlayer4;

    @FXML
    private TitledPane fieldsMasterPane;

    @FXML
    private Spinner<?> masterBoxField1;

    @FXML
    private Spinner<?> masterBoxField2;

    @FXML
    private Spinner<?> masterBoxField3;

    @FXML
    private Spinner<?> masterBoxField4;

    @FXML
    private Spinner<?> masterBoxField5;

    @FXML
    private Spinner<?> masterBoxField6;

    @FXML
    private CheckBox masterCheckBoxField7;

    @FXML
    private Spinner<?> masterBoxField7;

    @FXML
    private Button masterBackButton;

    @FXML
    private Button masterLetsButton;
    
    public void toFront() {
    	newMasterPane.toFront();
    }
    
    private WelcomeController welcomeController;
    
	public void setWelcomeController(WelcomeController welcomeController) {
		this.welcomeController = welcomeController;
	}


	public void backButton() {
    	welcomeController.toFront();
    }


	public void masterField7Toggle() {
		if(masterCheckBoxField7.isSelected())
			masterBoxField7.setDisable(true);
		else
			masterBoxField7.setDisable(false);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// rounds box
		masterRadioRoundsOther.selectedProperty().addListener((ObservableValue<?extends Boolean> obs,Boolean wasPreviouslySelected, Boolean isNowSelected)->{
			if(isNowSelected) {
				masterRoundsBox.setDisable(false);
			}else {
				masterRoundsBox.setDisable(true);
			}
		});
		// player boxes 2
		masterRadioPlayers2.selectedProperty().addListener((ObservableValue<?extends Boolean> obs,Boolean wasPreviouslySelected, Boolean isNowSelected)->{
			if(isNowSelected) {
				masterNamePlayer2.setDisable(false);
			}else {
				masterNamePlayer2.setDisable(true);
			}
		});
		// player boxes 3
		masterRadioPlayers3.selectedProperty().addListener((ObservableValue<?extends Boolean> obs,Boolean wasPreviouslySelected, Boolean isNowSelected)->{
			if(isNowSelected) {
				masterNamePlayer2.setDisable(false);
				masterNamePlayer3.setDisable(false);
			}else {
				masterNamePlayer2.setDisable(false);
				masterNamePlayer3.setDisable(true);
			}
		});
		// player boxes 4
		masterRadioPlayers4.selectedProperty().addListener((ObservableValue<?extends Boolean> obs,Boolean wasPreviouslySelected, Boolean isNowSelected)->{
			if(isNowSelected) {
				masterNamePlayer2.setDisable(false);
				masterNamePlayer3.setDisable(false);
				masterNamePlayer4.setDisable(false);
			}else {
				masterNamePlayer2.setDisable(true);
				masterNamePlayer3.setDisable(true);
				masterNamePlayer4.setDisable(true);
			}
		});		
	}

}
