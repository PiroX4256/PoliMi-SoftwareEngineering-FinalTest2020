package it.polimi.ingsw.client;

import it.polimi.ingsw.client.messages.Disconnect;
import it.polimi.ingsw.client.messages.actions.ChallengerPhaseAction;
import it.polimi.ingsw.client.messages.actions.WorkerSetupMessage;
import it.polimi.ingsw.client.messages.actions.turnactions.StartTurnAction;
import it.polimi.ingsw.client.messages.actions.workeractions.*;
import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.constants.Couple;
import it.polimi.ingsw.model.Card;

import java.util.Objects;


/**
 * Check the correctness of the input received from the ActionParser, returning either true or false after his check.
 *
 * @author Luca Pirovano, Nicolò Sonnino
 */
public class InputChecker {
    public static final String ERR_NONEXISTENT_UNREACHABLE = "Non-existent or unreachable cell, operation not permitted!";
    public static final String ERR_CELL_OCCUPIED = "Cell already occupied, operation not permitted!";
    public static final String ERR_INCORRECT_ACTION = "Incorrect action, wrong turn phase!";
    public static final String ERR_WORKER_NOT_SELECTED = "Worker not selected, operation not permitted!";
    public static final String CELL_WITH_DOME = "Cell with dome, operation not permitted!";
    private static final String GOD_NOT_FOUND = "Not existing god with your input's name.";
    private static final String RED = Constants.ANSI_RED;
    private static final String RST = Constants.ANSI_RESET;
    private final ConnectionSocket connection;
    private final ModelView modelView;


    public InputChecker(ConnectionSocket connection, ModelView modelView) {
        this.connection = connection;
        this.modelView = modelView;
    }

    /**
     * Validates a "GODDESC <god-name>" message type.
     *
     * @param in the user input under array representation.
     * @return true if the input is valid and sent to the server, false otherwise.
     */
    public ChallengerPhaseAction desc(String[] in) {
        ChallengerPhaseAction challengerPhaseAction;
        try {
            challengerPhaseAction = new ChallengerPhaseAction("DESC", Card.parseInput(in[1]));
        } catch (IllegalArgumentException e) {
            System.out.println(RED + GOD_NOT_FOUND + RST);
            return null;
        }
        return challengerPhaseAction;
    }

    /**
     * Validates an "ADDGOD <god-name>" message type.
     *
     * @param in the user input under array representation.
     * @return true if the input is valid and sent to the server, false otherwise.
     */
    public ChallengerPhaseAction addGod(String[] in) {
        ChallengerPhaseAction action;
        try {
            action = new ChallengerPhaseAction("ADD", Card.parseInput(in[1]));
        } catch (IllegalArgumentException e) {
            System.out.println(RED + GOD_NOT_FOUND + RST);
            return null;
        }
        return action;
    }

    /**
     * Validates a "CHOOSE <god-name>" message type.
     *
     * @param in the user input under array representation.
     * @return true if the input is valid and sent to the server, false otherwise.
     */
    public ChallengerPhaseAction choose(String[] in) {
        ChallengerPhaseAction action;
        try {
            action = new ChallengerPhaseAction("CHOOSE", Card.parseInput(in[1]));
        } catch (IllegalArgumentException e) {
            System.out.println(RED + GOD_NOT_FOUND + RST);
            return null;
        }
        return action;
    }

    /**
     * Validates a "STARTER <player-number>" starting player message type.
     *
     * @param in the user input under array representation.
     * @return true if the input is valid and sent to the server, false otherwise.
     */
    public ChallengerPhaseAction starter(String[] in) {
        ChallengerPhaseAction action;
        try {
            int startingPlayer = Integer.parseInt(in[1]);
            action = new ChallengerPhaseAction(startingPlayer);
        } catch (NumberFormatException e) {
            System.out.println(RED + "Error: it must be a numeric value, please try again." + RST);
            return null;
        }
        return action;
    }

    /**
     * Validates a "SET <x1> <y1> <x2> <y2>" worker placement message type.
     *
     * @param in the user input under array representation.
     * @return true if the input is valid and sent to the server, false otherwise.
     */
    public WorkerSetupMessage set(String[] in) {
        WorkerSetupMessage action;
        try {
            action = new WorkerSetupMessage(in);
            int x = Integer.parseInt(in[1]);
            int y = Integer.parseInt(in[2]);
            int w = Integer.parseInt(in[3]);
            int z = Integer.parseInt(in[4]);
            if (x < Constants.GRID_MIN_SIZE || x >= Constants.GRID_MAX_SIZE || y < Constants.GRID_MIN_SIZE || y >= Constants.GRID_MAX_SIZE || w < Constants.GRID_MIN_SIZE || w >= Constants.GRID_MAX_SIZE || z < Constants.GRID_MIN_SIZE || z >= Constants.GRID_MAX_SIZE) {
                System.err.println(ERR_NONEXISTENT_UNREACHABLE);
                return null;
            } else return action;
        } catch (NumberFormatException e) {
            System.out.println(RED + "Unknown input, please try again!" + RST);
            return null;
        }
    }

    /**
     * Handles the program quit statement, terminating the local service with exit code 0.
     */
    public void quit() {
        connection.send(new Disconnect());
        System.err.println("Disconnected from the server.");
        System.exit(0);
    }

    /**
     * Check if build action is possible
     *
     * @param turnPhase    int
     * @param x            int
     * @param y            int
     * @param activeWorker int
     * @return buildAction
     */
    public BuildAction build(int turnPhase, int x, int y, int activeWorker) {
        Couple w = findWorker(activeWorker, modelView.getColor());
        BuildAction build = new BuildAction(x, y);
        if (turnPhase == 2 || Constants.getBuildPhaseGods().contains(modelView.getGod().toUpperCase())) {
            return getBuildAction(x, y, w, build);
        } else {
            System.err.println(ERR_INCORRECT_ACTION);
            return null;
        }
    }


    public SelectBuildAction build(int turnPhase, int activeWorker) {
        if (activeWorker == 0) {
            System.err.println(ERR_WORKER_NOT_SELECTED);
            return null;
        } else if (turnPhase == 1 || Constants.getBuildPhaseGods().contains(modelView.getGod().toUpperCase())) {
            modelView.setBuildSelected(true);
            return new SelectBuildAction();
        } else {
            System.err.println(ERR_INCORRECT_ACTION);
            return null;
        }
    }

    public AtlasBuildAction atlasBuild(int turnPhase, int x, int y, int activeWorker) {
        if (modelView.getGod().equalsIgnoreCase("ATLAS")) {
            Couple w = findWorker(activeWorker, modelView.getColor());
            AtlasBuildAction build = new AtlasBuildAction(x, y, true);
                if (turnPhase == 2) {
                    return (AtlasBuildAction) getBuildAction(x, y, w, build);
                }
                    else{
                        System.err.println(ERR_INCORRECT_ACTION);
                        return null;
                    }
                }
             else{
                System.err.println("Current god isn't ATLAS, operation not permitted!");
                return null;
            }
        }

    /**
     * Check if move is possible
     *
     * @param turnPhase    int
     * @param x            int
     * @param y            int
     * @param activeWorker int
     * @return moveAction
     */
    public MoveAction move(int turnPhase, int x, int y, int activeWorker) {
        if (activeWorker == 0) {
            System.err.println(ERR_WORKER_NOT_SELECTED);
            return null;
        }
        Couple w = findWorker(activeWorker, modelView.getColor());
        MoveAction move = new MoveAction(x, y);
        if (turnPhase == 1 || Constants.getMovePhaseGods().contains(modelView.getGod().toUpperCase())) {
            if (isUnreachable(x, y, w)) {
                System.out.println(RED + ERR_NONEXISTENT_UNREACHABLE + RST);
                return null;
            } else {
                if (modelView.getBoard().getGrid()[x][y].getColor() != null) {
                    return canMoveToOccupiedCell(move);
                } else {
                    return canReachCell(x, y, w, move);
                }
            }
        } else {
            System.err.println(ERR_INCORRECT_ACTION);
            return null;
        }

    }

    private MoveAction canReachCell(int x, int y, Couple w, MoveAction move) {
        if (modelView.getBoard().getGrid()[x][y].isDome()) {
            System.out.println(RED + CELL_WITH_DOME + RST);
            return null;
        } else {
            assert w != null;
            if (modelView.getBoard().getGrid()[x][y].getLevel() - modelView.getBoard().getGrid()[w.getX()][w.getY()].getLevel() >= 2) {
                System.out.println(RED + "Trying to move up to unreachable level, operation not permitted!" + RST);
                return null;
            } else return move;
        }
    }

    private MoveAction canMoveToOccupiedCell(MoveAction move) {
        if (!Constants.getMoveToCellOccupiedGods().contains(modelView.getGod().toUpperCase())) {
            System.out.println(RED + ERR_CELL_OCCUPIED + RST);
            return null;
        } else return move;
    }

    private BuildAction getBuildAction(int x, int y, Couple w, BuildAction build) {
        if (isUnreachable(x, y, w)) {
            System.out.println(RED + ERR_NONEXISTENT_UNREACHABLE + RST);
            return null;
        } else {
            if (modelView.getBoard().getGrid()[x][y].getColor() != null) {
                System.out.println(RED + ERR_CELL_OCCUPIED + RST);
                return null;
            } else {
                if (modelView.getBoard().getGrid()[x][y].getLevel() == 4 || modelView.getBoard().getGrid()[x][y].isDome()) {
                    System.out.println(RED + CELL_WITH_DOME + RST);
                    return null;
                } else {
                    return build;
                }
            }
        }
    }


    private boolean isUnreachable(int x, int y, Couple w) {
        return x < Constants.GRID_MIN_SIZE || x >= Constants.GRID_MAX_SIZE || y < Constants.GRID_MIN_SIZE || y >= Constants.GRID_MAX_SIZE || x >= Objects.requireNonNull(w).getX() + 2 || x <= w.getX() - 2 || y >= w.getY() + 2 || y <= w.getY() - 2;
    }

    public SelectMoveAction move(int turnPhase, int activeWorker) {
        if (activeWorker == 0) {
            System.err.println(ERR_WORKER_NOT_SELECTED);
            return null;
        } else if (turnPhase == 0 || Constants.getMovePhaseGods().contains(modelView.getGod().toUpperCase())) {
            modelView.setMoveSelected(true);
            return new SelectMoveAction();
        } else {
            System.err.println(ERR_INCORRECT_ACTION);
            return null;
        }
    }

    private Couple findWorker(int activeWorker, String color) {
        Couple couple;
        for (int i = 0; i < Constants.GRID_MAX_SIZE; i++) {
            for (int j = 0; j < Constants.GRID_MAX_SIZE; j++) {
                if (modelView.getBoard().getGrid()[i][j].getWorkerNum() == activeWorker && modelView.getBoard().getGrid()[i][j].getColor().equals(color)) {
                    couple = new Couple(i, j);
                    return couple;
                }
            }
        }
        return null;
    }

    public StartTurnAction selectWorker(String[] in) {
        if(modelView.getTurnPhase()==0) {
            String var;
            if (Integer.parseInt(in[1]) == 1) {
                var = "worker1";
            } else if (Integer.parseInt(in[1]) == 2) {
                var = "worker2";
            } else {
                System.err.println("Non-existent worker, operation not permitted!");
                return null;
            }
            return new StartTurnAction(var);
        }
        System.err.println("You cannot change your worker after an action!");
        return null;
    }
}
