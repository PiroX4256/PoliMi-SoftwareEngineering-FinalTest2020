package it.polimi.ingsw.observer.workerListeners;

import it.polimi.ingsw.model.board.Space;
import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.answers.SelectSpacesMessage;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

public class SelectSpacesListener extends WorkerListener {

    public SelectSpacesListener(VirtualClient client) {
        super(client);
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        SelectSpacesMessage message = new SelectSpacesMessage((ArrayList<Space>)evt.getNewValue());
        virtualClient.send(message);
    }
}
