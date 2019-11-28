//package com.rmaj91.domain;
////
////import com.rmaj91.Main;
////import com.rmaj91.controller.BoardController;
////import com.rmaj91.controller.Game01Controller;
////import com.rmaj91.utility.Utility;
////import javafx.scene.Node;
////import javafx.scene.input.MouseEvent;
////import javafx.scene.layout.VBox;
////
////
////import java.util.ArrayList;
////import java.util.Arrays;
////import java.util.List;
////
////
////
////public class Game01 implements Playable {
////
////    ///////////////////////////
////    // static
////    ///////////////////////////
////    private static Game01Controller game01Controller;
////    private static BoardController boardController;
////    private static int rounds;
////    private static int playersQuantity;
////    private static boolean doubleOut;
////    //////////////////////////////
////    // variables
////    /////////////////////////////
////    private int currentThrow=1;
////    private int currentRound=1;
////    private int currentPlayer=1;
////    private List<Player> playerList = new ArrayList<>();
////    ////////////////////////////////
////    // SETTERS/GETTERS
////    ///////////////////////////////
////    public List<Player> getPlayerList() {
////        return playerList;
////    }
////
////    public static int getPlayersQuantity() {
////        return playersQuantity;
////    }
////
////    public void addPlayer(Player player){
////        playerList.add(player);
////    }
////
////    public static void setBoardController(BoardController boardController) {
////        Game01.boardController = boardController;
////    }
////
////    public static void setGame01Controller(Game01Controller game01Controller) {
////        Game01.game01Controller = game01Controller;
////    }
////
////    public void setCurrentThrow(int currentThrow) {
////        this.currentThrow = currentThrow;
////    }
////
////
////
////    public int getCurrentThrow() {
////        return this.currentThrow;
////    }
////
////    public int getCurrentRound() {
////        return currentRound;
////    }
////
////    public void setCurrentRound(int currentRound) {
////        this.currentRound = currentRound;
////    }
////
////    public int getCurrentPlayer() {
////        return currentPlayer;
////    }
////
////    public void setCurrentPlayer(int currentPlayer) {
////        this.currentPlayer = currentPlayer;
////    }
////
////    public static void setRounds(int rounds) {
////        Game01.rounds = rounds;
////    }
////
////    public static void setPlayersQuantity(int playersQuantity) {
////        Game01.playersQuantity = playersQuantity;
////    }
////
////    public static void setDoubleOut(boolean doubleOut) {
////        Game01.doubleOut = doubleOut;
////    }
////    ///////////////////////////////////////
////    // PLAYER CLASS
////    ///////////////////////////////////////
////    public static class Player{
////
////        private String name;
////        private int points;
////        private String[] fields = new String[3];
////        private double average=0;
////        //private int currentThrow = 1;
////
////        public Player(String name,int points) {
////            this.name = name;
////            this.points = points;
////            Arrays.fill(fields,new String());
////        }
////
////        public Player clonePlayer(){
////            Player newPlayer = new Player(this.name,this.points);
////            newPlayer.setAverage(this.average);
////            for(int i=0;i<3;i++){
////                newPlayer.setField(i,this.getField(i));
////            }
////            return newPlayer;
////        }
////
////        public String getField(int index) {
////            return fields[index];
////        }
////
////        public void setField(int index,String field) {
////            this.fields[index] = new String(field);
////        }
////
////        public void setAverage(double average) {
////            this.average = average;
////        }
////    }
////
////    ///////////////////////////////////////////
////    // METHODS FROM PLAYABLE
////    ///////////////////////////////////////////
////
////    @Override
////    public void initGUI() {
////
////    }
////
////    @Override
////    public Game01 cloneGame(){
////        Game01 newGame = new Game01();
////        newGame.setCurrentPlayer(this.currentPlayer);
////        newGame.setCurrentThrow(this.currentThrow);
////        newGame.setCurrentRound(this.currentRound);
////
////        for (Player player : playerList) {
////            Player newPlayer = player.clonePlayer();
////            newGame.getPlayerList().add(newPlayer);
////        }
////
////        return newGame;
////    }
////
//////	@Override
//////	public void next() {
//////		System.out.println("Next Button clicked!");
//////		if(currentRound < Game01.rounds){
//////			currentThrow = 1;
//////			if(currentPlayer == playersQuantity){
//////				currentPlayer = 1;
//////				currentRound ++;
//////			}
//////			Main.gamesRepositoty.addRound(this.cloneGame());
//////			display();
//////			boardController.getThrowField1().requestFocus();
//////		}
////
////}
////
////    @Override
////    public void back() {
//////		System.out.println("Back Button clicked!");
//////		this.currentThrow = 1;
////    }
////
////    @Override
////    public void throwDart(MouseEvent event) {
////        if(currentThrow == 4)
////            return;
////        double x = event.getX()-248;
////        double y = -(event.getY()-248.5);
////        System.out.print("Pressed: x="+x + "  y=" +y+"\t");
////        // tODO do main
////        Filters filters = new Filters();
////        int radiusIndex = Utility.getRadiusIndex(x,y);
////        int angleIndex = Utility.getAngleIndex(x,y);
////        String key="";
////        for (Filters.IndexMapper indexMapper : filters.getIndexMapperList()) {
////            if(indexMapper.hasKey(radiusIndex,angleIndex)){
////                key=indexMapper.getKey();
////                System.out.println(key);
////                break;
////            }
////        }
////
////
////        if(currentThrow == 1){
////            boardController.getThrowField1().setText(key);
////            boardController.getThrowField2().requestFocus();
////        }
////
////        else if(currentThrow == 2){
////            boardController.getThrowField2().setText(key);
////            boardController.getThrowField3().requestFocus();
////        }
////
////        else if(currentThrow == 3)
////            boardController.getThrowField3().setText(key);
////        currentThrow++;
////
////        System.out.println(Utility.readValues(key));
////
////    }
////
////
////    // TODO dodac init do players
////
////    @Override
////    public void display() {
////        // Rounds //
////        boardController.getRoundsLabel().setText("Round: "+currentRound+"/"+Game01.rounds);
////
////        // TextFields //
////        boardController.getThrowField1().setText(playerList.get(currentPlayer-1).fields[0]);
////        boardController.getThrowField2().setText(playerList.get(currentPlayer-1).fields[1]);
////        boardController.getThrowField3().setText(playerList.get(currentPlayer-1).fields[2]);
////
////        // Big Player name,points,average //
////        boardController.getPlayerNameLabel().setText(playerList.get(currentPlayer-1).name);
////        boardController.getPlayerPointsLabel().setText(String.valueOf(playerList.get(currentPlayer-1).points));
////        boardController.getAverageLabel().setText("Average: "+ playerList.get(currentPlayer-1).average);
////
////        //highlight player
////        Node node = boardController.getGame01PlayersTable().getChildren().get(currentPlayer - 1);
////        ((VBox)node).getChildren().get(0).setStyle("-fx-background-color: #0388fc;");
////        //((VBox)node).getChildren().get(0).setStyle("-fx-background-radius: 4;");
////    }
////
////    ///////////////////////////////////////
////    // PRIVATE METHODS
////    ///////////////////////////////////////
////
////}