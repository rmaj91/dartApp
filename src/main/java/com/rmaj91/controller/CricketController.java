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

public class CricketController implements Initializable {

    @FXML
    private VBox newCricketPane;

    @FXML
    private TitledPane roundsCricketPane;

    @FXML
    private Spinner<?> cricketRoundsBox;

    @FXML
    private RadioButton cricketRadioRounds25;

    @FXML
    private ToggleGroup group01_22;

    @FXML
    private RadioButton cricketRadioRoundsOther;

    @FXML
    private TitledPane playersCricketPane;

    @FXML
    private RadioButton cricketRadioPlayers1;

    @FXML
    private ToggleGroup group01_32;

    @FXML
    private RadioButton cricketRadioPlayers2;

    @FXML
    private RadioButton cricketRadioPlayers3;

    @FXML
    private RadioButton cricketRadioPlayers4;

    @FXML
    private CheckBox cricketDoubleOut;

    @FXML
    private TextField cricketNamePlayer1;

    @FXML
    private TextField cricketNamePlayer2;

    @FXML
    private TextField cricketNamePlayer3;

    @FXML
    private TextField cricketNamePlayer4;

    @FXML
    private TitledPane fieldsCricketPane;

    @FXML
    private Spinner<?> cricketBoxField1;

    @FXML
    private Spinner<?> cricketBoxField2;

    @FXML
    private Spinner<?> cricketBoxField3;

    @FXML
    private Spinner<?> cricketBoxField4;

    @FXML
    private Spinner<?> cricketBoxField5;

    @FXML
    private Spinner<?> cricketBoxField6;

    @FXML
    private CheckBox cricketCheckBoxField7;

    @FXML
    private Spinner<?> cricketBoxField7;

    @FXML
    private Button cricketBackButton;

    @FXML
    private Button cricketLetsButton;
    
    public void toFront() {
    	newCricketPane.toFront();
    }
    
    private WelcomeController welcomeController;
    
	public void setWelcomeController(WelcomeController welcomeController) {
		this.welcomeController = welcomeController;
	}


	public void backButton() {
    	welcomeController.toFront();
    }

	public void criketField7Toggle() {
		if(cricketCheckBoxField7.isSelected())
		cricketBoxField7.setDisable(true);
	else
		cricketBoxField7.setDisable(false);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// rounds box
		cricketRadioRoundsOther.selectedProperty().addListener((ObservableValue<?extends Boolean> obs,Boolean wasPreviouslySelected, Boolean isNowSelected)->{
			if(isNowSelected) {
				cricketRoundsBox.setDisable(false);
			}else {
				cricketRoundsBox.setDisable(true);
			}
		});
		// player boxes 2
		cricketRadioPlayers2.selectedProperty().addListener((ObservableValue<?extends Boolean> obs,Boolean wasPreviouslySelected, Boolean isNowSelected)->{
			if(isNowSelected) {
				cricketNamePlayer2.setDisable(false);
			}else {
				cricketNamePlayer2.setDisable(true);
			}
		});
		// player boxes 3
		cricketRadioPlayers3.selectedProperty().addListener((ObservableValue<?extends Boolean> obs,Boolean wasPreviouslySelected, Boolean isNowSelected)->{
			if(isNowSelected) {
				cricketNamePlayer2.setDisable(false);
				cricketNamePlayer3.setDisable(false);
			}else {
				cricketNamePlayer2.setDisable(false);
				cricketNamePlayer3.setDisable(true);
			}
		});
		// player boxes 4
		cricketRadioPlayers4.selectedProperty().addListener((ObservableValue<?extends Boolean> obs,Boolean wasPreviouslySelected, Boolean isNowSelected)->{
			if(isNowSelected) {
				cricketNamePlayer2.setDisable(false);
				cricketNamePlayer3.setDisable(false);
				cricketNamePlayer4.setDisable(false);
			}else {
				cricketNamePlayer2.setDisable(true);
				cricketNamePlayer3.setDisable(true);
				cricketNamePlayer4.setDisable(true);
			}
		});		
	}

}
