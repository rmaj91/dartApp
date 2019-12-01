package com.rmaj91.repository;

import com.rmaj91.Main;
import com.rmaj91.controller.BoardController;
import com.rmaj91.domain.Game01;
import com.rmaj91.interfaces.GamesRepository;
import com.rmaj91.interfaces.Playable;

import javafx.stage.FileChooser;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class GamesRepositoryImpl implements GamesRepository {

    private BoardController boardController;
	private List<Playable> gamesList;



    public void setBoardController(BoardController boardController) {
        this.boardController = boardController;
    }

    public GamesRepositoryImpl() {
		gamesList = new LinkedList<>();
	}


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
		if(gamesList.size()>1){
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
	public int getIndexOfRound(Playable round) {
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
	public boolean saveGame() {

		if(gamesList.isEmpty())
			return false;
        //Show save file dialog
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Drt files (*.drt)", "*.drt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(Main.stage);

        DataOutputStream dataOutputStream;

        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeBoolean(Game01.isDoubleOut());
            dataOutputStream.writeInt(Game01.getPlayersQuantity());
            dataOutputStream.writeInt(Game01.getRoundsMaxNumber());
            dataOutputStream.writeInt(Game01.getStartingPoints());

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(gamesList);

            dataOutputStream.close();
        } catch (IOException exception) {
            System.out.println(exception);
            return false;
        }

        //todo inform window o sukcesie
        System.out.println("Save succefully!");
		return true;
	}


	@Override
	public boolean loadGame() {
        //Show save file dialog
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Drt files (*.drt)", "*.drt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(Main.stage);

        DataInputStream dataInputStream;
        try {
            FileInputStream inputStream = new FileInputStream(file);
            dataInputStream = new DataInputStream(inputStream);
            Game01.setDoubleOut(dataInputStream.readBoolean());
            Game01.setPlayersQuantity(dataInputStream.readInt());
            Game01.setRoundsMaxNumber(dataInputStream.readInt());
            Game01.setStartingPoints(dataInputStream.readInt());

            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            gamesList = (List<Playable>)objectInputStream.readObject();

            dataInputStream.close();
        } catch (IOException exception) {
            System.out.println(exception);
            System.out.println("io exception");
            return false;
        }catch (ClassNotFoundException exception){
            System.out.println(exception);
            System.out.println("class not found exception");
        }

        boardController.initAndDisplay();
        return true;

	}

    @Override
    public void clear() {
        gamesList.clear();
    }

    @Override
    public boolean isEmpty() {
        if(gamesList.isEmpty())
            return true;
        else
            return false;
    }
}
