package com.rmaj91.controller;

import com.rmaj91.domain.Point;
import com.rmaj91.repository.GamesRepositoryImpl;
import com.rmaj91.utility.SoundPlayer;
import com.rmaj91.utility.Utilities;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.rmaj91.utility.Utilities.filters;
import static com.rmaj91.utility.Utilities.getRadius;
import static com.rmaj91.utility.Utilities.getAngle;

public class BoardController implements Initializable {

    //==================================================================================================
    // Dependencies
    //==================================================================================================
    private SoundPlayer soundPlayer;
    private GamesRepositoryImpl gamesRepository;


    //==================================================================================================
    // Propeties
    //==================================================================================================
    private GraphicsContext graphicsContext2DHighlight;
    private PixelWriter pixelWriterHighlight;
    private GraphicsContext graphicsContext2DDrawed;
    private PixelWriter pixelWriterDrawed;
    private int currentRadiusIndex = 7;
    private int currentAngleIndex = 20;


    //region JavaFX elements @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //==================================================================================================
    // JavaFX elements
    //==================================================================================================
    @FXML
    private StackPane mainStackPane;
    @FXML
    private Label roundsLabel;
    @FXML
    private TextField throwField1;
    @FXML
    private TextField throwField2;
    @FXML
    private TextField throwField3;
    @FXML
    private HBox cricketsScoreTable;
    @FXML
    private VBox cricketsFields;
    @FXML
    private StackPane game01ScoreTable;
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
    private Canvas canvas;
    @FXML
    private Canvas drawedBoard;
    @FXML
    private ImageView dartBoard;
    @FXML
    private HBox gamePane;
    @FXML
    private StackPane boardPane;
    @FXML
    private BorderPane pointsAreaPane;

    //endregion


    //==================================================================================================
    // Arrays od elements
    //==================================================================================================
    TextField[] throwTextFieldArray;
    List<Point> pointList = new ArrayList<>();


    //region Getters/Setters @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    //==================================================================================================
    // Getters/Setters
    //==================================================================================================
    public HBox getCricketsScoreTable() {
        return cricketsScoreTable;
    }

    public VBox getCricketsFields() {
        return cricketsFields;
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

    public HBox getGamePane() {
        return gamePane;
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

    public StackPane getGame01ScoreTable() {
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

    public ImageView getDartBoard() {
        return dartBoard;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public StackPane getBoardPane() {
        return boardPane;
    }

    public Canvas getDrawedBoard() {
        return drawedBoard;
    }

    public BorderPane getPointsAreaPane() {
        return pointsAreaPane;
    }

    public void setSoundPlayer(SoundPlayer soundPlayer) {
        this.soundPlayer = soundPlayer;
    }

    public void setGamesRepository(GamesRepositoryImpl gamesRepository) {
        this.gamesRepository = gamesRepository;
    }
    //endregion


    //==================================================================================================
    // Custom Getters/Setters
    //==================================================================================================
    public TextField getThrowTextField(int index) {
        return throwTextFieldArray[index];
    }


    //==================================================================================================
    // Events MEthods
    //==================================================================================================
    public void nextButtonClicked() {
        gamesRepository.getCurrentRound().NextButton();
    }

    public void backButtonClicked() {
        gamesRepository.getCurrentRound().BackButton();
    }

    public void dartBoardClicked(MouseEvent event) {
        gamesRepository.getCurrentRound().throwDart(event);
    }

    public void toFront() {
        mainStackPane.toFront();
    }

    public void onClickThrowField1(MouseEvent event) {
        throwField1.selectAll();
        gamesRepository.getCurrentRound().setCurrentThrowTo(1);
    }

    public void onClickThrowField2() {
        throwField2.selectAll();
        gamesRepository.getCurrentRound().setCurrentThrowTo(2);
    }

    public void onClickThrowField3() {
        throwField3.selectAll();
        gamesRepository.getCurrentRound().setCurrentThrowTo(3);
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
     *
     * @param event Mouse event
     */
    public void boardHover(MouseEvent event) {
        int hoverMouseRadiusIndex = Utilities.getRadiusScope(getRadius(getX(event), getY(event)));
        int hoverMouseAngleIndex = Utilities.getAngleScope(getAngle(getX(event), getY(event)));
        if (ifMouseDidntLeftCurrentField(hoverMouseRadiusIndex, hoverMouseAngleIndex))
            return;
        saveNewFieldIndexes(hoverMouseRadiusIndex, hoverMouseAngleIndex);
        graphicsContext2DHighlight.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        filterPixelsAndDrawHighlight(event, hoverMouseRadiusIndex, hoverMouseAngleIndex);
    }


    //==================================================================================================
    // Initializing
    //==================================================================================================
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initThrowTextFieldArray();
        initGraphicTools();
        throwTextFieldValidation();
        drawDartBoard();
    }


    //==================================================================================================
    // Private Methods
    //==================================================================================================
    private void saveNewFieldIndexes(int hoverMouseRadiusIndex, int hoverMouseAngleIndex) {
        currentRadiusIndex = hoverMouseRadiusIndex;
        currentAngleIndex = hoverMouseAngleIndex;
    }

    private boolean ifMouseDidntLeftCurrentField(int hoverMouseRadiusIndex, int hoverMouseAngleIndex) {
        return hoverMouseRadiusIndex == currentRadiusIndex && hoverMouseAngleIndex == currentAngleIndex;
    }

    private void initGraphicTools() {
        this.graphicsContext2DHighlight = canvas.getGraphicsContext2D();
        this.pixelWriterHighlight = graphicsContext2DHighlight.getPixelWriter();
        this.graphicsContext2DDrawed = drawedBoard.getGraphicsContext2D();
        this.pixelWriterDrawed = graphicsContext2DDrawed.getPixelWriter();
    }

    private void initThrowTextFieldArray() {
        throwTextFieldArray = new TextField[3];
        throwTextFieldArray[0] = throwField1;
        throwTextFieldArray[1] = throwField2;
        throwTextFieldArray[2] = throwField3;
    }

    private void filterPixelsAndDrawHighlight(MouseEvent event, int hoverRadiusIndex, int hoverAngleIndex) {
        pointList.stream()
                .filter(point -> {
                    int xCartesian = calculateCartesianX(point.getxCoordinate());
                    int yCartesian = calculateCartesianY(point.getyCoordinate());
                    int radius = getRadius(xCartesian, yCartesian);
                    return filters.getRadiusMapperList().get(hoverRadiusIndex).isInRange(radius);
                })
                .filter(point -> {
                    if (getRadius(getX(event), getY(event)) < 19 || getRadius(getX(event), getY(event)) > 205)
                        return true;
                    else {
                        int xCartesian = calculateCartesianX(point.getxCoordinate());
                        int yCartesian = calculateCartesianY(point.getyCoordinate());
                        double angle = getAngle(xCartesian, yCartesian);
                        return filters.getAngleMapperList().get(hoverAngleIndex).isInRange(angle);
                    }
                })
                .forEach(point -> {
                    displayPixel(point.getxCoordinate(), point.getyCoordinate());
                });
    }


    /**
     * displaying highlight pixel on x,y coordinates
     */
    private void displayPixel(int x, int y) {
        pixelWriterHighlight.setColor(x, y, Color.rgb(66, 135, 245));
    }

    private int calculateCartesianX(int x) {
        return x - 248;
    }

    private int calculateCartesianY(int y) {
        return -(y - 248);
    }

    private double getX(MouseEvent event) {
        return event.getX() - 248;
    }

    private double getY(MouseEvent event) {
        return -(event.getY() - 248.5);
    }

    private void throwTextFieldValidation() {
        for (int i = 0; i < 3; i++) {
            throwTextFieldArray[i].textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.length() > 10)
                    throwField1.setText(newValue.substring(0, 10));
            });
        }
    }

    private void drawDartBoard() {
        canvas.setOpacity(0.75);
        initPixelsCollection();
        Color redColor = Color.rgb(229, 9, 9);
        Color greenColor = Color.rgb(25, 137, 9);
        drawDartBoardFields(redColor, greenColor);
    }

    private void drawDartBoardFields(Color redColor, Color greenColor) {
        drawDartBoardCircles(Color.WHITE, 204);
        drawDartBoardAngleWhiteFragments();
        drawDartBoardCircles(greenColor, 19);
        drawDartBoardCircles(redColor, 9);

        drawDartBoardAngleFragments(redColor, 205, 193, 0);
        drawDartBoardAngleFragments(greenColor, 205, 193, 1);
        drawDartBoardAngleFragments(redColor, 127, 116, 0);
        drawDartBoardAngleFragments(greenColor, 127, 116, 1);

        drawDartBoardBlackBorders();
        fixCustomBoardEdges();
        fixAngleFragmentsBorders();
    }

    private void initPixelsCollection() {
        for (int y = 0; y < canvas.getHeight(); y++) {
            for (int x = 0; x < canvas.getWidth(); x++)
                pointList.add(new Point(x, y));
        }
    }

    private void fixAngleFragmentsBorders() {
        pointList.stream()
                .filter(point -> getRadius(calculateCartesianX(point.getxCoordinate()), calculateCartesianY(point.getyCoordinate())) > 18)
                .filter(point -> getRadius(calculateCartesianX(point.getxCoordinate()), calculateCartesianY(point.getyCoordinate())) < 205)
                .filter(point -> {
                    for (int i = 0; i < 20; i++) {
                        if (filters.getAngleMapperList().get(i).getFirstAngle()
                                == getAngle(calculateCartesianX(point.getxCoordinate()), calculateCartesianY(point.getyCoordinate()))
                                || filters.getAngleMapperList().get(i).getSecondAngle()
                                == getAngle(calculateCartesianX(point.getxCoordinate()), calculateCartesianY(point.getyCoordinate())))
                            return true;
                    }
                    return false;
                })
                .forEach(point -> pixelWriterDrawed.setColor(point.getxCoordinate(), point.getyCoordinate(), Color.BLACK));
    }

    private void fixCustomBoardEdges() {
        pointList.stream()
                .filter(point -> getRadius(calculateCartesianX(point.getxCoordinate()), calculateCartesianY(point.getyCoordinate())) > 204)
                .filter(point -> getRadius(calculateCartesianX(point.getxCoordinate()), calculateCartesianY(point.getyCoordinate())) < 206)
                .forEach(point -> pixelWriterDrawed.setColor(point.getxCoordinate(), point.getyCoordinate(), Color.BLACK));
    }

    private void drawDartBoardBlackBorders() {
        int[] radiuses = {8, 19, 116, 126, 194};
        for (int radius : radiuses) {
            pointList.stream()
                    .filter(point -> getRadius(calculateCartesianX(point.getxCoordinate()), calculateCartesianY(point.getyCoordinate())) == radius)
                    .forEach(point -> pixelWriterDrawed.setColor(point.getxCoordinate(), point.getyCoordinate(), Color.BLACK));
        }
    }

    private void drawDartBoardAngleWhiteFragments() {
        pointList.stream()
                .filter(point -> getRadius(calculateCartesianX(point.getxCoordinate()), calculateCartesianY(point.getyCoordinate())) < 204)
                .filter(point -> {
                    for (int i = 0; i < 20; i += 2) {
                        if (filters.getAngleMapperList().get(i).isInRange(getAngle(calculateCartesianX(point.getxCoordinate()), calculateCartesianY(point.getyCoordinate()))))
                            return true;
                    }
                    return false;
                })
                .forEach(point -> pixelWriterDrawed.setColor(point.getxCoordinate(), point.getyCoordinate(), Color.BLACK));
    }

    private void drawDartBoardAngleFragments(Color redColor, int i2, int i3, int i4) {
        pointList.stream()
                .filter(point -> getRadius(calculateCartesianX(point.getxCoordinate()), calculateCartesianY(point.getyCoordinate())) < i2)
                .filter(point -> getRadius(calculateCartesianX(point.getxCoordinate()), calculateCartesianY(point.getyCoordinate())) > i3)
                .filter(point -> {
                    for (int i = i4; i < 20; i += 2) {
                        if (filters.getAngleMapperList().get(i).isInRange(getAngle(calculateCartesianX(point.getxCoordinate()), calculateCartesianY(point.getyCoordinate()))))
                            return true;
                    }
                    return false;
                })
                .forEach(point -> pixelWriterDrawed.setColor(point.getxCoordinate(), point.getyCoordinate(), redColor));
    }

    private void drawDartBoardCircles(Color greenColor, int i2) {
        // SINGLE BULL
        pointList.stream()
                .filter(point -> getRadius(calculateCartesianX(point.getxCoordinate()), calculateCartesianY(point.getyCoordinate())) < i2)
                .forEach(point -> pixelWriterDrawed.setColor(point.getxCoordinate(), point.getyCoordinate(), greenColor));
    }

}
