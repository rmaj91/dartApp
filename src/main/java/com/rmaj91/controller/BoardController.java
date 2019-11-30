package com.rmaj91.controller;

import com.rmaj91.Main;
import com.rmaj91.domain.Point;
import com.rmaj91.utility.Filters;
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
    // TODO przniesc do Main
    Filters filters = new Filters();

    public void boardHover2(MouseEvent event){
        graphicsContext2D.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        pointList.stream()
                .filter(point -> {
                    int indexRangeScope = filters.getRadiusScope(getRadius(getX(event),getY(event)));
                    return filters.getRadiusList().get(indexRangeScope).isInRange(getRadius(getCenterX(point.getX()), getCenterY(point.getY()))); })
                .filter(point -> {
                    if(getRadius(getX(event),getY(event)) < 19 || getRadius(getX(event),getY(event)) > 205)
                        return true;
                    else{
                        int indexAngleScope = filters.getAngleScope(getAngle(getX(event),getY(event)));
                        return filters.getAngleList().get(indexAngleScope).isInRange(getAngle(getCenterX(point.getX()), getCenterY(point.getY()))); } })
                .forEach(point -> {
                    displayPixel(point.getX(),point.getY()); });
    }

    public int getRadius(double x, double y){
        return (int)Math.sqrt(x*x+y*y);
    }
    public void displayPixel(int x,int y){
        pixelWriter.setColor(x, y, Color.rgb(66, 135, 245));
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
        graphicsContext2D.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        canvas.setOpacity(0.8);
        this.graphicsContext2D = canvas.getGraphicsContext2D();
        this.pixelWriter = graphicsContext2D.getPixelWriter();
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
            // todo ogarnac zapis i przeliczenie to zmianie wartosci bo w tym przypadku zapisywalo 2x, za 2gim razem dla nast gracza
            // liczy
            //Main.gamesRepositotyImpl.getCurrentRound().saveThrowFields();
            //test
            Main.gamesRepositotyImpl.getCurrentRound().calculatePoints();
        });
        // adding textField validation and save()
        throwField2.textProperty().addListener((observable,oldValue,newValue) ->{
            if(newValue.length() > 10)
                throwField2.setText(newValue.substring(0, 10));
            //Main.gamesRepositotyImpl.getCurrentRound().saveThrowFields();
            //test
            Main.gamesRepositotyImpl.getCurrentRound().calculatePoints();
        });
        // adding textField validation and save()
        throwField3.textProperty().addListener((observable,oldValue,newValue) ->{
            if(newValue.length() > 10)
                throwField3.setText(newValue.substring(0, 10));
            //Main.gamesRepositotyImpl.getCurrentRound().saveThrowFields();
            //test
            Main.gamesRepositotyImpl.getCurrentRound().calculatePoints();
        });
    }

    private GraphicsContext graphicsContext2D;
    private PixelWriter pixelWriter;

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
