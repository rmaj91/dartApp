package com.rmaj91.repository;

import com.rmaj91.Main;
import com.rmaj91.domain.Cricket;
import com.rmaj91.domain.Game01;
import com.rmaj91.domain.MasterCricket;
import com.rmaj91.interfaces.GamesRepository;
import com.rmaj91.interfaces.GamesInterface;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GamesRepositoryImpl implements GamesRepository {

    //==================================================================================================
    // Games Collection
    //==================================================================================================
    private List<GamesInterface> gamesList;


    //==================================================================================================
    // Constructors
    //==================================================================================================
    public GamesRepositoryImpl() {
        gamesList = new LinkedList<>();
    }


    //==================================================================================================
    // Public Methods from GamesRepository interface
    //==================================================================================================
    @Override
    public void pushRound(GamesInterface round) {
        gamesList.add(round);
    }

    @Override
    public GamesInterface getCurrentRound() {
        return gamesList.get(gamesList.size() - 1);
    }

    @Override
    public boolean pullRound() {
        if (gamesList.size() > 2) {
            GamesInterface round = gamesList.get(gamesList.size() - 1);
            gamesList.remove(round);
            return true;
        } else
            return false;
    }

    @Override
    public void createNewGame(GamesInterface round) {
        gamesList = new LinkedList<>();
        gamesList.add(round);
    }

    @Override
    public int getNumberOfRound(GamesInterface round) {
        return gamesList.indexOf(round);
    }

    @Override
    public GamesInterface getPreviousRound() {
        if (gamesList.size() == 1)
            return null;
        else
            return gamesList.get(gamesList.size() - 2);
    }

    @Override
    public GamesInterface getZeroRound() {
        return gamesList.get(0);
    }

    @Override
    public boolean saveGame() {

        if (gamesList.isEmpty())
            return false;
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Drt files (*.drt)", "*.drt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(Main.stage);
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(file);
        } catch (Exception e) {
            return false;
        }

        try {
            outputStream = new FileOutputStream(file);
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(gamesList);

            String className = this.getCurrentRound().getClass().getName();
            dataOutputStream.writeUTF(className);
            if (className.equals("com.rmaj91.domain.Game01")) {
                writeGame01StaticVariables(dataOutputStream);
            } else if (className.equals("com.rmaj91.domain.Cricket")) {
                writeCricketStaticVariables(dataOutputStream);
                objectOutputStream.writeObject(Cricket.getFieldsToThrow());
            } else if (className.equals("com.rmaj91.domain.MasterCricket")) {
                writeMasterCricketStaticVariables(dataOutputStream);
                objectOutputStream.writeObject(MasterCricket.getFieldsToThrow());
            }

            dataOutputStream.close();
        } catch (Exception exception) {
            return false;
        }

        showInformationWindow("Game Saved!", "Game saved successfully :)");
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
        } catch (Exception exception) {
            return false;
        }

        try {
            dataInputStream = new DataInputStream(inputStream);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            gamesList = (List<GamesInterface>) objectInputStream.readObject();

            String className = dataInputStream.readUTF();
            if (className.equals("com.rmaj91.domain.Game01"))
                loadGame01StaticVariables(dataInputStream);
            else if (className.equals("com.rmaj91.domain.Cricket"))
                loadCricketStaticVariables(dataInputStream, objectInputStream);
            else if (className.equals("com.rmaj91.domain.MasterCricket"))
                loadMasterCricketStaticVariables(dataInputStream, objectInputStream);


            dataInputStream.close();
        } catch (Exception exception) {
            showInformationWindow("Error while loading", "Game cannot be loaded!");
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


    //==================================================================================================
    // Private Methods
    //==================================================================================================
    private void showInformationWindow(String title, String header) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.showAndWait();
    }


    private void writeMasterCricketStaticVariables(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeInt(MasterCricket.getNumberOfPlayers());
        dataOutputStream.writeInt(MasterCricket.getMaxNumberOfRounds());
        dataOutputStream.writeInt(MasterCricket.getCurrentFieldToThrowIndex());
    }

    private void writeCricketStaticVariables(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeInt(Cricket.getNumberOfPlayers());
        dataOutputStream.writeInt(Cricket.getMaxNumberOfRounds());
        dataOutputStream.writeInt(Cricket.getCurrentFieldToThrowIndex());
    }

    private void writeGame01StaticVariables(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeBoolean(Game01.isDoubleOut());
        dataOutputStream.writeInt(Game01.getNumberOfPlayers());
        dataOutputStream.writeInt(Game01.getMaxNumberOfRounds());
    }

    private void loadMasterCricketStaticVariables(DataInputStream dataInputStream, ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        MasterCricket.setNumberOfPlayers(dataInputStream.readInt());
        MasterCricket.setMaxNumberOfRounds(dataInputStream.readInt());
        MasterCricket.setCurrentFieldToThrowIndex(dataInputStream.readInt());
        MasterCricket.setFieldsToThrow((ArrayList<Integer>) objectInputStream.readObject());
    }

    private void loadCricketStaticVariables(DataInputStream dataInputStream, ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        Cricket.setNumberOfPlayers(dataInputStream.readInt());
        Cricket.setMaxNumberOfRounds(dataInputStream.readInt());
        Cricket.setCurrentFieldToThrowIndex(dataInputStream.readInt());
        Cricket.setFieldsToThrow((ArrayList<Integer>) objectInputStream.readObject());
    }

    private void loadGame01StaticVariables(DataInputStream dataInputStream) throws IOException {
        Game01.setDoubleOut(dataInputStream.readBoolean());
        Game01.setNumberOfPlayers(dataInputStream.readInt());
        Game01.setMaxNumberOfRounds(dataInputStream.readInt());
    }


}
