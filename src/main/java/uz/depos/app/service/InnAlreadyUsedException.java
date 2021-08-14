package uz.depos.app.service;

public class InnAlreadyUsedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InnAlreadyUsedException() {
        super("Inn is already in use!");
    }
}
