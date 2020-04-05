package it.polimi.ingsw.model.player.gods;

import it.polimi.ingsw.model.board.Space;
import it.polimi.ingsw.model.player.PlayerColors;
import it.polimi.ingsw.model.player.Worker;

public class Pan extends Worker {
    public Pan(PlayerColors color) {
        super(color);
    }

    /**
     * change the worker's position while check winning condition
     * there's another winning condition
     * @param space the new position
     * @throws IllegalArgumentException if space is null
     */
    @Override
    public void move(Space space) throws IllegalArgumentException {
        if((position.getTower().getHeight() - space.getTower().getHeight() ) > 1){
            super.move(space);
            System.out.println("bravo hai vinto per il potere di Pan"); //wiiiiiin
        }
        super.move(space);
    }
}