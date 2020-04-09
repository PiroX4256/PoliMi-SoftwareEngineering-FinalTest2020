package it.polimi.ingsw.observer.workerListeners;

import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.server.VirtualClient;
import it.polimi.ingsw.server.answers.worker.WinMessage;

import java.beans.PropertyChangeEvent;

public class WinListener extends WorkerListener {

    public WinListener(VirtualClient client) {
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
        WinMessage message = new WinMessage((Worker)evt.getSource());
        virtualClient.send(message);
    }
}
