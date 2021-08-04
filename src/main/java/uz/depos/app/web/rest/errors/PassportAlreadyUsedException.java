package uz.depos.app.web.rest.errors;

public class PassportAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public PassportAlreadyUsedException() {
        super(ErrorConstants.PASSPORT_ALREADY_USED_TYPE, "Passport already used!", "userManagement", "userexists");
    }
}
