package it.polimi.ingsw.client.messages.actions.workeractions;

import it.polimi.ingsw.client.messages.actions.UserAction;

/**
 * UserAction sent by the client to the server, defines an interface to worker actions.
 * @author Alice Piemonti
 * @see UserAction
 */
public abstract class WorkerAction implements UserAction {


    /**
     * Method getMessage returns the message of this WorkerAction object.
     *
     *
     *
     * @return the message (type Object) of this WorkerAction object.
     */
    public abstract Object getMessage();
}
