package uz.depos.app.web.rest.errors;

public class PhoneNumberAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public PhoneNumberAlreadyUsedException() {
        super(ErrorConstants.PHONENUMBER_ALREADY_USED_TYPE, "Phone-number is already in use!", "userManagement", "phonenumberexists");
    }
}
