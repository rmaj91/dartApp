package com.rmaj91.controller;

import com.rmaj91.Main;
import com.rmaj91.domain.Game01;
import com.rmaj91.domain.Point;
import com.rmaj91.utility.SoundPlayer;
import com.rmaj91.utility.Utility;
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

public class BoardController implements Initializable {

    // Dependencies //
    private SoundPlayer soundPlayer;

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


    public void setSoundPlayer(SoundPlayer soundPlayer) {
        this.soundPlayer = soundPlayer;
    }

    public VBox getStatusPlayer1() {
        return statusPlayer1;
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

    // OnClick Board
    public void boardClicked(MouseEvent event) {
        Main.gamesRepositotyImpl.getCurrentRound().throwDart(event);
    }


    List<Point> pointList = new ArrayList<>();

    private int currentRadiusIndex = -1;
    private int currentAngleIndex = -1;

    public void boardHover2(MouseEvent event){

        int hoverRadiusIndex = Utility.getRadiusScope(getRadius(getX(event),getY(event)));
        int hoverAngleIndex = Utility.getAngleScope(getAngle(getX(event),getY(event)));
        if(hoverRadiusIndex == currentRadiusIndex && hoverAngleIndex == currentAngleIndex)
            return;
        currentRadiusIndex = hoverRadiusIndex;
        currentAngleIndex = hoverAngleIndex;

        graphicsContext2DHighlight.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        pointList.stream()
                .filter(point -> {
                    //int indexRangeScope = Utility.filters.getRadiusScope(getRadius(getX(event),getY(event)));
                    return Utility.filters.getRadiusMapperList().get(hoverRadiusIndex).isInRange(getRadius(getCenterX(point.getxCoordinate())
                            , getCenterY(point.getyCoordinate()))); })
                .filter(point -> {
                    if(getRadius(getX(event),getY(event)) < 19 || getRadius(getX(event),getY(event)) > 205)
                        return true;
                    else{
                        //int indexAngleScope = Utility.filters.getAngleScope(getAngle(getX(event),getY(event)));
                        return Utility.filters.getAngleMapperList().get(hoverAngleIndex).isInRange(getAngle(getCenterX(point.getxCoordinate())
                                , getCenterY(point.getyCoordinate()))); } })
                .forEach(point -> {
                    displayPixel(point.getxCoordinate(),point.getyCoordinate()); });
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
                .filter(point -> getRadius(getCenterX(point.getxCoordinate()),getCenterY(point.getyCoordinate())) < 204)
                .forEach(point ->  pixelWriterDrawed.setColor(point.getxCoordinate(),point.getyCoordinate(),Color.WHITE) );
        ////////////////////////////////

        // NOT EVEN indexes
        pointList.stream()
                .filter(point -> getRadius(getCenterX(point.getxCoordinate()),getCenterY(point.getyCoordinate())) < 204)
                .filter(point -> {
                    for(int i=0;i<20;i+=2){
                        if(Utility.filters.getAngleMapperList().get(i).isInRange(getAngle(getCenterX(point.getxCoordinate()),getCenterY(point.getyCoordinate()))))
                            return true;
                    }
                    return false;
                })
                .forEach(point -> pixelWriterDrawed.setColor(point.getxCoordinate(),point.getyCoordinate(),Color.BLACK));
        ////////////////////////////////

        // EVEN indexes
        pointList.stream()
                .filter(point -> getRadius(getCenterX(point.getxCoordinate()),getCenterY(point.getyCoordinate())) < 204)
                .filter(point -> {
                    for(int i=1;i<20;i+=2){
                        if(Utility.filters.getAngleMapperList().get(i).isInRange(getAngle(getCenterX(point.getxCoordinate()),getCenterY(point.getyCoordinate()))))
                            return true;
                    }
                    return false;
                })
                .forEach(point -> pixelWriterDrawed.setColor(point.getxCoordinate(),point.getyCoordinate(),Color.WHITE));
        ////////////////////////////////


        // Radius SINGLE BULL //
        pointList.stream()
                .filter(point -> getRadius(getCenterX(point.getxCoordinate()),getCenterY(point.getyCoordinate())) < 19)
                .forEach(point ->  pixelWriterDrawed.setColor(point.getxCoordinate(),point.getyCoordinate(),greenColor) );
        ////////////////////////////////

        // Radius DOUBLE BULL //
        pointList.stream()
                .filter(point -> getRadius(getCenterX(point.getxCoordinate()),getCenterY(point.getyCoordinate())) < 9)
                .forEach(point ->  pixelWriterDrawed.setColor(point.getxCoordinate(),point.getyCoordinate(),redColor) );
        ////////////////////////////////

        // todo
        // Radius
        pointList.stream()
                .filter(point -> getRadius(getCenterX(point.getxCoordinate()),getCenterY(point.getyCoordinate())) == 8)
                .forEach(point ->  pixelWriterDrawed.setColor(point.getxCoordinate(),point.getyCoordinate(),Color.BLACK) );
        ////////////////////////////////
// todo
        // Radius D
        pointList.stream()
                .filter(point -> getRadius(getCenterX(point.getxCoordinate()),getCenterY(point.getyCoordinate())) == 19)
                .forEach(point ->  pixelWriterDrawed.setColor(point.getxCoordinate(),point.getyCoordinate(),Color.BLACK) );
        ////////////////////////////////


        // Radius < 205 && Radius > 195 NOT EVEN indexes
        pointList.stream()
                .filter(point -> getRadius(getCenterX(point.getxCoordinate()),getCenterY(point.getyCoordinate())) < 205)
                .filter(point -> getRadius(getCenterX(point.getxCoordinate()),getCenterY(point.getyCoordinate())) >= 194)
                .filter(point -> {
                    for(int i=0;i<20;i+=2){
                        if(Utility.filters.getAngleMapperList().get(i).isInRange(getAngle(getCenterX(point.getxCoordinate()),getCenterY(point.getyCoordinate()))))
                            return true;
                    }
                    return false;
                })
                .forEach(point -> pixelWriterDrawed.setColor(point.getxCoordinate(),point.getyCoordinate(),redColor));
        ////////////////////////////////

        // Radius < 205 && Radius > 195 EVEN indexes
        pointList.stream()
                .filter(point -> getRadius(getCenterX(point.getxCoordinate()),getCenterY(point.getyCoordinate())) < 205)
                .filter(point -> getRadius(getCenterX(point.getxCoordinate()),getCenterY(point.getyCoordinate())) >= 194)
                .filter(point -> {
                    for(int i=1;i<20;i+=2){
                        if(Utility.filters.getAngleMapperList().get(i).isInRange(getAngle(getCenterX(point.getxCoordinate()),getCenterY(point.getyCoordinate()))))
                            return true;
                    }
                    return false;
                })
                .forEach(point -> pixelWriterDrawed.setColor(point.getxCoordinate(),point.getyCoordinate(),greenColor));
        ////////////////////////////////
        ////////////////////////////////
        ////////////////////////////////

        // Radius < 127 && Radius > 118 NOT EVEN indexes
        pointList.stream()
                .filter(point -> getRadius(getCenterX(point.getxCoordinate()),getCenterY(point.getyCoordinate())) < 127)
                .filter(point -> getRadius(getCenterX(point.getxCoordinate()),getCenterY(point.getyCoordinate())) >= 117)
                .filter(point -> {
                    for(int i=0;i<20;i+=2){
                        if(Utility.filters.getAngleMapperList().get(i).isInRange(getAngle(getCenterX(point.getxCoordinate()),getCenterY(point.getyCoordinate()))))
                            return true;
                    }
                    return false;
                })
                .forEach(point -> pixelWriterDrawed.setColor(point.getxCoordinate(),point.getyCoordinate(),redColor));
        ////////////////////////////////

        // Radius < 127 && Radius > 118 EVEN indexes
        pointList.stream()
                .filter(point -> getRadius(getCenterX(point.getxCoordinate()),getCenterY(point.getyCoordinate())) < 127)
                .filter(point -> getRadius(getCenterX(point.getxCoordinate()),getCenterY(point.getyCoordinate())) >= 117)
                .filter(point -> {
                    for(int i=1;i<20;i+=2){
                        if(Utility.filters.getAngleMapperList().get(i).isInRange(getAngle(getCenterX(point.getxCoordinate()),getCenterY(point.getyCoordinate()))))
                            return true;
                    }
                    return false;
                })
                .forEach(point -> pixelWriterDrawed.setColor(point.getxCoordinate(),point.getyCoordinate(),greenColor));
        ////////////////////////////////

        // todo
        // Radius D
        pointList.stream()
                .filter(point -> getRadius(getCenterX(point.getxCoordinate()),getCenterY(point.getyCoordinate())) == 126)
                .forEach(point ->  pixelWriterDrawed.setColor(point.getxCoordinate(),point.getyCoordinate(),Color.BLACK) );
        ////////////////////////////////
        // todo
        // Radius D
        pointList.stream()
                .filter(point -> getRadius(getCenterX(point.getxCoordinate()),getCenterY(point.getyCoordinate())) == 116)
                .forEach(point ->  pixelWriterDrawed.setColor(point.getxCoordinate(),point.getyCoordinate(),Color.BLACK) );
        ////////////////////////////////
        // todo
        // Radius D
        pointList.stream()
                .filter(point -> getRadius(getCenterX(point.getxCoordinate()),getCenterY(point.getyCoordinate())) == 194)
                .forEach(point ->  pixelWriterDrawed.setColor(point.getxCoordinate(),point.getyCoordinate(),Color.BLACK) );
        ////////////////////////////////

        // todo
        // fixing custom board edges :)
        pointList.stream()
                .filter(point -> getRadius(getCenterX(point.getxCoordinate()),getCenterY(point.getyCoordinate())) > 204)
                .filter(point -> getRadius(getCenterX(point.getxCoordinate()),getCenterY(point.getyCoordinate())) < 206)
                .forEach(point ->  pixelWriterDrawed.setColor(point.getxCoordinate(),point.getyCoordinate(),Color.BLACK) );
        ////////////////////////////////

//todo
        pointList.stream()
                .filter(point -> getRadius(getCenterX(point.getxCoordinate()),getCenterY(point.getyCoordinate())) > 18)
                .filter(point -> getRadius(getCenterX(point.getxCoordinate()),getCenterY(point.getyCoordinate())) < 205)
                .filter(point -> {
                    for(int i=0;i<20;i++){
                        if(Utility.filters.getAngleMapperList().get(i).getFirstAngle()
                                == getAngle(getCenterX(point.getxCoordinate()),getCenterY(point.getyCoordinate()))
                        || Utility.filters.getAngleMapperList().get(i).getSecondAngle()
                                == getAngle(getCenterX(point.getxCoordinate()),getCenterY(point.getyCoordinate())))

                            return true;
                    }
                    return false;
                })
                .forEach(point -> pixelWriterDrawed.setColor(point.getxCoordinate(),point.getyCoordinate(),Color.BLACK));
        ////////////////////////////////
    }




    private GraphicsContext graphicsContext2DHighlight;
    private PixelWriter pixelWriterHighlight;

    private GraphicsContext graphicsContext2DDrawed;
    private PixelWriter pixelWriterDrawed;

    public void onClickThrowField1(){
        throwField1.selectAll();
        Main.gamesRepositotyImpl.getCurrentRound().setCurrentThrow(1);
    }
    public void onClickThrowField2(){
        throwField2.selectAll();
        Main.gamesRepositotyImpl.getCurrentRound().setCurrentThrow(2);
    }
    public void onClickThrowField3(){
        throwField3.selectAll();
        Main.gamesRepositotyImpl.getCurrentRound().setCurrentThrow(3);
    }

    //todo dokonczyc init VBoxes
    public void initAndDisplay(){
        int currentPlayer = Main.gamesRepositotyImpl.getCurrentRound().getCurrentPlayer();
        int length = Main.gamesRepositotyImpl.getCurrentRound().getPlayer().length;


        for (int i = 0; i < length; i++) {

            VBox vBox = new VBox();
            vBox.setAlignment(Pos.BOTTOM_CENTER);
            vBox.setMinWidth(100);

            vBox.getChildren().addAll(createPlayerLabel( Main.gamesRepositotyImpl.getCurrentRound().getPlayer()[i].getName(),false),
                    createPlayerLabel(String.valueOf(Game01.getStartingPoints()),true));
            game01PlayersTable.getChildren().add(vBox);

        }
        Main.gamesRepositotyImpl.getCurrentRound().displayRound();

        toFront();
        game01ScoreTable.toFront();
        if(Game01.isDoubleOut())
            doubleOut.setVisible(true);
        else
            doubleOut.setVisible(false);
    }

    private Label createPlayerLabel(String text,boolean ifBold){
        Label playerLabel = new Label();
        playerLabel.setText(text);
        playerLabel.setFont(new Font("System",22));
        playerLabel.setTextFill(Color.WHITE);
        if(ifBold)
            playerLabel.setStyle("-fx-font-weight: bold");
        return playerLabel;
    }

}
