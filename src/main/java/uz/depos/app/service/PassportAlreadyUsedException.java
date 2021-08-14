package uz.depos.app.service;

public class PassportAlreadyUsedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PassportAlreadyUsedException() {
        super("Passport is already in use!");
    }
}
