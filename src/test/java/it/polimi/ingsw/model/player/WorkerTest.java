package it.polimi.ingsw.model.player;

import it.polimi.ingsw.exceptions.OutOfBoundException;
import it.polimi.ingsw.model.board.GameBoard;
import it.polimi.ingsw.model.board.Space;
import it.polimi.ingsw.model.board.Tower;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Nested;

import static org.junit.jupiter.api.Assertions.*;

class WorkerTest {
    Worker worker;

    @BeforeEach
    void init(){
         worker = new Worker(PlayerColors.RED);
    }

    @Test
    void colorTest(){
        Worker workerBlue = new Worker(PlayerColors.BLUE);
        Worker workerRed = new Worker(PlayerColors.RED);
        Worker workerGreen = new Worker(PlayerColors.GREEN);
        System.out.println(workerRed.getWorkerColor() + "red");
        System.out.println(workerGreen.getWorkerColor() + "green");
        System.out.println(workerBlue.getWorkerColor() + "blue");
    }

    @Test
    void constructorTest(){
        Boolean blockExpected = false;
        Space positionExpected = null;
        assertEquals(blockExpected, worker.isBlocked(), "if the worker isn't blocked");
        assertEquals(positionExpected, worker.getPosition(), "if the worker hasn't got a position yet");
    }

    @Test
    @DisplayName("setPosition method and exceptions")
    void setPositionTest() {

        Space space = new Space();
        space.setX(2);
        space.setY(1);
        worker.setPosition(space);
        int expX = 2;
        int expY = 1;
        assertEquals(expX, worker.getPosition().getX());
        assertEquals(expY, worker.getPosition().getY());

        Space nullSpace = null;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {worker.setPosition(nullSpace);});

    }

    @Test
    @DisplayName("move method, exception and winning condition")
    void move() {
        Space nullSpace = null;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {worker.move(nullSpace);});

        Space Space = new Space();
        worker.setPosition(Space);
        Space.setX(1);
        Space.setY(4);
        int expectedX = 1;
        int expectedY = 4;

        assertEquals(expectedX, worker.getPosition().getX() );
        assertEquals(expectedY, worker.getPosition().getY());

        Space space2 = new Space();
        Space space3 = new Space();
        space2.setTower(new Tower());

        for(int i = 0; i < 2; i++) {
            try {
                space2.getTower().addLevel();
            } catch (OutOfBoundException e) {
                e.printStackTrace();
            }
        }
        space3.setTower(new Tower());
        for(int i = 0; i < 3; i++){
            try {
                space3.getTower().addLevel();
            } catch (OutOfBoundException e) {
                e.printStackTrace();
            }
        }

        System.out.println("you should win:");
        worker.setPosition(space2);
        worker.move(space3);
    }

    @Nested
    @DisplayName("getMoves tests")
    class getMovesTest{
        GameBoard gameBoard;

        @BeforeEach
        void init(){
            gameBoard = new GameBoard();
            for(int i=1; i<4; i++){
                for(int j=1; j<4; j++){
                    gameBoard.getSpace(i,j).setTower(new Tower());
                }
            }
        }

        @Test
        @DisplayName("getMoves test without towers")
        void getMoves(){

            worker.setPosition(gameBoard.getSpace(2, 2));
            int expectedMovesCenter = 8;
            assertEquals(expectedMovesCenter, worker.getMoves(gameBoard).size());

            int expectedMovesBorder = 5;
            worker.setPosition(gameBoard.getSpace(0, 3));
            assertEquals(expectedMovesBorder, worker.getMoves(gameBoard).size());
            worker.setPosition(gameBoard.getSpace(4, 3));
            assertEquals(expectedMovesBorder, worker.getMoves(gameBoard).size());
            worker.setPosition(gameBoard.getSpace(3, 0));
            assertEquals(expectedMovesBorder, worker.getMoves(gameBoard).size());
            worker.setPosition(gameBoard.getSpace(3, 4));
            assertEquals(expectedMovesBorder, worker.getMoves(gameBoard).size());

            int expectedMovesCorner = 3;
            worker.setPosition(gameBoard.getSpace(0, 0));
            assertEquals(expectedMovesCorner, worker.getMoves(gameBoard).size());
            worker.setPosition(gameBoard.getSpace(0, 4));
            assertEquals(expectedMovesCorner, worker.getMoves(gameBoard).size());
            worker.setPosition(gameBoard.getSpace(4, 0));
            assertEquals(expectedMovesCorner, worker.getMoves(gameBoard).size());
            worker.setPosition(gameBoard.getSpace(4, 4));
            assertEquals(expectedMovesCorner, worker.getMoves(gameBoard).size(), "the worker moves correctly without towers around");
        }

        @Test
        @DisplayName("getMoves from different height test ")
        void getMovesHeightTest() throws OutOfBoundException {

            gameBoard.getSpace(1, 1).getTower().addLevel();
            gameBoard.getSpace(1, 1).getTower().addLevel();
            gameBoard.getSpace(1, 2).getTower().addLevel();
            gameBoard.getSpace(2, 1).getTower().addLevel();
            gameBoard.getSpace(2, 1).getTower().addLevel();
            gameBoard.getSpace(2, 1).getTower().addLevel();
            gameBoard.getSpace(2, 3).getTower().addLevel();
            gameBoard.getSpace(2, 3).getTower().addLevel();
            gameBoard.getSpace(2, 3).getTower().addLevel();
            gameBoard.getSpace(2, 3).getTower().addLevel();
            gameBoard.getSpace(3, 1).getTower().addLevel();
            gameBoard.getSpace(3, 3).getTower().addLevel();
            gameBoard.getSpace(3, 3).getTower().addLevel();
            gameBoard.getSpace(3, 3).getTower().addLevel();

            int expected0 = 4;
            int expected1 = 5;
            int expected2 = 7;
            int expected3 = 7;

            worker.setPosition(gameBoard.getSpace(2, 2));
            assertEquals(expected0, worker.getMoves(gameBoard).size());

            worker.setPosition(gameBoard.getSpace(2, 2));
            gameBoard.getSpace(2, 2).getTower().addLevel();
            assertEquals(expected1, worker.getMoves(gameBoard).size());

            worker.setPosition(gameBoard.getSpace(2, 2));
            gameBoard.getSpace(2, 2).getTower().addLevel();
            assertEquals(expected2, worker.getMoves(gameBoard).size());

            worker.setPosition(gameBoard.getSpace(2, 2));
            gameBoard.getSpace(2, 2).getTower().addLevel();
            assertEquals(expected3, worker.getMoves(gameBoard).size(), "the worker moves correctly from any height to any height");
        }

        @Test
        @DisplayName("getMoves exception test")
        void getMovesButBlocked() throws OutOfBoundException {
            GameBoard gameBoard = new GameBoard();
            for(int i=0; i<3; i++){
                for(int j=0; j<3; j++){
                    gameBoard.getSpace(i,j).setTower(new Tower());
                    if(i != 1 || j != 1){
                        for(int k=0; k<4; k++){
                            gameBoard.getSpace(i,j).getTower().addLevel();
                        }
                    }
                }
            }

            worker.setPosition(gameBoard.getSpace(1,1));
            Exception exception = assertThrows(IllegalStateException.class, () -> {worker.getMoves(gameBoard);});
        }

    }

    @Test
    void getBuildableSpaces() {
    }
}