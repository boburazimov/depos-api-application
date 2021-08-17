package uz.depos.app.web.rest.errors.company;

import uz.depos.app.web.rest.errors.BadRequestAlertException;
import uz.depos.app.web.rest.errors.ErrorConstants;

public class CompanyEmailAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public CompanyEmailAlreadyUsedException() {
        super(ErrorConstants.COMPANY_EMAIL_ALREADY_USED_TYPE, "Email is already in use!", "companyManagement", "emailExists");
    }
}
