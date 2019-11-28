//package com.rmaj91.controller;
//
//import java.net.URL;
//import java.util.ResourceBundle;
//import java.util.function.UnaryOperator;
//
//import com.rmaj91.Main;
//import com.rmaj91.domain.Game01;
//import com.rmaj91.repository.GameFactory;
//import javafx.beans.value.ObservableValue;
//
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.geometry.Pos;
//import javafx.scene.control.*;
//import javafx.scene.layout.VBox;
//import javafx.scene.paint.Color;
//import javafx.scene.text.Font;
//import javafx.util.StringConverter;
//
//public class Game01Controller implements Initializable{
//
//    public final static int MIN_POINTS=100;
//    public final static int MAX_POINTS=999;
//    public final static int DEFAULT_POINTS=501;
//
//    public final static int MIN_ROUNDS=1;
//    public final static int MAX_ROUNDS=99;
//    public final static int DEFAULT_ROUNDS=25;
//
//    @FXML
//    private VBox newGame01Pane;
//
//    @FXML
//    private TitledPane pointsGame01Pane;
//
//    @FXML
//    private RadioButton game01RadioPoints501;
//
//    @FXML
//    private ToggleGroup group01_1;
//
//    @FXML
//    private RadioButton game01RadioPoints701;
//
//    @FXML
//    private RadioButton game01RadioPointsOther;
//
//    @FXML
//    private Spinner<Integer> game01PointsBox;
//
//    @FXML
//    private TitledPane roundsGame01Pane;
//
//    @FXML
//    private Spinner<Integer> game01RoundsBox;
//
//    @FXML
//    private RadioButton game01RadioRounds25;
//
//    @FXML
//    private ToggleGroup group01_2;
//
//    @FXML
//    private RadioButton game01RadioRoundsOther;
//
//    @FXML
//    private TitledPane playersGame01Pane;
//
//    @FXML
//    private RadioButton game01RadioPlayers1;
//
//    @FXML
//    private ToggleGroup group01_3;
//
//    @FXML
//    private RadioButton game01RadioPlayers2;
//
//    @FXML
//    private RadioButton game01RadioPlayers3;
//
//    @FXML
//    private RadioButton game01RadioPlayers4;
//
//    @FXML
//    private CheckBox game01DoubleOut;
//
//    @FXML
//    private TextField game01NamePlayer1;
//
//    @FXML
//    private TextField game01NamePlayer2;
//
//    @FXML
//    private TextField game01NamePlayer3;
//
//    @FXML
//    private TextField game01NamePlayer4;
//
//    @FXML
//    private Button game01BackButton;
//
//    @FXML
//    private Button game01LetsButton;
//
//    ///////////////////////////////////////////////////////////////
//    ///////////////////////////////////////////////////////////////
//    //
//    ///////////////////////////////////////////////////////////////
//    ///////////////////////////////////////////////////////////////
//
//
//    public void toFront() {
//        newGame01Pane.toFront();
//    }
//
//    private WelcomeController welcomeController;
//
//    public void setWelcomeController(WelcomeController welcomeController) {
//        this.welcomeController = welcomeController;
//    }
//
//
//    private BoardController boardController;
//
//    public void backButton() {
//        welcomeController.toFront();
//    }
//
//    public void setBoardController(BoardController boardController) {
//        this.boardController = boardController;
//    }
//
//
//
//    public void letsPlayButtonClicked() {
//        boardController.toFront();
//        boardController.getGame01ScoreTable().toFront();
//        displayDoubleOutLabel();
//        Main.gamesRepositoty.createNew(GameFactory.getGame("'01 Game"));
//        initializeStaticGame01Variables();
//        createPlayersHBoxes(getPlayersQuantity());
//        Main.gamesRepositoty.getCurrentGame().display();
//    }
//
//
//    ///////////////////////////////////////////////////////////////////
//    //                      INITIALIZING!
//    ///////////////////////////////////////////////////////////////////
//
//
//    @Override
//    public void initialize(URL arg0, ResourceBundle arg1) {
//        // points box
//        game01RadioPointsOther.selectedProperty().addListener((ObservableValue<?extends Boolean> obs,Boolean wasPreviouslySelected, Boolean isNowSelected)->{
//            if(isNowSelected) {
//                game01PointsBox.setDisable(false);
//            }else {
//                game01PointsBox.setDisable(true);
//            }
//        });
//        // rounds box
//        game01RadioRoundsOther.selectedProperty().addListener((ObservableValue<?extends Boolean> obs,Boolean wasPreviouslySelected, Boolean isNowSelected)->{
//            if(isNowSelected) {
//                game01RoundsBox.setDisable(false);
//            }else {
//                game01RoundsBox.setDisable(true);
//            }
//        });
//
//        // player boxes 2
//        game01RadioPlayers2.selectedProperty().addListener((ObservableValue<?extends Boolean> obs,Boolean wasPreviouslySelected, Boolean isNowSelected)->{
//            if(isNowSelected) {
//                game01NamePlayer2.setDisable(false);
//            }else {
//                game01NamePlayer2.setDisable(true);
//            }
//        });
//        // player boxes 3
//        game01RadioPlayers3.selectedProperty().addListener((ObservableValue<?extends Boolean> obs,Boolean wasPreviouslySelected, Boolean isNowSelected)->{
//            if(isNowSelected) {
//                game01NamePlayer2.setDisable(false);
//                game01NamePlayer3.setDisable(false);
//            }else {
//                game01NamePlayer2.setDisable(false);
//                game01NamePlayer3.setDisable(true);
//            }
//        });
//        // player boxes 4
//        game01RadioPlayers4.selectedProperty().addListener((ObservableValue<?extends Boolean> obs,Boolean wasPreviouslySelected, Boolean isNowSelected)->{
//            if(isNowSelected) {
//                game01NamePlayer2.setDisable(false);
//                game01NamePlayer3.setDisable(false);
//                game01NamePlayer4.setDisable(false);
//            }else {
//                game01NamePlayer2.setDisable(true);
//                game01NamePlayer3.setDisable(true);
//                game01NamePlayer4.setDisable(true);
//            }
//        });
//        // Textfields listener //
//        /////////////////////////
//        game01NamePlayer1.textProperty().addListener((observable,oldValue,newValue) ->{
//            if(newValue.length() > 8)
//                game01NamePlayer1.setText(newValue.substring(0, 8));
//        });
//        game01NamePlayer2.textProperty().addListener((observable,oldValue,newValue) ->{
//            if(newValue.length() > 8)
//                game01NamePlayer2.setText(newValue.substring(0, 8));
//        });
//        game01NamePlayer3.textProperty().addListener((observable,oldValue,newValue) ->{
//            if(newValue.length() > 8)
//                game01NamePlayer3.setText(newValue.substring(0, 8));
//        });
//        game01NamePlayer4.textProperty().addListener((observable,oldValue,newValue) ->{
//            if(newValue.length() > 8)
//                game01NamePlayer4.setText(newValue.substring(0, 8));
//        });
//
//        //////////////////////////////
//        // game points spinner
//        //////////////////////////////
//
//        SpinnerValueFactory<Integer> svfPoints = new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_POINTS, MAX_POINTS, DEFAULT_POINTS);
//        SpinnerValueFactory<Integer> svfRounds = new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_ROUNDS,MAX_ROUNDS, DEFAULT_ROUNDS);
//        game01PointsBox.setValueFactory(svfPoints);
//        game01RoundsBox.setValueFactory(svfRounds);
//
//        // POINTS VALIDATION //
//        StringConverter<Integer> pointsConverter = new StringConverter<>() {
//
//            @Override
//            public Integer fromString(String string) {
//                if(string.isEmpty())
//                    return DEFAULT_POINTS;
//                else
//                    return Integer.parseInt(string);
//            }
//
//            @Override
//            public String toString(Integer object) {
//                return object.toString();
//            }
//
//        };
//
//        UnaryOperator<TextFormatter.Change> pointsFilter = new UnaryOperator<TextFormatter.Change>() {
//
//            @Override
//            public TextFormatter.Change apply(TextFormatter.Change t) {
//
//                String newText = t.getControlNewText() ;
//                if (newText.matches("[0-9]*")) {
//                    return t ;
//                }
//                return null ;
//            }
//        };
//
//        // ROUnds VALIDATION //
//        StringConverter<Integer> roundsConverter = new StringConverter<>() {
//
//            @Override
//            public Integer fromString(String string) {
//                if(string.isEmpty())
//                    return DEFAULT_ROUNDS;
//                else
//                    return Integer.parseInt(string);
//            }
//
//            @Override
//            public String toString(Integer object) {
//                return object.toString();
//            }
//
//        };
//
//        UnaryOperator<TextFormatter.Change> roundsFilter = new UnaryOperator<TextFormatter.Change>() {
//
//            @Override
//            public TextFormatter.Change apply(TextFormatter.Change t) {
//
//                String newText = t.getControlNewText() ;
//                if (newText.matches("[0-9]*")) {
//                    return t ;
//                }
//                return null ;
//            }
//        };
//
//        svfPoints.setConverter(pointsConverter);
//        game01PointsBox.getEditor().setTextFormatter(new TextFormatter<>(pointsConverter,DEFAULT_POINTS,pointsFilter));
//        svfRounds.setConverter(roundsConverter);
//        game01RoundsBox.getEditor().setTextFormatter(new TextFormatter<>(roundsConverter,DEFAULT_ROUNDS,roundsFilter));
//
//
//
//    }
//
//
//
//
//    ////////////////////////////////////////////////////////////////
//    ////////////////////////////////////////////////////////////////
//    //                       PRIVATE METHODS
//    ////////////////////////////////////////////////////////////////
//    ////////////////////////////////////////////////////////////////
//
//    /*
//     * Returns number of rounds defined by player
//     * */
//    private int getRounds(){
//        if(game01RadioRoundsOther.isSelected())
//            return game01RoundsBox.getValue();
//        else
//            return 25;
//    }
//
//    /*
//     * Returns number of starting points defined by player
//     * */
//    private int getStartingPoints(){
//        if(game01RadioPointsOther.isSelected())
//            return game01PointsBox.getValue();
//        else if(game01RadioPoints501.isSelected())
//            return 501;
//        else
//            return 701;
//    }
//
//    /*
//     * Returns number of players defined by player
//     * */
//    private int getPlayersQuantity(){
//        if(game01RadioPlayers1.isSelected())
//            return 1;
//        else if(game01RadioPlayers2.isSelected())
//            return 2;
//        else if(game01RadioPlayers3.isSelected())
//            return 3;
//        else
//            return 4;
//    }
//
//    /*
//     * Returns players names defines by player
//     * */
//    private String[] getPlayersNames(int playersQuantity){
//        String[] playersNames = new String[playersQuantity];
//        // TODO Add limit size of textField and defaultValues or emptyProtection
//        if(playersQuantity >= 1)
//            playersNames[0] = getPlayerName(game01NamePlayer1);
//        if(playersQuantity >= 2)
//            playersNames[1] = getPlayerName(game01NamePlayer2);
//        if(playersQuantity >= 3)
//            playersNames[2] = getPlayerName(game01NamePlayer3);
//        if(playersQuantity == 4)
//            playersNames[3] = getPlayerName(game01NamePlayer4);
//        return playersNames;
//    }
//
//    private String getPlayerName(TextField textField){
//        if(textField.getText().trim().equals("")){
//            return "Player"+textField.getId().substring(textField.getId().length()-1);
//        }
//        else
//            return textField.getText();
//    }
//
//    private boolean isDoubleOut(){
//        if(game01DoubleOut.isSelected())
//            return true;
//        else
//            return false;
//    }
//
//    private void initializeStaticGame01Variables(){
//        Game01.setRounds(getRounds());
//        Game01.setPlayersQuantity(getPlayersQuantity());
//        Game01.setDoubleOut(isDoubleOut());
//    }
//
//    private String[] initializeGame01PlayersNames(){
//        String[] players = getPlayersNames(getPlayersQuantity());
            // to mozna chyba przypisac tabele do tabeli
//        for (String playerName : players) {
//            ((Game01) Main.gamesRepositoty.getGame(0)).addPlayer(new Game01.Player(playerName,getStartingPoints()));
//        }
//        return players;
//    }
//
//    private void createPlayersHBoxes(int playersQuantity){
//
//        // initializing player Names
//        String[] playersNames = initializeGame01PlayersNames();
//
//        for(int i=0;i<playersQuantity;i++){
//            VBox vBox = new VBox();
//            vBox.setAlignment(Pos.BOTTOM_CENTER);
//            vBox.setMinWidth(100);
//
//            vBox.getChildren().addAll(createPlayerLabel(playersNames[i],false),
//                    createPlayerLabel(String.valueOf(getStartingPoints()),true));
//            boardController.getGame01PlayersTable().getChildren().add(vBox);
//        }
//    }
//
//    private Label createPlayerLabel(String text,boolean ifBold){
//        Label playerLabel = new Label();
//        playerLabel.setText(text);
//        playerLabel.setFont(new Font("System",22));
//        playerLabel.setTextFill(Color.WHITE);
//        if(ifBold)
//            playerLabel.setStyle("-fx-font-weight: bold");
//        return playerLabel;
//    }
//
//    private void displayDoubleOutLabel(){
//        if(isDoubleOut())
//            boardController.getDoubleOut().setVisible(true);
//        else
//            boardController.getDoubleOut().setVisible(false);
//    }
//}
