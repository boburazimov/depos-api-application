package uz.depos.app.web.rest.errors;

public class PinflAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public PinflAlreadyUsedException() {
        super(ErrorConstants.LOGIN_ALREADY_USED_TYPE, "Pinfl already used!", "userManagement", "userexists");
    }
}
