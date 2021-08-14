package uz.depos.app.service;

public class PinflAlreadyUsedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PinflAlreadyUsedException() {
        super("Pinfl is already in use!");
    }
}
