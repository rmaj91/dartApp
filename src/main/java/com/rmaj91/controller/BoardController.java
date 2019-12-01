package com.rmaj91.controller;

import com.rmaj91.Main;
import com.rmaj91.domain.Point;
import com.rmaj91.utility.Filters;
import com.rmaj91.utility.Utility;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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


import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BoardController implements Initializable {

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

    public TextField[] getThrowTextFieldArray() {
        return throwTextFieldArray;
    }

    public StackPane getMainStackPane() {
        return mainStackPane;
    }

    public Label getAverageLabel() {
        return averageLabel;
    }


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
    public void boardClicked(MouseEvent event) {
        Main.gamesRepositotyImpl.getCurrentRound().throwDart(event);
    }
    // Onclick button next
    public void nextButtonClicked(){
        Main.gamesRepositotyImpl.getCurrentRound().next();
    }
    //Onclick Button back
    public void backButtonClicked(){
        Main.gamesRepositotyImpl.getCurrentRound().back();
    }

    ////////////////////////////////////////////
    // dart fields ALGORITHM ///////////////////
    ////////////////////////////////////////////
    List<Point> pointList = new ArrayList<>();

    private int currentRadiusIndex = -1;
    private int currentAngleIndex = -1;

    public void boardHover2(MouseEvent event){

        int hoverRadiusIndex = Utility.filters.getRadiusScope(getRadius(getX(event),getY(event)));
        int hoverAngleIndex = Utility.filters.getAngleScope(getAngle(getX(event),getY(event)));
        if(hoverRadiusIndex == currentRadiusIndex && hoverAngleIndex == currentAngleIndex)
            return;
        currentRadiusIndex = hoverRadiusIndex;
        currentAngleIndex = hoverAngleIndex;

        graphicsContext2DHighlight.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        pointList.stream()
                .filter(point -> {
                    //int indexRangeScope = Utility.filters.getRadiusScope(getRadius(getX(event),getY(event)));
                    return Utility.filters.getRadiusList().get(hoverRadiusIndex).isInRange(getRadius(getCenterX(point.getX())
                            , getCenterY(point.getY()))); })
                .filter(point -> {
                    if(getRadius(getX(event),getY(event)) < 19 || getRadius(getX(event),getY(event)) > 205)
                        return true;
                    else{
                        //int indexAngleScope = Utility.filters.getAngleScope(getAngle(getX(event),getY(event)));
                        return Utility.filters.getAngleList().get(hoverAngleIndex).isInRange(getAngle(getCenterX(point.getX())
                                , getCenterY(point.getY()))); } })
                .forEach(point -> {
                    displayPixel(point.getX(),point.getY()); });
    }

    public int getRadius(double x, double y){
        return (int)Math.sqrt(x*x+y*y);
    }
    public void displayPixel(int x,int y){
        pixelWriterHighlight.setColor(x, y, Color.rgb(66, 135, 245));
    }

    public int getCenterX(int x){
        return x-248;
    }

    public int getCenterY(int y){
        return -(y-248);
    }

    public double getX(MouseEvent event){
        return event.getX() - 248;
    }

    public  double getY(MouseEvent event){
        return -(event.getY() - 248.5);
    }

    public double getAngle(double x, double y){
        double angle=Math.atan2(y,x);
        angle = Math.toDegrees(angle);
        if(angle<0)
            angle+=360;
        return angle;
    }

    public void boardHoverOff() {
        graphicsContext2DHighlight.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        currentRadiusIndex = -1;
        currentAngleIndex = -1;
    }


    // todo dartboard hints
    // todo serialize, save/load
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        canvas.setOpacity(0.75);
        this.graphicsContext2DHighlight = canvas.getGraphicsContext2D();
        this.pixelWriterHighlight = graphicsContext2DHighlight.getPixelWriter();

        this.graphicsContext2DDrawed = drawedBoard.getGraphicsContext2D();
        this.pixelWriterDrawed = graphicsContext2DDrawed.getPixelWriter();
        // points list init
        for(int y=0;y<canvas.getHeight();y++){
            for(int x=0;x<canvas.getWidth();x++)
                pointList.add(new Point(x,y));
        }

        throwTextFieldArray = new TextField[3];
        throwTextFieldArray[0] = throwField1;
        throwTextFieldArray[1] = throwField2;
        throwTextFieldArray[2] = throwField3;

        // adding textField validation and save()
        throwField1.textProperty().addListener((observable,oldValue,newValue) ->{
            if(newValue.length() > 10)
                throwField1.setText(newValue.substring(0, 10));
            Main.gamesRepositotyImpl.getCurrentRound().calculatePoints();
        });
        // adding textField validation and save()
        throwField2.textProperty().addListener((observable,oldValue,newValue) ->{
            if(newValue.length() > 10)
                throwField2.setText(newValue.substring(0, 10));
            Main.gamesRepositotyImpl.getCurrentRound().calculatePoints();
        });
        // adding textField validation and save()
        throwField3.textProperty().addListener((observable,oldValue,newValue) ->{
            if(newValue.length() > 10)
                throwField3.setText(newValue.substring(0, 10));
            Main.gamesRepositotyImpl.getCurrentRound().calculatePoints();
        });


        /**********************************************/
        /**********    Drawing dart board    **********/
        /**********************************************/
        // todo drawing
        Color redColor = Color.rgb(229, 9, 9);
        Color greenColor = Color.rgb(25, 137, 9);

        // Radius < 204 BackGROUND
        pointList.stream()
                .filter(point -> getRadius(getCenterX(point.getX()),getCenterY(point.getY())) < 204)
                .forEach(point ->  pixelWriterDrawed.setColor(point.getX(),point.getY(),Color.WHITE) );
        ////////////////////////////////

        // NOT EVEN indexes
        pointList.stream()
                .filter(point -> getRadius(getCenterX(point.getX()),getCenterY(point.getY())) < 204)
                .filter(point -> {
                    for(int i=0;i<20;i+=2){
                        if(Utility.filters.getAngleList().get(i).isInRange(getAngle(getCenterX(point.getX()),getCenterY(point.getY()))))
                            return true;
                    }
                    return false;
                })
                .forEach(point -> pixelWriterDrawed.setColor(point.getX(),point.getY(),Color.BLACK));
        ////////////////////////////////

        // EVEN indexes
        pointList.stream()
                .filter(point -> getRadius(getCenterX(point.getX()),getCenterY(point.getY())) < 204)
                .filter(point -> {
                    for(int i=1;i<20;i+=2){
                        if(Utility.filters.getAngleList().get(i).isInRange(getAngle(getCenterX(point.getX()),getCenterY(point.getY()))))
                            return true;
                    }
                    return false;
                })
                .forEach(point -> pixelWriterDrawed.setColor(point.getX(),point.getY(),Color.WHITE));
        ////////////////////////////////


        // Radius SINGLE BULL //
        pointList.stream()
                .filter(point -> getRadius(getCenterX(point.getX()),getCenterY(point.getY())) < 19)
                .forEach(point ->  pixelWriterDrawed.setColor(point.getX(),point.getY(),greenColor) );
        ////////////////////////////////

        // Radius DOUBLE BULL //
        pointList.stream()
                .filter(point -> getRadius(getCenterX(point.getX()),getCenterY(point.getY())) < 9)
                .forEach(point ->  pixelWriterDrawed.setColor(point.getX(),point.getY(),redColor) );
        ////////////////////////////////

        // todo
        // Radius
        pointList.stream()
                .filter(point -> getRadius(getCenterX(point.getX()),getCenterY(point.getY())) == 8)
                .forEach(point ->  pixelWriterDrawed.setColor(point.getX(),point.getY(),Color.BLACK) );
        ////////////////////////////////
// todo
        // Radius D
        pointList.stream()
                .filter(point -> getRadius(getCenterX(point.getX()),getCenterY(point.getY())) == 18)
                .forEach(point ->  pixelWriterDrawed.setColor(point.getX(),point.getY(),Color.BLACK) );
        ////////////////////////////////


        // Radius < 205 && Radius > 195 NOT EVEN indexes
        pointList.stream()
                .filter(point -> getRadius(getCenterX(point.getX()),getCenterY(point.getY())) < 205)
                .filter(point -> getRadius(getCenterX(point.getX()),getCenterY(point.getY())) >= 194)
                .filter(point -> {
                    for(int i=0;i<20;i+=2){
                        if(Utility.filters.getAngleList().get(i).isInRange(getAngle(getCenterX(point.getX()),getCenterY(point.getY()))))
                            return true;
                    }
                    return false;
                })
                .forEach(point -> pixelWriterDrawed.setColor(point.getX(),point.getY(),redColor));
        ////////////////////////////////

        // Radius < 205 && Radius > 195 EVEN indexes
        pointList.stream()
                .filter(point -> getRadius(getCenterX(point.getX()),getCenterY(point.getY())) < 205)
                .filter(point -> getRadius(getCenterX(point.getX()),getCenterY(point.getY())) >= 194)
                .filter(point -> {
                    for(int i=1;i<20;i+=2){
                        if(Utility.filters.getAngleList().get(i).isInRange(getAngle(getCenterX(point.getX()),getCenterY(point.getY()))))
                            return true;
                    }
                    return false;
                })
                .forEach(point -> pixelWriterDrawed.setColor(point.getX(),point.getY(),greenColor));
        ////////////////////////////////
        ////////////////////////////////
        ////////////////////////////////

        // Radius < 127 && Radius > 118 NOT EVEN indexes
        pointList.stream()
                .filter(point -> getRadius(getCenterX(point.getX()),getCenterY(point.getY())) < 127)
                .filter(point -> getRadius(getCenterX(point.getX()),getCenterY(point.getY())) >= 117)
                .filter(point -> {
                    for(int i=0;i<20;i+=2){
                        if(Utility.filters.getAngleList().get(i).isInRange(getAngle(getCenterX(point.getX()),getCenterY(point.getY()))))
                            return true;
                    }
                    return false;
                })
                .forEach(point -> pixelWriterDrawed.setColor(point.getX(),point.getY(),redColor));
        ////////////////////////////////

        // Radius < 127 && Radius > 118 EVEN indexes
        pointList.stream()
                .filter(point -> getRadius(getCenterX(point.getX()),getCenterY(point.getY())) < 127)
                .filter(point -> getRadius(getCenterX(point.getX()),getCenterY(point.getY())) >= 117)
                .filter(point -> {
                    for(int i=1;i<20;i+=2){
                        if(Utility.filters.getAngleList().get(i).isInRange(getAngle(getCenterX(point.getX()),getCenterY(point.getY()))))
                            return true;
                    }
                    return false;
                })
                .forEach(point -> pixelWriterDrawed.setColor(point.getX(),point.getY(),greenColor));
        ////////////////////////////////

        // todo
        // Radius D
        pointList.stream()
                .filter(point -> getRadius(getCenterX(point.getX()),getCenterY(point.getY())) == 126)
                .forEach(point ->  pixelWriterDrawed.setColor(point.getX(),point.getY(),Color.BLACK) );
        ////////////////////////////////
        // todo
        // Radius D
        pointList.stream()
                .filter(point -> getRadius(getCenterX(point.getX()),getCenterY(point.getY())) == 116)
                .forEach(point ->  pixelWriterDrawed.setColor(point.getX(),point.getY(),Color.BLACK) );
        ////////////////////////////////
        // todo
        // Radius D
        pointList.stream()
                .filter(point -> getRadius(getCenterX(point.getX()),getCenterY(point.getY())) == 194)
                .forEach(point ->  pixelWriterDrawed.setColor(point.getX(),point.getY(),Color.BLACK) );
        ////////////////////////////////

//todo
        pointList.stream()
                .filter(point -> getRadius(getCenterX(point.getX()),getCenterY(point.getY())) > 18)
                .filter(point -> getRadius(getCenterX(point.getX()),getCenterY(point.getY())) < 205)
                .filter(point -> {
                    for(int i=0;i<20;i++){
                        if(Utility.filters.getAngleList().get(i).getFirstAngle()
                                == getAngle(getCenterX(point.getX()),getCenterY(point.getY()))
                        || Utility.filters.getAngleList().get(i).getSecondAngle()
                                == getAngle(getCenterX(point.getX()),getCenterY(point.getY())))

                            return true;
                    }
                    return false;
                })
                .forEach(point -> pixelWriterDrawed.setColor(point.getX(),point.getY(),Color.BLACK));
        ////////////////////////////////

//        boardPane.setStyle("-fx-background-color: radial-gradient(center 50% 50%, radius 55%, #198909, #333333);");
    }




    private GraphicsContext graphicsContext2DHighlight;
    private PixelWriter pixelWriterHighlight;

    private GraphicsContext graphicsContext2DDrawed;
    private PixelWriter pixelWriterDrawed;

    public void onClickThrowField1(){
        throwField1.selectAll();
        System.out.println("TextField 1 Marked!");
        Main.gamesRepositotyImpl.getCurrentRound().setCurrentThrow(1);
    }
    public void onClickThrowField2(){
        throwField2.selectAll();
        System.out.println("TextField 2 Marked!");
        Main.gamesRepositotyImpl.getCurrentRound().setCurrentThrow(2);
    }
    public void onClickThrowField3(){
        throwField3.selectAll();
        System.out.println("TextField 3 Marked!");
        Main.gamesRepositotyImpl.getCurrentRound().setCurrentThrow(3);
    }

}
