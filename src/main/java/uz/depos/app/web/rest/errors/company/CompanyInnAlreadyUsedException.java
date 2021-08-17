package uz.depos.app.web.rest.errors.company;

import uz.depos.app.web.rest.errors.BadRequestAlertException;
import uz.depos.app.web.rest.errors.ErrorConstants;

public class CompanyInnAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public CompanyInnAlreadyUsedException() {
        super(ErrorConstants.COMPANY_INN_ALREADY_USED_TYPE, "Inn is already in use!", "companyManagement", "innExists");
    }
}
