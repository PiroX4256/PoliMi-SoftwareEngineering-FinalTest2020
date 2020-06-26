package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.ActionParser;
import it.polimi.ingsw.client.ConnectionSocket;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.exceptions.DuplicateNicknameException;
import it.polimi.ingsw.exceptions.InvalidNicknameException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * MainMenuController class is a controller, bound to the FXML scene of the main menu and the game
 * setup, it handles the event of play button and quit button.
 *
 * @author Luca Pirovano
 * @see GUIController
 */
public class MainMenuController implements GUIController {
  private static final String URL =
      "https://github.com/S0NN1/ing-sw-2020-piemonti-pirovano-sonnino";
  private GUI gui;
  private boolean muted;
  @FXML private TextField username;
  @FXML private TextField address;
  @FXML private TextField port;
  @FXML private Label confirmation;
  @FXML private ImageView music;

  /**
   * Method about opens the project home website.
   *
   * @throws URISyntaxException when the URI is not valid.
   * @throws IOException when the browsing function stops working.
   */
  public void about() throws URISyntaxException, IOException {
    Desktop.getDesktop().browse(new URI(URL));
  }

  /** Method quit kills the application when the "Quit" button is pressed. */
  public void quit() {
    System.out.println("Thanks for playing! See you next time!");
    System.exit(0);
  }

  /** Method play changes the stage scene to the setup one when the button "Play" is pressed. */
  public void play() {
    gui.changeStage("setup.fxml");
    gui.centerApplication();
  }

  /** Method mute mutes game music. */
  public void mute() {
    if (muted) {
      gui.getPlayer().play();
      music.setImage(new Image(getClass().getResourceAsStream("/graphics/icons/speaker.png")));
      muted = false;
    } else {
      gui.getPlayer().stop();
      music.setImage(new Image(getClass().getResourceAsStream("/graphics/icons/mute.png")));
      muted = true;
    }
  }

  /**
   * Method start, bound to the setup FXML scene, it instantiates a socket connection with the
   * remote server and changes the scene to the loader one.
   */
  public void start() {
    if (username.getText().equals("")
        || address.getText().equals("")
        || port.getText().equals("")) {
      confirmation.setText("Error: missing parameters!");
    } else if (username.getText().length() > 15) {
      confirmation.setText("Error: the maximum length of nickname is 15 characters!");
    } else if (address.getText().contains(" ")) {
      confirmation.setText("Error: address must not contain spaces!");
    } else {
      gui.getModelView().setPlayerName(username.getText());
      LoaderController loaderController;
      try {
        Constants.setAddress(address.getText());
        Constants.setPort(Integer.parseInt(port.getText()));
      } catch (NumberFormatException e) {
        confirmation.setText("Error: missing parameters!");
        return;
      }
      try {
        gui.changeStage("loading.fxml");
        gui.centerApplication();
        loaderController = (LoaderController) gui.getControllerFromName("loading.fxml");
        loaderController.setText("CONFIGURING SOCKET CONNECTION...");
        ConnectionSocket connectionSocket = new ConnectionSocket();
        if (!connectionSocket.setup(
            username.getText(), gui.getModelView(), gui.getActionHandler())) {
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle("Error");
          alert.setHeaderText("Server not reachable");
          alert.setContentText(
              "The entered IP/port doesn't match any active server or the server is not "
                  + "running. Please try again!");
          alert.showAndWait();
          gui.changeStage("MainMenu.fxml");
          return;
        }
        gui.setConnectionSocket(connectionSocket);
        loaderController.setText("SOCKET CONNECTION \nSETUP COMPLETED!");
        loaderController.setText("WAITING FOR PLAYERS");
        gui.getListeners()
            .addPropertyChangeListener(
                "action", new ActionParser(connectionSocket, gui.getModelView()));

      } catch (DuplicateNicknameException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Duplicate nickname");
        alert.setHeaderText("Duplicate nickname!");
        alert.setContentText("This nickname is already in use! Please choose another one.");
        alert.showAndWait();
        gui.changeStage("MainMenu.fxml");
      } catch (InvalidNicknameException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid character nickname");
        alert.setHeaderText("Special character contained in nickname!");
        alert.setContentText(
            "Nickname can't contain - special character! Please choose another one");
        alert.showAndWait();
        gui.changeStage("MainMenu.fxml");
      }
    }
  }

  /** @see GUIController#setGui(GUI) */
  @Override
  public void setGui(GUI gui) {
    this.gui = gui;
  }
}
