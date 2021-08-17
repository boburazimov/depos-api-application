package uz.depos.app.web.rest.errors.company;

import uz.depos.app.web.rest.errors.BadRequestAlertException;
import uz.depos.app.web.rest.errors.ErrorConstants;

public class CompanyNameAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public CompanyNameAlreadyUsedException() {
        super(ErrorConstants.COMPANY_NAME_ALREADY_USED_TYPE, "Company name already used!", "companyManagement", "nameExists");
    }
}
