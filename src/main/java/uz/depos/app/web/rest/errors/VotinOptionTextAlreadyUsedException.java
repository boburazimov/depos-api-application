package uz.depos.app.web.rest.errors;

public class VotinOptionTextAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public VotinOptionTextAlreadyUsedException() {
        super(
            ErrorConstants.VOTING_TEXT_ALREADY_USED_TYPE,
            "Voting text is already have in this Agenda!",
            "votingTextManagement",
            "votingTextExists"
        );
    }
}
