package uz.depos.app.service;

public class AgendaSubjectAlreadyUsedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AgendaSubjectAlreadyUsedException() {
        super("Subject is already have in this Meeting!");
    }
}
