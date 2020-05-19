package it.polimi.ingsw.controller;

import it.polimi.ingsw.client.messages.actions.ChallengerPhaseAction;
import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.CardSelectionModel;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.server.GameHandler;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.answers.Answer;
import it.polimi.ingsw.server.answers.CustomMessage;
import it.polimi.ingsw.server.answers.ChallengerMessages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class GodSelectionControllerTest {

    private static class VirtualClientStub extends VirtualClient {
        private boolean notified = false;
        private List<String> gods;
        private String message;

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            notified = true;
            if (evt.getNewValue() instanceof ChallengerMessages) {
                if (((ChallengerMessages)evt.getNewValue()).getMessage()!=null) message = ((ChallengerMessages) evt.getNewValue()).getMessage();
                else gods = ((ChallengerMessages) evt.getNewValue()).getGodList();
            }
            else {
                message = ((CustomMessage)evt.getNewValue()).getMessage();
            }
        }
    }

    private static class GameHandlerStub extends GameHandler {
        public int started = 1;

        public GameHandlerStub(Server server) {
            super(server);
        }

        @Override
        public int getCurrentPlayerID() {
            return 0;
        }

        @Override
        public void singleSend(Answer message, int id) {
            System.out.println("OK");
        }

        @Override
        public void sendAllExcept(Answer message, int excludedID) {
            System.out.println(Constants.ANSI_RED + "\nSemi-broadcast message: " + Constants.ANSI_RESET + message.getMessage());
        }

        @Override
        public void sendAll(Answer message) {
            System.out.println(Constants.ANSI_RED + "\nFull-broadcast message: " + Constants.ANSI_RESET + message.getMessage());
        }

        @Override
        public int isStarted() {
            return started;
        }

    }

    private class GameStub extends Game {
        private final DeckStub deck;
        public GameStub() {
            this.deck = new DeckStub(this);
        }

        @Override
        public Deck getDeck() {
            return deck;
        }
    }

    private static class DeckStub extends Deck {
        public DeckStub(Game game) {
            super(game);
        }
        @Override
        public boolean chooseCard(Card card, VirtualClient client) {
            if(!super.getCards().contains(card)) {
                return false;
            }
            super.getCards().remove(card);
            return true;
        }
    }

    final Server server = new Server();
    final GameStub game = new GameStub();
    final GameHandlerStub gameHandler = new GameHandlerStub(server);
    final Controller controller = new Controller(game, gameHandler);
    final VirtualClientStub virtualClient = new VirtualClientStub();
    final GodSelectionController selectionController = new GodSelectionController(new CardSelectionModel(game.getDeck()), controller, virtualClient);

    @BeforeEach
    void setUp() {
        controller.getModel().createNewPlayer(new Player("Luca", 0));
        controller.getModel().createNewPlayer(new Player("Alice", 1));
    }

    @Test
    @DisplayName("God Selection flow management test, all cases")
    void selectionFlowTest() {
        controller.getModel().setCurrentPlayer(controller.getModel().getActivePlayers().get(0));
        //God list and description testing

        selectionController.propertyChange(new PropertyChangeEvent(this, null, null, new ChallengerPhaseAction("LIST", null)));
        assertTrue(virtualClient.notified);
        assertEquals(Card.godsName(), virtualClient.gods);
        virtualClient.notified = false;
        selectionController.propertyChange(new PropertyChangeEvent(this, null, null, new ChallengerPhaseAction("DESC", Card.APOLLO)));
        assertTrue(virtualClient.notified);
        assertEquals(virtualClient.message, Card.APOLLO.godsDescription());

        //God deck addition test
        assertTrue(selectionController.add(Card.APOLLO));
        assertTrue(virtualClient.message.contains("God APOLLO has been added"));
        assertFalse(selectionController.add(Card.APOLLO));

        assertTrue(selectionController.add(Card.PAN));
        assertTrue(virtualClient.message.contains("God PAN has been added"));
        assertFalse(selectionController.add(Card.PROMETHEUS));

        //God choosing test
        gameHandler.started = 2;
        controller.getModel().setCurrentPlayer(controller.getModel().getActivePlayers().get(0));
        assertFalse(selectionController.lastSelection());
        assertTrue(selectionController.choose(Card.APOLLO));
        assertFalse(selectionController.choose(Card.APOLLO));
        assertFalse(selectionController.choose(Card.ATHENA));
        assertTrue(selectionController.lastSelection());
    }
}