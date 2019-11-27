package com.rmaj91.controller;

import com.rmaj91.domain.Throw;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class BoardController {

    @FXML
    private StackPane mainStackPane;

    @FXML
    private HBox gamePane;

    @FXML
    private StackPane boardPane;

    @FXML
    private AnchorPane pointsAreaPane;

    @FXML
    private Label roundsLabel;

    @FXML
    private HBox throwsPoints;

    @FXML
    private TextField throwField1;

    @FXML
    private TextField throwField2;

    @FXML
    private TextField throwField3;

    @FXML
    private Button nextRoundButton;

    @FXML
    private Button backRoundButton;

    @FXML
    private HBox cricketsScoreTable;

    @FXML
    private VBox cricketsFields;

    @FXML
    private VBox statusPlayer1;

    @FXML
    private VBox statusPlayer2;

    @FXML
    private VBox statusPlayer3;

    @FXML
    private VBox statusPlayer4;

    @FXML
    private AnchorPane game01ScoreTable;

    @FXML
    private Label averageLabel;

    @FXML
    private Label playerNameLabel;

    @FXML
    private Label playerPointsLabel;

    @FXML
    private Label doubleOut;

    @FXML
    private HBox game01PlayersTable;


    
    public void toFront() {
    	mainStackPane.toFront();
    }


    public Label getDoubleOut() {
        return doubleOut;
    }

    public Label getRoundsLabel() {
        return roundsLabel;
    }

    public TextField getThrowField1() {
        return throwField1;
    }

    public TextField getThrowField2() {
        return throwField2;
    }

    public TextField getThrowField3() {
        return throwField3;
    }

    public AnchorPane getGame01ScoreTable() {
        return game01ScoreTable;
    }

    public Label getPlayerNameLabel() {
        return playerNameLabel;
    }

    public Label getPlayerPointsLabel() {
        return playerPointsLabel;
    }

    public HBox getGame01PlayersTable() {
        return game01PlayersTable;
    }

    public VBox getStatusPlayer1() {
        return statusPlayer1;
    }

    // OnClick Board
    public Throw boardClicked(int x, int y){

        return new Throw(1,1);
    }
}
