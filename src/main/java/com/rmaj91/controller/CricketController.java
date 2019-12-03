package com.rmaj91.controller;

import java.net.URL;
import java.util.LinkedHashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.UnaryOperator;

import com.rmaj91.domain.Cricket;
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
    private Spinner<Integer>[] cricketFieldBoxes;

    public void setBoardController(BoardController boardController) {
        this.boardController = boardController;
    }


    public void toFront() {
    	newCricketPane.toFront();
    }

    public void setGamesRepository(GamesRepository gamesRepository) {
        this.gamesRepository = gamesRepository;
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

    /**
     * Method Initialize board view and games repository
     */
    public void letsPlayButtonClicked() {
        if(!validAndSetFieldsToHit()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Cricket Fields must be unique");
            alert.showAndWait();
            return;
        }

        deletesPlayersVBoxes();
        // In case of startig new game after winning previous
        boardController.getMainStackPane().setDisable(false);
        boardController.toFront();
        boardController.getCricketsScoreTable().toFront();
        boardController.getThrowField1().requestFocus();


        gamesRepository.createNewGame(GameFactory.getGame("Cricket"));

        initializeStaticCricketVariables();
        initializeCricketPlayers();
        validAndSetFieldsToHit();
        fillFieldsToHitVBox(getPlayersQuantity());
        createPlayersVBoxes(getPlayersQuantity());

        gamesRepository.getCurrentRound().displayRound();
    }


    /*Private Methods*/
    /**
     * Method defines number of rounds selected by radio buttons and returns it.
     */
    private int getRounds(){
        if(cricketRadioRoundsOther.isSelected())
            return cricketRoundsBox.getValue();
        else
            return 25;
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

    /**
     * Method validate(in fields are unique) and sets(if values are unique) fields to hit in Criket.class
     * @return true if fields are unique or false if not
     */
    private boolean validAndSetFieldsToHit(){
        int[] fieldToHit = new int[7];

        Set<Integer> set = new LinkedHashSet<>();
        for (int i = 0; i < 7; i++) {
            fieldToHit[i] = cricketFieldBoxes[i].getValue();
            if(i == 6 && cricketCheckBoxField7.isSelected())
                fieldToHit[i] = 25;
        }

        for (int i = 0; i < 7; i++)
            for (int j = 0; j < 7; j++) {
                if(fieldToHit[i] == fieldToHit[j] && i != j)
                    return false;
            }
        Cricket.setFieldsToHit(fieldToHit);
        return true;
    }



    /**
     * Method creates VBoxes for field to hit in a cricket game,
     * VBoxes contains labels with name and amount of points
     * @param playersQuantity quantity of setted players
     */
    private void fillFieldsToHitVBox(int playersQuantity){
        for (int i = 0; i < 7; i++) {
            Label label = (Label)boardController.getCricketsFields().getChildren().get(i);
            label.setText(String.valueOf(Cricket.getFieldsToHit()[i]));
        }
    }



    /**
     * Method creates VBoxes for players in cricketScoreTable at board view,
     * VBoxes contains labels with name and amount of points
     * @param playersQuantity quantity of setted players
     */
    private void createPlayersVBoxes(int playersQuantity){
        for(int i=0;i<playersQuantity;i++){
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.BOTTOM_CENTER);
            vBox.setMinWidth(100);
            for(int j=0;j<7;j++)
                vBox.getChildren().add(createLabel(new String(""),false));

            vBox.getChildren().addAll(createLabel(gamesRepository.getCurrentRound().getPlayers()[i].getName(),false),
                    createLabel(new String("0"),true));
            boardController.getCricketsScoreTable().getChildren().add(vBox);
        }
    }



//    /**
//     * Method creates VBoxes for players in cricketScoreTable at board view,
//     * VBoxes contains labels with name and amount of points
//     * @param playersQuantity quantity of setted players
//     */
    private void deletesPlayersVBoxes(){
        ObservableList<Node> children = boardController.getCricketsScoreTable().getChildren();
        for(int i=0;i<Cricket.getPlayersQuantity();i++)
            children.remove(1);
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
     * Method initialize Cricket.class static variables
     */
    private void initializeStaticCricketVariables(){
        Cricket.setPlayersQuantity(getPlayersQuantity());
        Cricket.setRoundsMaxNumber(getRounds());
    }

    /**
     * Method initialize players array in Cricket object at games repository with Player.class objects
     */
    private void initializeCricketPlayers(){
        // Getting players names
        String[] playersNames = getPlayersNames(getPlayersQuantity());
        // Getting blank players Array
        PlayerCricket[] playersArray = new PlayerCricket[getPlayersQuantity()];
        for(int i=0;i<getPlayersQuantity();i++){
            playersArray[i] = new PlayerCricket();
        }
        // Init players names
        for(int i=0;i<getPlayersQuantity();i++){
            playersArray[i].setName(playersNames[i]);
        }

        // Injecting Array into repository
        ((Cricket)gamesRepository.getCurrentRound()).setPlayers(playersArray);
    }

























	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

        cricketPlayerNamesTextFields = new TextField[4];
        cricketPlayerNamesTextFields[0] = cricketNamePlayer1;
        cricketPlayerNamesTextFields[1] = cricketNamePlayer2;
        cricketPlayerNamesTextFields[2] = cricketNamePlayer3;
        cricketPlayerNamesTextFields[3] = cricketNamePlayer4;

        cricketFieldBoxes = new Spinner[7];
        cricketFieldBoxes[0] = cricketBoxField1;
        cricketFieldBoxes[1] = cricketBoxField2;
        cricketFieldBoxes[2] = cricketBoxField3;
        cricketFieldBoxes[3] = cricketBoxField4;
        cricketFieldBoxes[4] = cricketBoxField5;
        cricketFieldBoxes[5] = cricketBoxField6;
        cricketFieldBoxes[6] = cricketBoxField7;

        // rounds box
        cricketRadioRoundsOther.selectedProperty().addListener((ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) -> {
            if (isNowSelected) {
                cricketRoundsBox.setDisable(false);
            } else {
                cricketRoundsBox.setDisable(true);
            }
        });

        // initializing radio button listeners
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

        // Players names length validation
        for (TextField game01PlayerNamesTextField : cricketPlayerNamesTextFields) {
            game01PlayerNamesTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.length() > MainController.PLAYERS_NAME_MAX_LENGTH)
                    game01PlayerNamesTextField.setText(newValue.substring(0, MainController.PLAYERS_NAME_MAX_LENGTH));
            });
        }

        // Spinners Factories
        SpinnerValueFactory<Integer> svfRounds = new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_ROUNDS,MAX_ROUNDS, DEFAULT_ROUNDS);
        cricketRoundsBox.setValueFactory(svfRounds);

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
        svfRounds.setConverter(roundsConverter);
        cricketRoundsBox.getEditor().setTextFormatter(new TextFormatter<>(roundsConverter,DEFAULT_ROUNDS,roundsFilter));




        for(int i=20,j=0;j<7;i--,j++){
            cricketFieldBoxes[j].setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, i));
        }
//        cricketFieldBoxes[6].getValueFactory().setValue();
//        for (Spinner<Integer> cricketFieldBox : cricketFieldBoxes) {
//            cricketFieldBox.setValueFactory(svfFieldsChoose);
//            cricketFieldBox.getValueFactory().setValue(firstValue--);
//        }




    }





}
