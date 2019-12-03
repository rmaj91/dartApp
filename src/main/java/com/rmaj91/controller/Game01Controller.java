package com.rmaj91.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import com.rmaj91.domain.Game01;
import com.rmaj91.domain.Player01;
import com.rmaj91.repository.GameFactory;
import com.rmaj91.repository.GamesRepositoryImpl;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.StringConverter;

public class Game01Controller implements Initializable{

    /*Dependencies*/
    private GamesRepositoryImpl gamesRepository;
    private WelcomeController welcomeController;
    private BoardController boardController;

    /*Final Values*/
	public final static int MIN_POINTS=100;
	public final static int MAX_POINTS=999;
	public final static int DEFAULT_POINTS=501;
	
	public final static int MIN_ROUNDS=1;
	public final static int MAX_ROUNDS=99;
	public final static int DEFAULT_ROUNDS=25;

	/*JavaFX elements*/
    @FXML
    private VBox newGame01Pane;

    @FXML
    private RadioButton game01RadioPoints501;

    @FXML
    private RadioButton game01RadioPointsOther;

    @FXML
    private Spinner<Integer> game01PointsSpinner;

    @FXML
    private Spinner<Integer> game01RoundsSpinner;

    @FXML
    private RadioButton game01RadioRoundsOther;

    @FXML
    private ToggleGroup radioPlayersGroup;

    @FXML
    private CheckBox game01DoubleOut;

    @FXML
    private TextField game01PlayerNameTextField1;

    @FXML
    private TextField game01PlayerNameTextField2;

    @FXML
    private TextField game01PlayerNameTextField3;

    @FXML
    private TextField game01PlayerNameTextField4;

    TextField[] game01PlayerNamesTextFields;

    /*Setters & Getters*/
    public void setGamesRepository(GamesRepositoryImpl gamesRepository) {
        this.gamesRepository = gamesRepository;
    }

    public void setWelcomeController(WelcomeController welcomeController) {
        this.welcomeController = welcomeController;
    }

    public void setBoardController(BoardController boardController) {
        this.boardController = boardController;
    }

    /*Events*/
    public void toFront() {
    	newGame01Pane.toFront();
    }

    public void backButtonClicked() {
        welcomeController.toFront();
    }

    /**
     * Method Initialize board view and games repository
     */
	public void letsPlayButtonClicked() {
        boardController.toFront();
        boardController.getGame01ScoreTable().toFront();
        displayDoubleOutLabel();

        gamesRepository.createNewGame(GameFactory.getGame("'01 Game"));
        initializeStaticGame01Variables();
        initializeGame01Players();
        createPlayersVBoxes(getPlayersQuantity());

		gamesRepository.getCurrentRound().displayRound();
        boardController.getThrowField1().requestFocus();
        // In case of startig new game after winning previous
        boardController.getMainStackPane().setDisable(false);
	}

    /*Private Methods*/
    /**
     * Method defines number of rounds selected by radio buttons and returns it.
     */
    private int getRounds(){
        if(game01RadioRoundsOther.isSelected())
            return game01RoundsSpinner.getValue();
        else
            return 25;
    }

    /**
     * Method defines starting points selected by radio buttons and returns it.
     * */
    private int getStartingPoints(){
        if(game01RadioPointsOther.isSelected())
            return game01PointsSpinner.getValue();
        else if(game01RadioPoints501.isSelected())
            return 501;
        else
            return 701;
    }

    /**
     * Method defines number of players selected by radio buttons and returns it.
     * */
    private int getPlayersQuantity(){
        int playersQuantity = Integer.parseInt(((RadioButton) radioPlayersGroup.getSelectedToggle()).getText());
        return playersQuantity;
    }

    /**
     * Method gets players names from textFieldsArray and returns it.
     * */
    private String[] getPlayersNames(int playersQuantity){
        String[] playersNames = new String[playersQuantity];
        for (int i = 0; i < playersQuantity; i++) {
            playersNames[i] = getPlayerName(game01PlayerNamesTextFields[i]);
        }
        return playersNames;
    }

    /**
     * Method takes text from textfield and if its empty returns "Player"+"player number"
     * else returns getText() value
     */
    private String getPlayerName(TextField textField){
        if(textField.getText().trim().equals("")){
            return "Player"+textField.getId().substring(textField.getId().length()-1);
        }
        else
            return textField.getText();
    }

    /**
     * Method checks if double out option is checked, return true if yes, false if not.
     */
    private boolean isDoubleOut(){
        if(game01DoubleOut.isSelected())
            return true;
        else
            return false;
    }

    /**
     * Method creates VBoxes for players in game01PlayersTable at board view,
     * VBoxes contains labels with name and amount of points
     * @param playersQuantity quantity of setted players
     */
    private void createPlayersVBoxes(int playersQuantity){
        for(int i=0;i<playersQuantity;i++){
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.BOTTOM_CENTER);
            vBox.setMinWidth(100);
            vBox.getChildren().addAll(createPlayerLabel(gamesRepository.getCurrentRound().getPlayers()[i].getName(),false),
                    createPlayerLabel(String.valueOf(Game01.getStartingPoints()),true));
            boardController.getGame01PlayersTable().getChildren().add(vBox);
        }
    }

    /**
     * Method created label for game01PlayersTable VBoxes
     * @param text content of the label
     * @param ifBold if text in label should be bolded
     * @return player label
     */
    private Label createPlayerLabel(String text,boolean ifBold){
        Label playerLabel = new Label();
        playerLabel.setText(text);
        playerLabel.setFont(new Font("System",22));
        playerLabel.setTextFill(Color.WHITE);
        if(ifBold)
            playerLabel.setStyle("-fx-font-weight: bold");
        return playerLabel;
    }

    /**
     * Method initialize Game01.class static variables
     */
    private void initializeStaticGame01Variables(){
        Game01.setDoubleOut(isDoubleOut());
        Game01.setPlayersQuantity(getPlayersQuantity());
        Game01.setRoundsMaxNumber(getRounds());
        Game01.setStartingPoints(getStartingPoints());
    }

    /**
     * Method initialize players array in Game01 object at games repository with Player.class objects
      */
    private void initializeGame01Players(){
        // Getting players names
        String[] playersNames = getPlayersNames(getPlayersQuantity());
        // Getting blank players Array
        Player01[] playersArray = new Player01[getPlayersQuantity()];
        for(int i=0;i<getPlayersQuantity();i++){
            playersArray[i] = new Player01();
        }
        // Init players names
        for(int i=0;i<getPlayersQuantity();i++){
            playersArray[i].setName(playersNames[i]);
        }
        // Init players points
        for (Player01 player : playersArray) {
            player.setPoints(getStartingPoints());
        }
        // Injecting Array into repository
        ((Game01)gamesRepository.getCurrentRound()).setPlayers(playersArray);
    }

    /**
     * Method displays on game01PlayersTable if double out option is setted if not label its not visible
     */
    private void displayDoubleOutLabel(){
        if(isDoubleOut())
            boardController.getDoubleOut().setVisible(true);
        else
            boardController.getDoubleOut().setVisible(false);
    }


    ////////////////
    /*Initializing*/
    ////////////////
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        // Creating array of game01PlayerNameText fields
        game01PlayerNamesTextFields = new TextField[4];
        game01PlayerNamesTextFields[0] = game01PlayerNameTextField1;
        game01PlayerNamesTextFields[1] = game01PlayerNameTextField2;
        game01PlayerNamesTextFields[2] = game01PlayerNameTextField3;
        game01PlayerNamesTextFields[3] = game01PlayerNameTextField4;

        // Points Box listener
        game01RadioPointsOther.selectedProperty().addListener((obs,wasPrevSelected,isNowSelected)->{
            if(isNowSelected) {
                game01PointsSpinner.setDisable(false);
            }else {
                game01PointsSpinner.setDisable(true);
            }
        });

        // Rounds Box listener
        game01RadioRoundsOther.selectedProperty().addListener((obs,wasPrevSelected,isNowSelected)->{
            if(isNowSelected) {
                game01RoundsSpinner.setDisable(false);
            }else {
                game01RoundsSpinner.setDisable(true);
            }
        });

        // Players quantity radio button group listener
        radioPlayersGroup.selectedToggleProperty().addListener((obs,oldValue,newValue)->{
            String radioButtonText = ((RadioButton)radioPlayersGroup.getSelectedToggle()).getText();
            int playersQuantity = Integer.parseInt(radioButtonText);
            // Disabling all players text fields
            for(int i=0;i<4;i++)
                game01PlayerNamesTextFields[i].setDisable(true);
            // Setting active players text fields
            for(int i=0;i<playersQuantity;i++)
                game01PlayerNamesTextFields[i].setDisable(false);
        });

        // Players names length validation
        for (TextField game01PlayerNamesTextField : game01PlayerNamesTextFields) {
            game01PlayerNamesTextField.textProperty().addListener((observable, oldValue, newValue) ->{
                if(newValue.length() > MainController.PLAYERS_NAME_MAX_LENGTH)
                    game01PlayerNamesTextField.setText(newValue.substring(0, MainController.PLAYERS_NAME_MAX_LENGTH));
            });
        }


        // Spinners Factories
        SpinnerValueFactory<Integer> svfPoints = new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_POINTS, MAX_POINTS, DEFAULT_POINTS);
        SpinnerValueFactory<Integer> svfRounds = new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_ROUNDS,MAX_ROUNDS, DEFAULT_ROUNDS);
        game01PointsSpinner.setValueFactory(svfPoints);
        game01RoundsSpinner.setValueFactory(svfRounds);

        // Spinner points validation
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

        // Spinner rounds validation
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

        // Setting converters and filters
        svfPoints.setConverter(pointsConverter);
        game01PointsSpinner.getEditor().setTextFormatter(new TextFormatter<>(pointsConverter,DEFAULT_POINTS,pointsFilter));
        svfRounds.setConverter(roundsConverter);
        game01RoundsSpinner.getEditor().setTextFormatter(new TextFormatter<>(roundsConverter,DEFAULT_ROUNDS,roundsFilter));
    }
}
