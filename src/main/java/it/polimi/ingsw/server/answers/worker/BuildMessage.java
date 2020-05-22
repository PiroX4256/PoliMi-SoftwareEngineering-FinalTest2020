package it.polimi.ingsw.server.answers.worker;

import it.polimi.ingsw.model.board.Space;
import it.polimi.ingsw.server.answers.Answer;
import it.polimi.ingsw.constants.Couple;

/**
 * @author Alice Piemonti
 */
public class BuildMessage implements Answer {

    private final Couple message;
    private final boolean dome;


    public  BuildMessage(Space space, boolean dome){
        message = new Couple(space.getRow(), space.getColumn());
        this.dome = dome;
    }

    @Override
    public Couple getMessage() {
        return message;
    }

    public boolean getDome(){ return dome;}
}
