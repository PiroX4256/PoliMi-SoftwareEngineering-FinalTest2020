package it.polimi.ingsw.exceptions;

public class DuplicateNicknameException extends Exception {
    @Override
    public String getMessage() {
        return "Error: this nickname has already been chosen!";
    }
}
