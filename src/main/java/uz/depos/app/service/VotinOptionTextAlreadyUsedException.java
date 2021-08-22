package uz.depos.app.service;

public class VotinOptionTextAlreadyUsedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public VotinOptionTextAlreadyUsedException() {
        super("Voting text is already have in this Agenda!");
    }
}
