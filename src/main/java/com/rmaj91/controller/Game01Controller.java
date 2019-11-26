package com.rmaj91.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import com.rmaj91.Main;
import com.rmaj91.domain.Game01;
import com.rmaj91.repository.GameFactory;
import javafx.beans.value.ObservableValue;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

public class Game01Controller implements Initializable{
	
	public final static int MIN_POINTS=100;
	public final static int MAX_POINTS=999;
	public final static int DEFAULT_POINTS=501;
	
	public final static int MIN_ROUNDS=1;
	public final static int MAX_ROUNDS=99;
	public final static int DEFAULT_ROUNDS=25;

    @FXML
    private VBox newGame01Pane;

    @FXML
    private TitledPane pointsGame01Pane;

    @FXML
    private RadioButton game01RadioPoints501;

    @FXML
    private ToggleGroup group01_1;

    @FXML
    private RadioButton game01RadioPoints701;

    @FXML
    private RadioButton game01RadioPointsOther;

    @FXML
    private Spinner<Integer> game01PointsBox;

    @FXML
    private TitledPane roundsGame01Pane;

    @FXML
    private Spinner<Integer> game01RoundsBox;

    @FXML
    private RadioButton game01RadioRounds25;

    @FXML
    private ToggleGroup group01_2;

    @FXML
    private RadioButton game01RadioRoundsOther;

    @FXML
    private TitledPane playersGame01Pane;

    @FXML
    private RadioButton game01RadioPlayers1;

    @FXML
    private ToggleGroup group01_3;

    @FXML
    private RadioButton game01RadioPlayers2;

    @FXML
    private RadioButton game01RadioPlayers3;

    @FXML
    private RadioButton game01RadioPlayers4;

    @FXML
    private CheckBox game01DoubleOut;

    @FXML
    private TextField game01NamePlayer1;

    @FXML
    private TextField game01NamePlayer2;

    @FXML
    private TextField game01NamePlayer3;

    @FXML
    private TextField game01NamePlayer4;

    @FXML
    private Button game01BackButton;

    @FXML
    private Button game01LetsButton;
    
    public void toFront() {
    	newGame01Pane.toFront();
    }
    
    private WelcomeController welcomeController;
    
	public void setWelcomeController(WelcomeController welcomeController) {
		this.welcomeController = welcomeController;
	}


	private BoardController boardController;
	
	
	public void setBoardController(BoardController boardController) {
		this.boardController = boardController;
	}

	public void letsPlayButton() {
		boardController.toFront();
		// initializing games repository
		Main.gamesRepositoty.createNew(GameFactory.getGame("'01 Game"));
//		int subrounds = 
//		int playersQuantity =
		Game01.setPlayersQuantity(3);
		Game01.setSubrounds(2);

	}


	public void backButton() {
    	welcomeController.toFront();
    }
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// points box
		game01RadioPointsOther.selectedProperty().addListener((ObservableValue<?extends Boolean> obs,Boolean wasPreviouslySelected, Boolean isNowSelected)->{
			if(isNowSelected) {
				game01PointsBox.setDisable(false);
			}else {
				game01PointsBox.setDisable(true);
			}
		});
		// rounds box
		game01RadioRoundsOther.selectedProperty().addListener((ObservableValue<?extends Boolean> obs,Boolean wasPreviouslySelected, Boolean isNowSelected)->{
			if(isNowSelected) {
				game01RoundsBox.setDisable(false);
			}else {
				game01RoundsBox.setDisable(true);
			}
		});
		
		// player boxes 2
		game01RadioPlayers2.selectedProperty().addListener((ObservableValue<?extends Boolean> obs,Boolean wasPreviouslySelected, Boolean isNowSelected)->{
			if(isNowSelected) {
				game01NamePlayer2.setDisable(false);
			}else {
				game01NamePlayer2.setDisable(true);
			}
		});
		// player boxes 3
		game01RadioPlayers3.selectedProperty().addListener((ObservableValue<?extends Boolean> obs,Boolean wasPreviouslySelected, Boolean isNowSelected)->{
			if(isNowSelected) {
				game01NamePlayer2.setDisable(false);
				game01NamePlayer3.setDisable(false);
			}else {
				game01NamePlayer2.setDisable(false);
				game01NamePlayer3.setDisable(true);
			}
		});
		// player boxes 4
		game01RadioPlayers4.selectedProperty().addListener((ObservableValue<?extends Boolean> obs,Boolean wasPreviouslySelected, Boolean isNowSelected)->{
			if(isNowSelected) {
				game01NamePlayer2.setDisable(false);
				game01NamePlayer3.setDisable(false);
				game01NamePlayer4.setDisable(false);
			}else {
				game01NamePlayer2.setDisable(true);
				game01NamePlayer3.setDisable(true);
				game01NamePlayer4.setDisable(true);
			}
		});
		
		//////////////////////////////
		// game points spinner
		//////////////////////////////

		SpinnerValueFactory<Integer> svfPoints = new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_POINTS, MAX_POINTS, DEFAULT_POINTS);
		SpinnerValueFactory<Integer> svfRounds = new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_ROUNDS,MAX_ROUNDS, DEFAULT_ROUNDS);
		game01PointsBox.setValueFactory(svfPoints);
		game01RoundsBox.setValueFactory(svfRounds);
		
		// POINTS VALIDATION //
		StringConverter<Integer> pointsConverter = new StringConverter<>() {

			@Override
			public Integer fromString(String string) {
				if(string.isEmpty())
					return DEFAULT_POINTS;
				else
					return Integer.parseInt(string);
			}

			@Override
			public String toString(Integer object) {
				return object.toString();
			}
			
		};
		
	    UnaryOperator<TextFormatter.Change> pointsFilter = new UnaryOperator<TextFormatter.Change>() {

	        @Override
	        public TextFormatter.Change apply(TextFormatter.Change t) {

	            String newText = t.getControlNewText() ;
	            if (newText.matches("[0-9]*")) {
	                return t ;
	            }
	            return null ;
	        }
	    };
	    
		// ROUnds VALIDATION //
		StringConverter<Integer> roundsConverter = new StringConverter<>() {

			@Override
			public Integer fromString(String string) {
				if(string.isEmpty())
					return DEFAULT_ROUNDS;
				else
					return Integer.parseInt(string);
			}

			@Override
			public String toString(Integer object) {
				return object.toString();
			}
			
		};
		
	    UnaryOperator<TextFormatter.Change> roundsFilter = new UnaryOperator<TextFormatter.Change>() {

	        @Override
	        public TextFormatter.Change apply(TextFormatter.Change t) {

	            String newText = t.getControlNewText() ;
	            if (newText.matches("[0-9]*")) {
	                return t ;
	            }
	            return null ;
	        }
	    };
		
	    svfPoints.setConverter(pointsConverter);
	    game01PointsBox.getEditor().setTextFormatter(new TextFormatter<>(pointsConverter,DEFAULT_POINTS,pointsFilter));
	    svfRounds.setConverter(roundsConverter);
	    game01RoundsBox.getEditor().setTextFormatter(new TextFormatter<>(roundsConverter,DEFAULT_ROUNDS,roundsFilter));
		
		
		
	}

}
