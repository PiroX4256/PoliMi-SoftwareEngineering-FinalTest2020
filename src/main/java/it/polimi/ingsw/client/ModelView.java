package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.constants.Couple;
import it.polimi.ingsw.server.answers.*;

import java.util.ArrayList;
import java.util.List;

/**
 * ModelView class contains a small representation of the game model, and contains linking to the main client actions,
 * which is invoked after an instance control.
 * @author Alice Piemonti, Nicolò Sonnino
 */
public class ModelView {

    private Answer serverAnswer;
    private final CLI cli;
    private boolean canInput;
    private int gamePhase;
    private final ClientBoard clientBoard;
    private final GUI gui;
    private volatile int started;
    private int activeWorker;
    private String playerName;
    private String god;

    /**
     * Method getGodDesc returns the godDesc of this ModelView object.
     *
     *
     *
     * @return the godDesc (type String) of this ModelView object.
     */
    public String getGodDesc() {
        return godDesc;
    }

    /**
     * Method setGodDesc sets the godDesc of this ModelView object.
     *
     *
     *
     * @param godDesc the godDesc of this ModelView object.
     *
     */
    public void setGodDesc(String godDesc) {
    if(godDesc.length()>110){
        String temp1=godDesc.substring(0, 110);
        String temp2=godDesc.substring(110);
        if(Character.toString(temp2.charAt(0)).equals(" ")){
            godDesc= temp1 + "\n" + temp2.substring(1);
        }
        else{
            godDesc= temp1 + "-" + "\n" + temp2;
        }
    }
        this.godDesc = godDesc;
    }

    private String godDesc;
    private int turnPhase;
    private boolean turnActive;
    private boolean buildSelected;
    private boolean moveSelected;
    private List<Couple> selectSpaces= new ArrayList<>();

    /**
     * Method getSelectSpaces returns the selectSpaces of this ModelView object.
     *
     *
     *
     * @return the selectSpaces (type List<Couple>) of this ModelView object.
     */
    public List<Couple> getSelectSpaces() {
        return selectSpaces;
    }

    /**
     * Method setSelectSpaces sets the selectSpaces of this ModelView object.
     *
     *
     *
     * @param selectSpaces the selectSpaces of this ModelView object.
     *
     */
    public void setSelectSpaces(List<Couple> selectSpaces) {
        this.selectSpaces = selectSpaces;
    }

    /**
     * Method getPlayerName returns the playerName of this ModelView object.
     *
     *
     *
     * @return the playerName (type String) of this ModelView object.
     */
    public String getPlayerName() {
        return playerName;
    }



    /**
     * Method setPlayerName sets the playerName of this ModelView object.
     *
     *
     *
     * @param playerName the playerName of this ModelView object.
     *
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Method getTurnPhase returns the turnPhase of this ModelView object.
     *
     *
     *
     * @return the turnPhase (type int) of this ModelView object.
     */
    public int getTurnPhase() {
        return turnPhase;
    }

    /**
     * Method setTurnPhase sets the turnPhase of this ModelView object.
     *
     *
     *
     * @param turnPhase the turnPhase of this ModelView object.
     *
     */
    public void setTurnPhase(int turnPhase) {
        this.turnPhase = turnPhase;
    }

    /**
     * Method getColor returns the color of this ModelView object.
     *
     *
     *
     * @return the color (type String) of this ModelView object.
     */
    public String getColor() {
        return color;
    }

    /**
     * Method setColor sets the color of this ModelView object.
     *
     *
     *
     * @param color the color of this ModelView object.
     *
     */
    public void setColor(String color) {
        this.color = color;
    }

    private String color;

    /**
     * Method getGod returns the god of this ModelView object.
     *
     *
     *
     * @return the god (type String) of this ModelView object.
     */
    public String getGod() {
        return god;
    }

    /**
     * Method setGod sets the god of this ModelView object.
     *
     *
     *
     * @param god the god of this ModelView object.
     *
     */
    public void setGod(String god) {
        this.god = god;
    }

    /**
     * Method isTurnActive returns the turnActive of this ModelView object.
     *
     *
     *
     * @return the turnActive (type boolean) of this ModelView object.
     */
    public boolean isTurnActive() {
        return turnActive;
    }

    /**
     * Method setTurnActive sets the turnActive of this ModelView object.
     *
     *
     *
     * @param turnActive the turnActive of this ModelView object.
     *
     */
    public void setTurnActive(boolean turnActive) {
        this.turnActive = turnActive;
    }

    /**
     * Method getActiveWorker returns the activeWorker of this ModelView object.
     *
     *
     *
     * @return the activeWorker (type int) of this ModelView object.
     */
    public int getActiveWorker() {
        return activeWorker;
    }

    /**
     * Method setActiveWorker sets the activeWorker of this ModelView object.
     *
     *
     *
     * @param activeWorker the activeWorker of this ModelView object.
     *
     */
    public void setActiveWorker(int activeWorker) {
        this.activeWorker = activeWorker;
    }

    /**
     * Constructor ModelView creates a new ModelView instance.
     *
     * @param cli of type CLI
     */
    public ModelView(CLI cli) {
        this.cli = cli;
        gamePhase = 0;
        clientBoard = new ClientBoard();
        gui = null;
        activeWorker = 0;
    }

    /**
     * Constructor ModelView creates a new ModelView instance.
     *
     * @param gui of type GUI
     */
    public ModelView(GUI gui) {
        this.gui = gui;
        this.clientBoard = new ClientBoard();
        this.cli = null;
        gamePhase = 0;
        activeWorker = 0;
    }

    /**
     * Method getBoard returns the board of this ModelView object.
     *
     *
     *
     * @return the board (type ClientBoard) of this ModelView object.
     */
    public synchronized ClientBoard getBoard() {
        return clientBoard;
    }

    /**
     * Method getCli returns the cli of this ModelView object.
     *
     *
     *
     * @return the cli (type CLI) of this ModelView object.
     */
    public CLI getCli() {
        return cli;
    }

    /**
     * Method getGui returns the gui of this ModelView object.
     *
     *
     *
     * @return the gui (type GUI) of this ModelView object.
     */
    public GUI getGui() {
        return gui;
    }

    /**
     * Method setStarted sets the started of this ModelView object.
     *
     *
     *
     * @param val the started of this ModelView object.
     *
     */
    public synchronized void setStarted(int val) {
        started = val;
    }

    /**
     * Method getStarted returns the started of this ModelView object.
     *
     *
     *
     * @return the started (type int) of this ModelView object.
     */
    public synchronized int getStarted() {
        return started;
    }

    /**
     * Method setBuildSelected sets the buildSelected of this ModelView object.
     *
     *
     *
     * @param buildSelected the buildSelected of this ModelView object.
     *
     */
    public void setBuildSelected(boolean buildSelected) {
        this.buildSelected = buildSelected;
    }

    /**
     * Method setMoveSelected sets the moveSelected of this ModelView object.
     *
     *
     *
     * @param moveSelected the moveSelected of this ModelView object.
     *
     */
    public void setMoveSelected(boolean moveSelected) {
        this.moveSelected = moveSelected;
    }

    /**
     * Method setGamePhase sets the game phase variable.
     *
     * @param phase the current phase of the game.
     */
    public void setGamePhase(int phase) {
        gamePhase = phase;
    }


    /**
     * Method getGamePhase returns the gamePhase of this ModelView object.
     *
     *
     *
     * @return the gamePhase (type int) of this ModelView object.
     */
    public int getGamePhase() {
        return gamePhase;
    }


    /**
     * Method activateInput activates the input of the main user class.
     */
    public synchronized void activateInput() {
        canInput = true;
    }

    /**
     * Method activateInput deactivates the input of the main user class.
     */
    public synchronized void deactivateInput() {
        canInput = false;
    }


    /**
     * Method getCanInput returns the canInput of this ModelView object.
     *
     *
     *
     * @return the canInput (type boolean) of this ModelView object.
     */
    public synchronized boolean getCanInput() {
        return canInput;
    }


    /**
     * Method setCanInput sets the canInput of this ModelView object.
     *
     *
     *
     * @param value the canInput of this ModelView object.
     *
     */
    public synchronized void setCanInput(boolean value) {
        canInput = value;
    }

    /**
     * Method setServerAnswer sets the serverAnswer of this ModelView object.
     *
     *
     *
     * @param answer the serverAnswer of this ModelView object.
     *
     */
    public void setServerAnswer(Answer answer) {
        this.serverAnswer = answer;
    }


    /**
     * Method getServerAnswer returns the serverAnswer of this ModelView object.
     *
     *
     *
     * @return the serverAnswer (type Answer) of this ModelView object.
     */
    public Answer getServerAnswer() {
        return serverAnswer;
    }

    /**
     * Method unregisterPlayer removes loser's workers from clientBoard.
     *
     * @param loserColor of type String the loser's color.
     */
    public void unregisterPlayer(String loserColor) {
        for (int i = Constants.GRID_MIN_SIZE; i < Constants.GRID_MAX_SIZE; i++) {
            for (int j = Constants.GRID_MIN_SIZE; j < Constants.GRID_MAX_SIZE; j++) {
                if(clientBoard.getGrid()[i][j].getColor()!=null && clientBoard.getGrid()[i][j].getColor().equalsIgnoreCase(loserColor)){
                    clientBoard.getGrid()[i][j].setColor(null);
                    clientBoard.getGrid()[i][j].setWorkerNum(0);
                }
            }
        }
    }
}
