package com.rmaj91.controller;

import com.rmaj91.domain.MasterCricket;
import com.rmaj91.domain.PlayerMasterCricket;
import com.rmaj91.interfaces.GamesRepository;
import com.rmaj91.repository.GameFactory;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class MasterCricketController implements Initializable {

    public final static int MIN_ROUNDS = 1;
    public final static int MAX_ROUNDS = 99;
    public final static int DEFAULT_ROUNDS = 25;

    private BoardController boardController;
    private GamesRepository gamesRepository;
    private WelcomeController welcomeController;


    @FXML
    private VBox newMasterPane;

    @FXML
    private TitledPane roundsGame01Pane2;

    @FXML
    private Spinner<Integer> masterRoundsBox;

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
    private ToggleGroup masterRadioGroupForPlayers;

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
    private Spinner<Integer> masterBoxField1;

    @FXML
    private Spinner<Integer> masterBoxField2;

    @FXML
    private Spinner<Integer> masterBoxField3;

    @FXML
    private Spinner<Integer> masterBoxField4;

    @FXML
    private Spinner<Integer> masterBoxField5;

    @FXML
    private Spinner<Integer> masterBoxField6;

    @FXML
    private CheckBox masterCheckBoxField7;

    @FXML
    private Spinner<Integer> masterBoxField7;

    @FXML
    private Button masterBackButton;

    @FXML
    private Button masterLetsButton;

    private TextField[] masterPlayerNamesTextFields;
    private Spinner<Integer>[] masterFieldsSpinners;



    @FXML
    void masterField7Toggle(ActionEvent event) {
        if (masterCheckBoxField7.isSelected())
            masterBoxField7.setDisable(true);
        else
            masterBoxField7.setDisable(false);
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

















    /**
     * Method Initialize board view and games repository
     */
    public void letsPlayButtonClicked() {
        if (!validFieldsToThrowIfUniqueAndSet())
            return;
        //Creating zero'th game
        gamesRepository.createNewGame(GameFactory.getGame("Master Cricket"));
        MasterCricket.setStaticVariables(getNumberOfPlayers(), getMaxNumberOfRounds(), 0);

        initializeMasterCricketPlayersArray();
        removeAndCreatePlayersVBoxes();
        fillFieldsToThrowVBox();

        MasterCricket firstRound = new MasterCricket((MasterCricket) gamesRepository.getCurrentRound());
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
        initMasterCricketPlayerNamesTextFieldsArray();
        initMasterCricketFieldsSpinnersArray();
        setRadioRoundsOthersSelectListener();
        setPlayersNamesRadioButtonSelectedListener();
        setPlayersNamesLengthValidation();
        setSpinnersFactoriesAndFilters();
    }



    /**
     * Method defines number of rounds selected by radio buttons and returns it.
     */
    private int getMaxNumberOfRounds() {
        if (masterRadioRoundsOther.isSelected())
            return masterRoundsBox.getValue();
        else
            return 25;
    }

    /**
     * Method defines number of players selected by radio buttons and returns it.
     */
    private int getNumberOfPlayers() {
        int playersQuantity = Integer.parseInt(((RadioButton) masterRadioGroupForPlayers.getSelectedToggle()).getText());
        return playersQuantity;
    }

    /**
     * Method gets players names from textFieldsArray and returns it.
     */
    private String[] getPlayersNames(int numberOfPlayers) {
        String[] playersNames = new String[numberOfPlayers];
        for (int i = 0; i < numberOfPlayers; i++) {
            playersNames[i] = getPlayerName(masterPlayerNamesTextFields[i]);
        }
        return playersNames;
    }

    /**
     * Method takes text from textfield and if its empty returns "Player"+"player number"
     * else returns getText() value
     */
    private String getPlayerName(TextField textField) {
        if (textField.getText().trim().equals("")) {
            return "Player" + textField.getId().substring(textField.getId().length() - 1);
        } else
            return textField.getText();
    }
//

    /**
     * Method validate(in fields are unique) and sets(if values are unique) fields to hit in Criket.class
     *
     * @return true if fields are unique or false if not
     */
    private boolean validFieldsToThrowIfUniqueAndSet() {

        ArrayList<Integer> fieldToThrow = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            fieldToThrow.add(masterFieldsSpinners[i].getValue());
            if (i == 6 && masterCheckBoxField7.isSelected())
                fieldToThrow.add(25);
        }

        for (int i = 0; i < 7; i++)
            for (int j = 0; j < 7; j++) {
                if (fieldToThrow.get(i) == fieldToThrow.get(j) && i != j) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Dialog");
                    alert.setHeaderText("Master cricket Fields must be unique");
                    alert.showAndWait();
                    return false;
                }
            }
        MasterCricket.setFieldsToThrow(fieldToThrow);
        return true;
    }

    /**
     * Method creates fills VBoxes for field to hit in a master cricket game,
     */
    private void fillFieldsToThrowVBox() {
        for (int i = 0; i < 7; i++) {
            Label label = (Label) boardController.getCricketsFields().getChildren().get(i);
            label.setText(String.valueOf(MasterCricket.getFieldsToThrow().get(i)));
        }
    }

    /**
     * Method creates VBoxes for players in cricketScoreTable at board view,
     * VBoxes contains labels with name and amount of points and fields hits
     */
    public void removeAndCreatePlayersVBoxes() {
        ObservableList<Node> children = boardController.getCricketsScoreTable().getChildren();
        while (children.size() > 1)
            children.remove(1);

        int numberOfPlayers = ((MasterCricket) gamesRepository.getZeroRound()).getPlayers().size();
        for (int i = 0; i < numberOfPlayers; i++) {
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.BOTTOM_CENTER);
            vBox.setMinWidth(100);
            for (int j = 0; j < 7; j++)
                vBox.getChildren().add(createLabel(new String(""), false));

            String playerName = ((MasterCricket) gamesRepository.getZeroRound()).getPlayers().get(i).getName();
            vBox.getChildren().addAll(createLabel(playerName, false),
                    createLabel(new String("0"), true));
            boardController.getCricketsScoreTable().getChildren().add(vBox);
        }
    }



    /**
     * Method created label for criketPlayersTable VBoxes
     *
     * @param text   content of the label
     * @param ifBold if text in label should be bolded
     * @return player label
     */
    private Label createLabel(String text, boolean ifBold) {
        Label label = new Label();
        label.setText(text);
        label.setFont(new Font("System", 22));
        label.setTextFill(Color.WHITE);
        if (ifBold)
            label.setStyle("-fx-font-weight: bold");
        return label;
    }

    /**
     * Method initialize players array in master cricket object at games repository with Player.class objects
     */
    private void initializeMasterCricketPlayersArray() {
        String[] playersNames = getPlayersNames(getNumberOfPlayers());
        ArrayList<PlayerMasterCricket> players = new ArrayList<>();
        for (String playersName : playersNames) {
            players.add(new PlayerMasterCricket(playersName));
        }
        ((MasterCricket) gamesRepository.getZeroRound()).setPlayers(players);
        ((MasterCricket) gamesRepository.getZeroRound()).setCurrentPlayer(players.get(0));
    }

    private void initMasterCricketFieldsSpinnersArray() {
        masterFieldsSpinners = new Spinner[7];
        masterFieldsSpinners[0] = masterBoxField1;
        masterFieldsSpinners[1] = masterBoxField2;
        masterFieldsSpinners[2] = masterBoxField3;
        masterFieldsSpinners[3] = masterBoxField4;
        masterFieldsSpinners[4] = masterBoxField5;
        masterFieldsSpinners[5] = masterBoxField6;
        masterFieldsSpinners[6] = masterBoxField7;
    }

    private void initMasterCricketPlayerNamesTextFieldsArray() {
        masterPlayerNamesTextFields = new TextField[4];
        masterPlayerNamesTextFields[0] = masterNamePlayer1;
        masterPlayerNamesTextFields[1] = masterNamePlayer2;
        masterPlayerNamesTextFields[2] = masterNamePlayer3;
        masterPlayerNamesTextFields[3] = masterNamePlayer4;
    }


    private void setSpinnersFactoriesAndFilters() {
        SpinnerValueFactory<Integer> svfRounds = new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_ROUNDS, MAX_ROUNDS, DEFAULT_ROUNDS);
        masterRoundsBox.setValueFactory(svfRounds);
        StringConverter<Integer> roundsConverter = getRoundsConverter();
        svfRounds.setConverter(roundsConverter);
        masterRoundsBox.getEditor().setTextFormatter(new TextFormatter<>(roundsConverter, DEFAULT_ROUNDS, getRoundsFilter()));

        for (int i = 20, j = 0; j < 7; i--, j++) {
            masterFieldsSpinners[j].setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, i));
        }
    }

    private UnaryOperator<TextFormatter.Change> getRoundsFilter() {
        return new UnaryOperator<TextFormatter.Change>() {

            @Override
            public TextFormatter.Change apply(TextFormatter.Change t) {

                String newText = t.getControlNewText();
                if (newText.matches("[0-9]*")) {
                    return t;
                }
                return null;
            }
        };
    }

    private StringConverter<Integer> getRoundsConverter() {
        return new StringConverter<>() {

            @Override
            public Integer fromString(String string) {
                if (string.isEmpty())
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
        for (TextField game01PlayerNamesTextField : masterPlayerNamesTextFields) {
            game01PlayerNamesTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.length() > MainController.PLAYERS_NAME_MAX_LENGTH)
                    game01PlayerNamesTextField.setText(newValue.substring(0, MainController.PLAYERS_NAME_MAX_LENGTH));
            });
        }
    }

    private void setPlayersNamesRadioButtonSelectedListener() {
        masterRadioGroupForPlayers.selectedToggleProperty().addListener((obs, oldValue, newValue) -> {
            String radioButtonText = ((RadioButton) masterRadioGroupForPlayers.getSelectedToggle()).getText();
            int playersQuantity = Integer.parseInt(radioButtonText);
            // Disabling all players text fields
            for (int i = 0; i < 4; i++)
               masterPlayerNamesTextFields[i].setDisable(true);
            // Setting active players text fields
            for (int i = 0; i < playersQuantity; i++)
                masterPlayerNamesTextFields[i].setDisable(false);
        });
    }

    private void setRadioRoundsOthersSelectListener() {
        masterRadioRoundsOther.selectedProperty().addListener((ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) -> {
            if (isNowSelected) {
                masterRoundsBox.setDisable(false);
            } else {
                masterRoundsBox.setDisable(true);
            }
        });
    }


    public void setBoardController(BoardController boardController) {
        this.boardController = boardController;
    }

    public void setGamesRepository(GamesRepository gamesRepository) {
        this.gamesRepository = gamesRepository;
    }
}







