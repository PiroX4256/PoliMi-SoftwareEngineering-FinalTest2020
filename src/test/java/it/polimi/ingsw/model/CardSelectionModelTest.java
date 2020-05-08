package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.OutOfBoundException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Luca Pirovano
 */
class CardSelectionModelTest {

    CardSelectionModel testModel;
    Game game;

    /**
     * Setup of the test, with the creation of 2 players.
     */
    @BeforeEach
    void setUp() {
        game = new Game();
        testModel = new CardSelectionModel(game.getDeck());
        game.createNewPlayer(new Player("Luca", 0));
        game.createNewPlayer(new Player("Alice", 1));
        game.getActivePlayers().get(0).setColor(PlayerColors.RED);
        game.getActivePlayers().get(1).setColor(PlayerColors.GREEN);
    }

    /**
     * Insert test in standard conditions.
     */
    @Test
    @DisplayName("Insert test in standard conditions")
    void testInsertStandard() throws OutOfBoundException{
        testModel.addToDeck(Card.PROMETHEUS);
        testModel.addToDeck(Card.APOLLO);
        assertEquals(2, game.getDeck().getCards().size());
        assertEquals(Card.PROMETHEUS, game.getDeck().getCards().get(0));
        assertEquals(Card.APOLLO, game.getDeck().getCards().get(1));
    }

    /**
     * Insert test with duplicate values check.
     */
    @Test
    @DisplayName("Insert test with duplicate values")
    void testInsertDuplicate() throws OutOfBoundException {
        testModel.addToDeck(Card.ATHENA);
        assertEquals(1, game.getDeck().getCards().size());
        assertEquals(Card.ATHENA, game.getDeck().getCards().get(0));
        testModel.addToDeck(Card.ATHENA);
        assertEquals(1, game.getDeck().getCards().size());
        assertEquals(Card.ATHENA, game.getDeck().getCards().get(0));
        testModel.addToDeck(Card.PAN);
        assertEquals(2, game.getDeck().getCards().size());
        assertEquals(Card.PAN, game.getDeck().getCards().get(1));
    }

    /**
     * Insert test with "out of bound" cards condition.
     */
    @Test
    @DisplayName("Insert test with out of bound cards")
    void testInsertOutOfBound() throws OutOfBoundException {
        testModel.addToDeck(Card.APOLLO);
        testModel.addToDeck(Card.ATHENA);
        assertThrows(OutOfBoundException.class, () -> testModel.addToDeck(Card.PROMETHEUS));
    }

    /**
     * Setting of the selected god.
     */
    @Test
    @DisplayName("Setting of the selected god")
    void setGodTest() {
        for(Card card:Card.values()) {
            testModel.setSelectedGod(card);
            assertEquals(testModel.getSelectedGod(), card);
        }
    }
}