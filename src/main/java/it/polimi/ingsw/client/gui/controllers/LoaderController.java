package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.gui.tiles.GodTile;
import it.polimi.ingsw.client.messages.ChosenColor;
import it.polimi.ingsw.client.messages.NumberOfPlayers;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.player.PlayerColors;
import it.polimi.ingsw.server.answers.ChallengerMessages;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

public class LoaderController implements GUIController {

    private GUI gui;
    @FXML
    private Label displayStatus;

    public void setText(String text) {
        displayStatus.setText(text);
    }

    public void displayCustomMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText("Message from the server");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public boolean godTile(Card god) {
        Stage godDetails = new Stage();
        GodTile godTile = new GodTile(god, godDetails, gui);
        Scene scene = new Scene(godTile);
        godDetails.setScene(scene);
        godDetails.setResizable(false);
        godDetails.showAndWait();
        return godTile.getValue();
    }

    protected void startingPlayer(ChallengerMessages req) {
        Alert startingPlayer = new Alert(Alert.AlertType.CONFIRMATION);
        startingPlayer.setTitle("Choose starting player");
        startingPlayer.setContentText(req.message);
        HashMap<String, ButtonType> players = new HashMap<>();
        req.players.forEach(n -> players.put(n, new ButtonType(n)));
        startingPlayer.getButtonTypes().setAll(players.values());
        Optional<ButtonType> result = startingPlayer.showAndWait();
        result.ifPresent(buttonType -> gui.getObservers().firePropertyChange("action", null, "STARTER " + req.players.indexOf(buttonType.getText())));
    }

    protected void displayGodList(ChallengerMessages req) {
        ComboBox<String> godListDropdown;
        while (true) {
            Alert godList = new Alert(Alert.AlertType.CONFIRMATION);
            godList.setTitle("Choose a god");
            godListDropdown = new ComboBox<>(FXCollections.observableArrayList(req.godList));
            godList.getDialogPane().setContent(godListDropdown);
            ButtonType ok = new ButtonType("SELECT");
            godList.getButtonTypes().setAll(ok);
            godList.showAndWait();
            if (godListDropdown.getValue()!=null) {
                if(godTile(Card.parseInput(godListDropdown.getValue()))) break;
            }
        }
    }

    protected void chooseGod(ChallengerMessages req) {
        while(true) {
            Alert message = new Alert(Alert.AlertType.INFORMATION);
            message.setTitle("Choose your god power!");
            message.setContentText(req.message + "\n" + req.choosable.stream().map(Enum::toString).collect(Collectors.joining("\n")));
            ComboBox<Card> choices = new ComboBox<>(FXCollections.observableArrayList(req.choosable));
            message.getDialogPane().setContent(choices);
            ButtonType choose = new ButtonType("CHOOSE");
            message.getButtonTypes().setAll(choose);
            message.showAndWait();
            if (choices.getValue()!=null) {
                //TODO
                break;
            }
        }
    }

    public void challengerPhase(ChallengerMessages req) {
        gui.getModelView().toggleInput();
        if (req.startingPlayer && req.players != null) {
            startingPlayer(req);
        }
        else if (req.godList != null) {
            displayGodList(req);
        }
        else if (req.choosable != null) {
            chooseGod(req);
        }
        else {
            Alert message = new Alert(Alert.AlertType.INFORMATION);
            message.setTitle("Message from the server");
            message.setContentText(req.message);
            ButtonType godList = new ButtonType("GODS' LIST");
            message.getButtonTypes().setAll(godList);
            message.showAndWait();
            gui.getObservers().firePropertyChange("action", null, "GODLIST");
        }
    }

    public void requestPlayerNumber(String message){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Looby capacity");
        alert.setHeaderText("Choose the number of players.");
        alert.setContentText(message);

        ButtonType two = new ButtonType("2");
        ButtonType three = new ButtonType("3");

        alert.getButtonTypes().setAll(two, three);
        Optional<ButtonType> result = alert.showAndWait();
        int players = 0;
        if(result.isPresent() && result.get()== two) {
            players=2;
        } else if(result.isPresent() && result.get() == three) {
            players=3;
        }
        gui.getConnection().send(new NumberOfPlayers(players));
    }

    public void requestColor(ArrayList<PlayerColors> colors) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Workers' color");
        alert.setHeaderText("Choose your workers' color!");
        alert.setContentText("Click one of the color below!");
        HashMap<String, ButtonType> buttons = new HashMap<>();
        colors.forEach(n -> buttons.put(n.toString(), new ButtonType(n.toString())));
        alert.getButtonTypes().setAll(buttons.values());
        Optional<ButtonType> result = alert.showAndWait();
        result.ifPresent(buttonType -> gui.getConnection().send(new ChosenColor(PlayerColors.parseInput(buttonType.getText()))));
    }

    public void godDescription(String description) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("God's Description");
        alert.setContentText(description);
        alert.getButtonTypes().setAll(new ButtonType("CLOSE"));
        alert.showAndWait();
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
