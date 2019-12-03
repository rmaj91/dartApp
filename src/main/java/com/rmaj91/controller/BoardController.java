package com.rmaj91.controller;

import com.rmaj91.domain.Game01;
import com.rmaj91.domain.Point;
import com.rmaj91.repository.GamesRepositoryImpl;
import com.rmaj91.utility.SoundPlayer;
import com.rmaj91.utility.Utilities;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.rmaj91.utility.Utilities.filters;
import static com.rmaj91.utility.Utilities.getRadius;
import static com.rmaj91.utility.Utilities.getAngle;

public class BoardController implements Initializable {

    /*Dependencies*/
    private SoundPlayer soundPlayer;
    private GamesRepositoryImpl gamesRepository;

    /*Javafx stuff*/
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

    @FXML
    private ImageView dartBoard;

    @FXML
    private Canvas canvas;

    @FXML
    private Canvas drawedBoard;

    TextField[] throwTextFieldArray;
    private GraphicsContext graphicsContext2DHighlight;
    private PixelWriter pixelWriterHighlight;
    private GraphicsContext graphicsContext2DDrawed;
    private PixelWriter pixelWriterDrawed;

    // Variables for dart boards highlighting
    private int currentRadiusIndex = 7;
    private int currentAngleIndex = 20;
    List<Point> pointList = new ArrayList<>();

    /*Getters & Setters*/
    public TextField[] getThrowTextFieldArray() {
        return throwTextFieldArray;
    }

    public StackPane getMainStackPane() {
        return mainStackPane;
    }

    public Label getAverageLabel() {
        return averageLabel;
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

    public void setSoundPlayer(SoundPlayer soundPlayer) {
        this.soundPlayer = soundPlayer;
    }

    public VBox getStatusPlayer1() {
        return statusPlayer1;
    }

    public void setGamesRepository(GamesRepositoryImpl gamesRepository) {
        this.gamesRepository = gamesRepository;
    }

    /*Events*/
    public void nextButtonClicked(){
        gamesRepository.getCurrentRound().next();
    }

    public void backButtonClicked(){
        gamesRepository.getCurrentRound().back();
    }

    public void dartBoardClicked(MouseEvent event) {
        gamesRepository.getCurrentRound().throwDart(event);
    }

    public void toFront() {
        mainStackPane.toFront();
    }

    public void onClickThrowField1(MouseEvent event){
        throwField1.selectAll();
        gamesRepository.getCurrentRound().setCurrentThrow(1);
    }
    public void onClickThrowField2(){
        throwField2.selectAll();
        gamesRepository.getCurrentRound().setCurrentThrow(2);
    }
    public void onClickThrowField3(){
        throwField3.selectAll();
        gamesRepository.getCurrentRound().setCurrentThrow(3);
    }

    /**
     * Method clears highligh when cursor leaves board
     */
    public void boardHoverOff() {
        graphicsContext2DHighlight.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        currentRadiusIndex = 7;
        currentAngleIndex = 20;
    }

    /**
     * Method contains algorith for highlighting dart boards fields when hover
     * @param event Mouse event
     */
    public void boardHover2(MouseEvent event){

        // New fields indexes
        int hoverRadiusIndex = Utilities.getRadiusScope(getRadius(getX(event),getY(event)));
        int hoverAngleIndex = Utilities.getAngleScope(getAngle(getX(event),getY(event)));
        // Checking if mouse leaves current field
        if(hoverRadiusIndex == currentRadiusIndex && hoverAngleIndex == currentAngleIndex)
            return;
        // Saving new field indexes
        currentRadiusIndex = hoverRadiusIndex;
        currentAngleIndex = hoverAngleIndex;
        // Clearing previous highlighting
        graphicsContext2DHighlight.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Filtering pixels for highlighting field
        pointList.stream()
                .filter(point ->{
                    int xCartesian = calculateCartesianX(point.getxCoordinate());
                    int yCartesian = calculateCartesianY(point.getyCoordinate());
                    int radius = getRadius(xCartesian, yCartesian);
                    return filters.getRadiusMapperList().get(hoverRadiusIndex).isInRange(radius);
                })
                .filter(point -> {
                    // id cursor is out of this radius, filter is off
                    if(getRadius(getX(event),getY(event)) < 19 || getRadius(getX(event),getY(event)) > 205)
                        return true;
                    else{
                        int xCartesian = calculateCartesianX(point.getxCoordinate());
                        int yCartesian = calculateCartesianY(point.getyCoordinate());
                        double angle = getAngle(xCartesian, yCartesian);
                        return filters.getAngleMapperList().get(hoverAngleIndex).isInRange(angle); } })
                .forEach(point -> {
                    displayPixel(point.getxCoordinate(),point.getyCoordinate()); });
    }

    /**
     * displaying highlight pixel on x,y coordinates
     */
    private void displayPixel(int x,int y){
        pixelWriterHighlight.setColor(x, y, Color.rgb(66, 135, 245));
    }

    private int calculateCartesianX(int x){
        return x-248;
    }

    private int calculateCartesianY(int y){
        return -(y-248);
    }

    private double getX(MouseEvent event){
        return event.getX() - 248;
    }

    private   double getY(MouseEvent event){
        return -(event.getY() - 248.5);
    }


    /**
     * Initializing: drawing dart board and some variables init.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        throwTextFieldArray = new TextField[3];
        throwTextFieldArray[0] = throwField1;
        throwTextFieldArray[1] = throwField2;
        throwTextFieldArray[2] = throwField3;

        // ThrowTextField length validation
        for (int i = 0; i < 3; i++) {
            throwTextFieldArray[i].textProperty().addListener((observable,oldValue,newValue) ->{
                if(newValue.length() > 10)
                    throwField1.setText(newValue.substring(0, 10));
                gamesRepository.getCurrentRound().calculatePoints();
            });
        }

        // Stuff for drawing dart board
        canvas.setOpacity(0.75);
        this.graphicsContext2DHighlight = canvas.getGraphicsContext2D();
        this.pixelWriterHighlight = graphicsContext2DHighlight.getPixelWriter();

        this.graphicsContext2DDrawed = drawedBoard.getGraphicsContext2D();
        this.pixelWriterDrawed = graphicsContext2DDrawed.getPixelWriter();

        // Points list init
        for(int y=0;y<canvas.getHeight();y++){
            for(int x=0;x<canvas.getWidth();x++)
                pointList.add(new Point(x,y));
        }

        Color redColor = Color.rgb(229, 9, 9);
        Color greenColor = Color.rgb(25, 137, 9);

        /*Drawing dart board*/
        // Radius < 204 BackGROUND
        pointList.stream()
                .filter(point -> getRadius(calculateCartesianX(point.getxCoordinate()), calculateCartesianY(point.getyCoordinate())) < 204)
                .forEach(point ->  pixelWriterDrawed.setColor(point.getxCoordinate(),point.getyCoordinate(),Color.WHITE) );

        // Angle, NOT EVEN indexes
        pointList.stream()
                .filter(point -> getRadius(calculateCartesianX(point.getxCoordinate()), calculateCartesianY(point.getyCoordinate())) < 204)
                .filter(point -> {
                    for(int i=0;i<20;i+=2){
                        if(filters.getAngleMapperList().get(i).isInRange(getAngle(calculateCartesianX(point.getxCoordinate()), calculateCartesianY(point.getyCoordinate()))))
                            return true;
                    }
                    return false;
                })
                .forEach(point -> pixelWriterDrawed.setColor(point.getxCoordinate(),point.getyCoordinate(),Color.BLACK));

        // SINGLE BULL
        pointList.stream()
                .filter(point -> getRadius(calculateCartesianX(point.getxCoordinate()), calculateCartesianY(point.getyCoordinate())) < 19)
                .forEach(point ->  pixelWriterDrawed.setColor(point.getxCoordinate(),point.getyCoordinate(),greenColor) );

        // DOUBLE BULL
        pointList.stream()
                .filter(point -> getRadius(calculateCartesianX(point.getxCoordinate()), calculateCartesianY(point.getyCoordinate())) < 9)
                .forEach(point ->  pixelWriterDrawed.setColor(point.getxCoordinate(),point.getyCoordinate(),redColor) );

        // Radius < 205 && Radius > 195 EVEN indexes
        pointList.stream()
                .filter(point -> getRadius(calculateCartesianX(point.getxCoordinate()), calculateCartesianY(point.getyCoordinate())) < 205)
                .filter(point -> getRadius(calculateCartesianX(point.getxCoordinate()), calculateCartesianY(point.getyCoordinate())) >= 194)
                .filter(point -> {
                    for(int i=0;i<20;i+=2){
                        if(filters.getAngleMapperList().get(i).isInRange(getAngle(calculateCartesianX(point.getxCoordinate()), calculateCartesianY(point.getyCoordinate()))))
                            return true;
                    }
                    return false;
                })
                .forEach(point -> pixelWriterDrawed.setColor(point.getxCoordinate(),point.getyCoordinate(),redColor));

        // Radius < 205 && Radius > 195 not EVEN indexes
        pointList.stream()
                .filter(point -> getRadius(calculateCartesianX(point.getxCoordinate()), calculateCartesianY(point.getyCoordinate())) < 205)
                .filter(point -> getRadius(calculateCartesianX(point.getxCoordinate()), calculateCartesianY(point.getyCoordinate())) >= 194)
                .filter(point -> {
                    for(int i=1;i<20;i+=2){
                        if(filters.getAngleMapperList().get(i).isInRange(getAngle(calculateCartesianX(point.getxCoordinate()), calculateCartesianY(point.getyCoordinate()))))
                            return true;
                    }
                    return false;
                })
                .forEach(point -> pixelWriterDrawed.setColor(point.getxCoordinate(),point.getyCoordinate(),greenColor));

        // Radius < 127 && Radius > 118 EVEN indexes
        pointList.stream()
                .filter(point -> getRadius(calculateCartesianX(point.getxCoordinate()), calculateCartesianY(point.getyCoordinate())) < 127)
                .filter(point -> getRadius(calculateCartesianX(point.getxCoordinate()), calculateCartesianY(point.getyCoordinate())) >= 117)
                .filter(point -> {
                    for(int i=0;i<20;i+=2){
                        if(filters.getAngleMapperList().get(i).isInRange(getAngle(calculateCartesianX(point.getxCoordinate()), calculateCartesianY(point.getyCoordinate()))))
                            return true;
                    }
                    return false;
                })
                .forEach(point -> pixelWriterDrawed.setColor(point.getxCoordinate(),point.getyCoordinate(),redColor));

        // Radius < 127 && Radius > 118 not EVEN indexes
        pointList.stream()
                .filter(point -> getRadius(calculateCartesianX(point.getxCoordinate()), calculateCartesianY(point.getyCoordinate())) < 127)
                .filter(point -> getRadius(calculateCartesianX(point.getxCoordinate()), calculateCartesianY(point.getyCoordinate())) >= 117)
                .filter(point -> {
                    for(int i=1;i<20;i+=2){
                        if(filters.getAngleMapperList().get(i).isInRange(getAngle(calculateCartesianX(point.getxCoordinate()), calculateCartesianY(point.getyCoordinate()))))
                            return true;
                    }
                    return false;
                })
                .forEach(point -> pixelWriterDrawed.setColor(point.getxCoordinate(),point.getyCoordinate(),greenColor));

        // Black borders
        int[] radiuses = {8,19,116,126,194};
        for (int radius : radiuses) {
            pointList.stream()
                    .filter(point -> getRadius(calculateCartesianX(point.getxCoordinate()), calculateCartesianY(point.getyCoordinate())) == radius)
                    .forEach(point ->  pixelWriterDrawed.setColor(point.getxCoordinate(),point.getyCoordinate(),Color.BLACK) );
        }

        // Fixing custom board edges :)
        pointList.stream()
                .filter(point -> getRadius(calculateCartesianX(point.getxCoordinate()), calculateCartesianY(point.getyCoordinate())) > 204)
                .filter(point -> getRadius(calculateCartesianX(point.getxCoordinate()), calculateCartesianY(point.getyCoordinate())) < 206)
                .forEach(point ->  pixelWriterDrawed.setColor(point.getxCoordinate(),point.getyCoordinate(),Color.BLACK) );

        // Fixing "angle" borders
        pointList.stream()
                .filter(point -> getRadius(calculateCartesianX(point.getxCoordinate()), calculateCartesianY(point.getyCoordinate())) > 18)
                .filter(point -> getRadius(calculateCartesianX(point.getxCoordinate()), calculateCartesianY(point.getyCoordinate())) < 205)
                .filter(point -> {
                    for(int i=0;i<20;i++){
                        if(filters.getAngleMapperList().get(i).getFirstAngle()
                                == getAngle(calculateCartesianX(point.getxCoordinate()), calculateCartesianY(point.getyCoordinate()))
                        || filters.getAngleMapperList().get(i).getSecondAngle()
                                == getAngle(calculateCartesianX(point.getxCoordinate()), calculateCartesianY(point.getyCoordinate())))
                            return true;
                    }
                    return false;
                })
                .forEach(point -> pixelWriterDrawed.setColor(point.getxCoordinate(),point.getyCoordinate(),Color.BLACK));

    }

}
