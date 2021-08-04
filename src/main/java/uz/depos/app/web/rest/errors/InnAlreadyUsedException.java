package uz.depos.app.web.rest.errors;

public class InnAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public InnAlreadyUsedException() {
        super(ErrorConstants.INN_ALREADY_USED_TYPE, "Inn is already in use!", "userManagement", "innexists");
    }
}
