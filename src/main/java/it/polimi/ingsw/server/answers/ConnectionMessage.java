package it.polimi.ingsw.server.answers;
//Connection was successfully set-up! You are now connected.
public class ConnectionMessage implements Answer {
    private final int type; //0: connection confirmation, 1: connection termination
    private final String message;
    private final String header;

    public ConnectionMessage(String message, int type) {
        this.message = message;
        this.type = type;
        header = getClass().getName();
    }

    public String getHeader() {
        return null;
    }

    public String getMessage() {
        return message;
    }
    public int getType() {
        return type;
    }
}
