package com.rmaj91.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

public class MasterCricketController {

    private WelcomeController welcomeController;


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


    @FXML
    void masterField7Toggle(ActionEvent event) {

    }

    public void toFront() {
        newMasterPane.toFront();
    }

    public void backButtonClicked() {
        welcomeController.toFront();
    }

    public void setWelcomeController(WelcomeController welcomeController) {
        this.welcomeController = welcomeController;
    }
}
