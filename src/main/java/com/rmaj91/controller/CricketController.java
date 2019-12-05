package com.rmaj91.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import com.rmaj91.domain.Cricket;
import com.rmaj91.domain.Game01;
import com.rmaj91.domain.PlayerCricket;
import com.rmaj91.interfaces.GamesRepository;
import com.rmaj91.repository.GameFactory;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.StringConverter;

public class CricketController implements Initializable {

    public final static int MIN_ROUNDS=1;
    public final static int MAX_ROUNDS=99;
    public final static int DEFAULT_ROUNDS=25;

    private BoardController boardController;
    private GamesRepository gamesRepository;
    private WelcomeController welcomeController;

    @FXML
    private VBox newCricketPane;

    @FXML
    private TitledPane roundsCricketPane;

    @FXML
    private Spinner<Integer> cricketRoundsBox;

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
    private ToggleGroup radioPlayersGroup;

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
    private Spinner<Integer> cricketBoxField1;

    @FXML
    private Spinner<Integer> cricketBoxField2;

    @FXML
    private Spinner<Integer> cricketBoxField3;

    @FXML
    private Spinner<Integer> cricketBoxField4;

    @FXML
    private Spinner<Integer> cricketBoxField5;

    @FXML
    private Spinner<Integer> cricketBoxField6;

    @FXML
    private CheckBox cricketCheckBoxField7;

    @FXML
    private Spinner<Integer> cricketBoxField7;

    @FXML
    private Button cricketBackButton;

    @FXML
    private Button cricketLetsButton;

    private TextField[] cricketPlayerNamesTextFields;
    private Spinner<Integer>[] cricketFieldsSpinners;


    /*Setters & Getters*/
    public void setWelcomeController(WelcomeController welcomeController) {
        this.welcomeController = welcomeController;
    }
    public void setBoardController(BoardController boardController) {
        this.boardController = boardController;
    }
    public void setGamesRepository(GamesRepository gamesRepository) {
        this.gamesRepository = gamesRepository;
    }




    /*Events*/
    //////////
    public void toFront() {
    	newCricketPane.toFront();
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


    /**
     * Method Initialize board view and games repository
     */
    public void letsPlayButtonClicked() {
        if(!validFieldsToThrowIfUniqueAndSet())
            return;
        //Creating zero'th game
        gamesRepository.createNewGame(GameFactory.getGame("Cricket"));
        Cricket.setStaticVariables(getNumberOfPlayers(), getMaxNumberOfRounds(),0);

        initializeCricketPlayersArray();
        removeAndCreatePlayersVBoxes();
        fillFieldsToThrowVBox();

        // copy round
        Cricket firstRound = new Cricket((Cricket) gamesRepository.getCurrentRound());
        gamesRepository.pushRound(firstRound);
        gamesRepository.getCurrentRound().displayRoundState();

        boardController.getMainStackPane().setDisable(false);
        boardController.toFront();
        boardController.getCricketsScoreTable().toFront();
        boardController.getThrowField1().requestFocus();
    }





    /*Initialize*/
    //////////////
    //////////////

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cricketPlayerNamesTextFields = new TextField[4];
        cricketPlayerNamesTextFields[0] = cricketNamePlayer1;
        cricketPlayerNamesTextFields[1] = cricketNamePlayer2;
        cricketPlayerNamesTextFields[2] = cricketNamePlayer3;
        cricketPlayerNamesTextFields[3] = cricketNamePlayer4;

        cricketFieldsSpinners = new Spinner[7];
        cricketFieldsSpinners[0] = cricketBoxField1;
        cricketFieldsSpinners[1] = cricketBoxField2;
        cricketFieldsSpinners[2] = cricketBoxField3;
        cricketFieldsSpinners[3] = cricketBoxField4;
        cricketFieldsSpinners[4] = cricketBoxField5;
        cricketFieldsSpinners[5] = cricketBoxField6;
        cricketFieldsSpinners[6] = cricketBoxField7;

        setRadioRoundsOthersSelectListener();
        setPlayersNamesRadioButtonSelectedListener();
        setPlayersNamesLengthValidation();
        setSpinnersFactoriesAndFilters();
    }


    /**
     * Method defines number of rounds selected by radio buttons and returns it.
     */
    private int getMaxNumberOfRounds(){
        if(cricketRadioRoundsOther.isSelected())
            return cricketRoundsBox.getValue();
        else
            return 25;
    }

    /**
     * Method defines number of players selected by radio buttons and returns it.
     * */
    private int getNumberOfPlayers(){
        int playersQuantity = Integer.parseInt(((RadioButton) radioPlayersGroup.getSelectedToggle()).getText());
        return playersQuantity;
    }

    /**
     * Method gets players names from textFieldsArray and returns it.
     * */
    private String[] getPlayersNames(int numberOfPlayers){
        String[] playersNames = new String[numberOfPlayers];
        for (int i = 0; i < numberOfPlayers; i++) {
            playersNames[i] = getPlayerName(cricketPlayerNamesTextFields[i]);
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
//
    /**
     * Method validate(in fields are unique) and sets(if values are unique) fields to hit in Criket.class
     * @return true if fields are unique or false if not
     */
    private boolean validFieldsToThrowIfUniqueAndSet(){

        ArrayList<Integer> fieldToThrow = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            fieldToThrow.add(cricketFieldsSpinners[i].getValue());
            if(i == 6 && cricketCheckBoxField7.isSelected())
                fieldToThrow.add(25);
        }

        for (int i = 0; i < 7; i++)
            for (int j = 0; j < 7; j++) {
                if(fieldToThrow.get(i) == fieldToThrow.get(j) && i != j){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Dialog");
                    alert.setHeaderText("Cricket Fields must be unique");
                    alert.showAndWait();
                    return false;
                }
            }
        Cricket.setFieldsToThrow(fieldToThrow);
        return true;
    }

    /**
     * Method creates fills VBoxes for field to hit in a cricket game,
     */
    private void fillFieldsToThrowVBox(){
        for (int i = 0; i < 7; i++) {
            Label label = (Label)boardController.getCricketsFields().getChildren().get(i);
            label.setText(String.valueOf(Cricket.getFieldsToThrow().get(i)));
        }
    }

    /**
     * Method creates VBoxes for players in cricketScoreTable at board view,
     * VBoxes contains labels with name and amount of points and fields hits
     */
    public void removeAndCreatePlayersVBoxes(){
        ObservableList<Node> children = boardController.getCricketsScoreTable().getChildren();
        while(children.size() > 1)
            children.remove(1);

        int numberOfPlayers = ((Cricket)gamesRepository.getZeroRound()).getPlayers().size();
        for(int i=0;i<numberOfPlayers;i++){
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.BOTTOM_CENTER);
            vBox.setMinWidth(100);
            for(int j=0;j<7;j++)
                vBox.getChildren().add(createLabel(new String(""),false));

            String playerName = ((Cricket) gamesRepository.getZeroRound()).getPlayers().get(i).getName();
            vBox.getChildren().addAll(createLabel(playerName,false),
                    createLabel(new String("0"),true));
            boardController.getCricketsScoreTable().getChildren().add(vBox);
        }
    }


    /**
     * Method created label for criketPlayersTable VBoxes
     * @param text content of the label
     * @param ifBold if text in label should be bolded
     * @return player label
     */
    private Label createLabel(String text, boolean ifBold){
        Label label = new Label();
        label.setText(text);
        label.setFont(new Font("System",22));
        label.setTextFill(Color.WHITE);
        if(ifBold)
            label.setStyle("-fx-font-weight: bold");
        return label;
    }

    /**
     * Method initialize players array in Cricket object at games repository with Player.class objects
     */
    private void initializeCricketPlayersArray(){
        String[] playersNames = getPlayersNames(getNumberOfPlayers());
        ArrayList<PlayerCricket> players = new ArrayList<>();
        for (String playersName : playersNames) {
            players.add(new PlayerCricket(playersName));
        }
        ((Cricket) gamesRepository.getZeroRound()).setPlayers(players);
        ((Cricket) gamesRepository.getZeroRound()).setCurrentPlayer(players.get(0));
    }


    private void setSpinnersFactoriesAndFilters() {
        SpinnerValueFactory<Integer> svfRounds = new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_ROUNDS,MAX_ROUNDS, DEFAULT_ROUNDS);
        cricketRoundsBox.setValueFactory(svfRounds);
        StringConverter<Integer> roundsConverter = getRoundsConverter();
        svfRounds.setConverter(roundsConverter);
        cricketRoundsBox.getEditor().setTextFormatter(new TextFormatter<>(roundsConverter,DEFAULT_ROUNDS, getRoundsFilter()));

        for(int i=20,j=0;j<7;i--,j++){
            cricketFieldsSpinners[j].setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, i));
        }
    }

    private UnaryOperator<TextFormatter.Change> getRoundsFilter() {
        return new UnaryOperator<TextFormatter.Change>() {

                @Override
                public TextFormatter.Change apply(TextFormatter.Change t) {

                    String newText = t.getControlNewText() ;
                    if (newText.matches("[0-9]*")) {
                        return t ;
                    }
                    return null ;
                }
            };
    }

    private StringConverter<Integer> getRoundsConverter() {
        return new StringConverter<>() {

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
    }


    private void setPlayersNamesLengthValidation() {
        for (TextField game01PlayerNamesTextField : cricketPlayerNamesTextFields) {
            game01PlayerNamesTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.length() > MainController.PLAYERS_NAME_MAX_LENGTH)
                    game01PlayerNamesTextField.setText(newValue.substring(0, MainController.PLAYERS_NAME_MAX_LENGTH));
            });
        }
    }

    private void setPlayersNamesRadioButtonSelectedListener() {
        radioPlayersGroup.selectedToggleProperty().addListener((obs, oldValue, newValue) -> {
            String radioButtonText = ((RadioButton) radioPlayersGroup.getSelectedToggle()).getText();
            int playersQuantity = Integer.parseInt(radioButtonText);
            // Disabling all players text fields
            for (int i = 0; i < 4; i++)
                cricketPlayerNamesTextFields[i].setDisable(true);
            // Setting active players text fields
            for (int i = 0; i < playersQuantity; i++)
                cricketPlayerNamesTextFields[i].setDisable(false);
        });
    }

    private void setRadioRoundsOthersSelectListener() {
        cricketRadioRoundsOther.selectedProperty().addListener((ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) -> {
            if (isNowSelected) {
                cricketRoundsBox.setDisable(false);
            } else {
                cricketRoundsBox.setDisable(true);
            }
        });
    }
}
