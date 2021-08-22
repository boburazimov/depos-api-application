package uz.depos.app.web.rest.errors;

public class AgendaSubjectAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public AgendaSubjectAlreadyUsedException() {
        super(ErrorConstants.SUBJECT_ALREADY_USED_TYPE, "Subject is already have in this Meeting!", "subjectManagement", "subjectExists");
    }
}
