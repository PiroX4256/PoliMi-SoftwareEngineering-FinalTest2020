package it.polimi.ingsw.client.messages.actions.workerActions;

import it.polimi.ingsw.client.messages.actions.UserAction;

/**
 * @author Alice Piemonti
 */
public abstract class WorkerAction implements UserAction {

    public abstract Object getMessage();
}
