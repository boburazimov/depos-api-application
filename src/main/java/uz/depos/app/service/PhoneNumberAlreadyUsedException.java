package uz.depos.app.service;

public class PhoneNumberAlreadyUsedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PhoneNumberAlreadyUsedException() {
        super("Phone-number is already in use!");
    }
}
