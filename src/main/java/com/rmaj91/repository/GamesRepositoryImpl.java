package com.rmaj91.repository;

import com.rmaj91.Main;
import com.rmaj91.controller.BoardController;
import com.rmaj91.domain.Game01;
import com.rmaj91.interfaces.GamesRepository;
import com.rmaj91.interfaces.Playable;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import java.io.*;
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
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            // writing class name
            dataOutputStream.writeUTF(this.getCurrentRound().getClass().getName());
            dataOutputStream.writeBoolean(Game01.isDoubleOut());
            dataOutputStream.writeInt(Game01.getPlayersQuantity());
            dataOutputStream.writeInt(Game01.getRoundsMaxNumber());
            dataOutputStream.writeInt(Game01.getStartingPoints());

            //todo for other games classes
            //System.out.println(this.getCurrentRound().getClass().getName());

            // Serializing List
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(gamesList);
            dataOutputStream.close();
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }

        showInformationWindow();
		return true;
	}

    @Override
	public boolean loadGame() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Drt files (*.drt)", "*.drt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(Main.stage);

        DataInputStream dataInputStream;
        try {
            FileInputStream inputStream = new FileInputStream(file);
            dataInputStream = new DataInputStream(inputStream);


            //reads class name
            //todo for other game classes
            String className = dataInputStream.readUTF();
            if(className.equals("com.rmaj91.domain.Game01")){
                Game01.setDoubleOut(dataInputStream.readBoolean());
                Game01.setPlayersQuantity(dataInputStream.readInt());
                Game01.setRoundsMaxNumber(dataInputStream.readInt());
                Game01.setStartingPoints(dataInputStream.readInt());
                // Reading Serialized List
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                gamesList = (List<Playable>) objectInputStream.readObject();
                dataInputStream.close();
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
        this.getCurrentRound().initAndDisplay();

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
    private void showInformationWindow() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Saved!");
        alert.setHeaderText("Game saved successfully :)");
        alert.showAndWait();
    }
}
