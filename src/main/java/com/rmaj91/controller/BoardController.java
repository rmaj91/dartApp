package com.rmaj91.controller;

import com.rmaj91.domain.Throw;
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

        // wywolanie metody game, rzut
        System.out.println("Pressed: x="+(event.getX() - 248) + "  y=" + (event.getY() - 248.5));
    }

    public void boardHover(MouseEvent event) {

        graphicsContext2D.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        double x = event.getX() - 248;
        double y = -(event.getY() - 248.5);


        printCircles(x,y,0,8);
        printCircles(x,y,8,19);
        printCircles(x,y,19,117);
        printCircles(x,y,117,127);
        printCircles(x,y,127,193);
        printCircles(x,y,193,205);
        printCircles(x,y,205,249);


        int z = (int) Math.sqrt(x * x + y * y);
        System.out.print(z + "\t");
        System.out.print(x + " " + y+"\t");
        System.out.println("Angle alpha = "+getAngle(x,y));

    }

    public void boardHoverOff() {
        graphicsContext2D.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void printCircles(double x,double y,int smallRadius,int BigRadius) {
        int z = (int) Math.sqrt(x * x + y * y);

        if (z < BigRadius && z >= smallRadius) {

            for (int i = 0; i < dartBoard.getFitWidth(); i++) {
                for (int j = 0; j < dartBoard.getFitHeight(); j++) {

                    int jj = j - 248;
                    double ii = -(i - 248.5);
                    int zz = (int) Math.sqrt(ii * ii + jj * jj);

                    if (zz < BigRadius && zz >= smallRadius)
                            if( zz < 19 || zz > 205)
                                pixelWriter.setColor(j, i, Color.rgb(66, 135, 245));
                            else{
                                for(int k=0;k<19;k++){
                                    checkAngleAndDrawPiece(x,y,jj,ii,j,i,7.2+18*k,26.2+18*k);
                                    //brakuje jednego przypadku gdy kąt się zeruje
                                }
                                if(getAngle(x,y) >= 0 && getAngle(x,y) < 7.2
                                || getAngle(x,y) > 349.2){
                                    if(getAngle(jj,ii) >= 0 && getAngle(jj,ii) < 7.2
                                    || getAngle(jj,ii) > 349.2)
                                        pixelWriter.setColor(j, i, Color.rgb(66, 135, 245));
                                }
                            }
                }
            }

        }
    }



    public void checkAngleAndDrawPiece(double x,double y,double jj, double ii,int j,int i, double firstAngle,double secondAngle){

        if(getAngle(x,y) > firstAngle && getAngle(x,y) < secondAngle){
            if(getAngle(jj,ii) > firstAngle && getAngle(jj,ii) < secondAngle)
                pixelWriter.setColor(j, i, Color.rgb(66, 135, 245));
        }
    }

    public double getAngle(double x, double y){
        double angle=Math.atan2(y,x);
        angle = Math.toDegrees(angle);
        if(angle<0)
            angle+=360;
        return angle;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        canvas.setOpacity(0.8);
        this.graphicsContext2D = canvas.getGraphicsContext2D();
        this.pixelWriter = graphicsContext2D.getPixelWriter();
    }

    private GraphicsContext graphicsContext2D;
    private PixelWriter pixelWriter;
}
