package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    Player testPlayer;
    private static final String nickname = "player";
    private static final int clientID = 0;

    @BeforeEach
    void setUp() {
        testPlayer = new Player(nickname, clientID);
    }

    @Test
    @DisplayName("Nickname/client ID getter test")
    void nicknameTest() {
        assertEquals(testPlayer.getNickname(), nickname);
        assertEquals(testPlayer.getClientID(), clientID);
    }

    @Test
    @DisplayName("Color setting/getting test")
    void colorTest() {
        testPlayer.setColor(PlayerColors.GREEN);
        assertEquals(testPlayer.getColor(), PlayerColors.GREEN);
    }

    @Test
    @DisplayName("God Card addition test")
    void cardAdditionTest() {
        testPlayer.setColor(PlayerColors.RED);
        testPlayer.setCard(Card.ATLAS);
        assertTrue(testPlayer.getWorkers().get(0).getClass().toString().toUpperCase().contains(Card.ATLAS.toString()));
    }

    @Test
    @DisplayName("Player and worker creation test")
    void playerWorkerCreation() {
        for(Card card:Card.values()) {
            Player player = new Player("test", 1);
            player.setColor(PlayerColors.RED);
            player.addWorker(card);
            assertTrue(player.getWorkers().get(0).getClass().toString().toUpperCase().contains(card.toString()));
        }
    }
}