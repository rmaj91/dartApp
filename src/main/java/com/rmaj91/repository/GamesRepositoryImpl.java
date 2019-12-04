package com.rmaj91.repository;

import com.rmaj91.Main;
import com.rmaj91.controller.BoardController;
import com.rmaj91.domain.Cricket;
import com.rmaj91.domain.Game01;
import com.rmaj91.interfaces.GamesRepository;
import com.rmaj91.interfaces.Playable;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GamesRepositoryImpl implements GamesRepository {

    /*Dependencies*/
    private BoardController boardController;

    /*Variables*/
    private List<Playable> gamesList;

    /*Constructor*/
    public GamesRepositoryImpl() {
        gamesList = new LinkedList<>();
    }

    /*Setters & Getters*/
    public void setBoardController(BoardController boardController) {
        this.boardController = boardController;
    }

    //////////////////
    /*Public Methods*/
    //////////////////
	@Override
	public void pushRound(Playable round) {
		gamesList.add(round);
	}

	@Override
	public Playable getCurrentRound() {
		return gamesList.get(gamesList.size()-1);
	}

	@Override
	public boolean pullRound() {
		if(gamesList.size()>2){
			Playable round = gamesList.get(gamesList.size()-1);
			gamesList.remove(round);
			return true;
		}
		else
			return false;
	}

	@Override
	public void createNewGame(Playable round) {
		gamesList = new LinkedList<>();
		gamesList.add(round);
	}

	@Override
	public int getNumberOfRound(Playable round) {
		return gamesList.indexOf(round);
	}

	@Override
	public Playable getPreviousRound() {
		if(gamesList.size() == 1)
			return null;
		else
			return gamesList.get(gamesList.size()-2);
	}

    @Override
    public Playable getZeroRound() {
        return gamesList.get(0);
    }

    @Override
	public boolean saveGame() {

		if(gamesList.isEmpty())
			return false;
        //Show save file dialog
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Drt files (*.drt)", "*.drt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(Main.stage);
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(gamesList);

            //writing class name
            String className = this.getCurrentRound().getClass().getName();
            dataOutputStream.writeUTF(className);
            if(className.equals("com.rmaj91.domain.Game01")) {
                dataOutputStream.writeBoolean(Game01.isDoubleOut());
                dataOutputStream.writeInt(Game01.getNumberOfPlayers());
                dataOutputStream.writeInt(Game01.getMaxNumberOfRounds());
            }
            //            else if(className.equals("com.rmaj91.domain.Cricket") || className.equals("com.rmaj91.domain.MasterCricket")){
//                dataOutputStream.writeInt(Cricket.getPlayersQuantity());
//                dataOutputStream.writeInt(Cricket.getRoundsMaxNumber());
//                dataOutputStream.writeInt(Cricket.getCurrentFieldToThrowIndex());
//                objectOutputStream.writeObject(Cricket.getFieldsToHit());
            //          }

            dataOutputStream.close();

        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }

        showInformationWindow("Game Saved!","Game saved successfully :)");
		return true;
	}

    @Override
	public boolean loadGame() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Drt files (*.drt)", "*.drt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(Main.stage);

        DataInputStream dataInputStream;
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (Exception exception){
            return false;
        }

        try{
            dataInputStream = new DataInputStream(inputStream);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            gamesList = (List<Playable>) objectInputStream.readObject();

            String className = dataInputStream.readUTF();
            if(className.equals("com.rmaj91.domain.Game01")) {
                Game01.setDoubleOut(dataInputStream.readBoolean());
                Game01.setNumberOfPlayers(dataInputStream.readInt());
                Game01.setMaxNumberOfRounds(dataInputStream.readInt());
            }
            //            else if(className.equals("com.rmaj91.domain.Cricket") || className.equals("com.rmaj91.domain.MasterCricket")){
//                Cricket.setPlayersQuantity(dataInputStream.readInt());
//                Cricket.setRoundsMaxNumber(dataInputStream.readInt());
//                Cricket.setCurrentFieldToThrowIndex(dataInputStream.readInt());
//                Cricket.setFieldsToHit((ArrayList<Integer>) objectInputStream.readObject());
            //          }
            dataInputStream.close();

        } catch (Exception exception) {
            exception.printStackTrace();
            showInformationWindow("Error while loading","Game cannot be loaded!");
            return false;
        }
        this.getCurrentRound().setBoardViewVisible();

        return true;

	}

    @Override
    public void clear() {
        gamesList.clear();
    }

    @Override
    public boolean isEmpty() {
        return gamesList.isEmpty();
    }


    /*Private Methods*/
    private void showInformationWindow(String title,String header) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.showAndWait();
    }


}
