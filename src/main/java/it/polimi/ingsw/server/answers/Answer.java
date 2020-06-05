package it.polimi.ingsw.server.answers;

import java.io.Serializable;

/**
 * Interface for server answers. It's implemented by several messages, which represent a specific type of communication
 * that the server would have with the client.
 * @author Luca Pirovano, Nicolò Sonnino, Alice Piemonti (of the entire package)
 */
public interface Answer extends Serializable {
    /**
     * Method getMessage returns the message of this WorkerPlacement object.
     *
     *
     *
     * @return the message (type Object) of this WorkerPlacement object.
     */
    Object getMessage();
}
