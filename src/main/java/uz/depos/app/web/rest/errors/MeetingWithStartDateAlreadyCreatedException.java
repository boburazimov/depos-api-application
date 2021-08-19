package uz.depos.app.web.rest.errors;

public class MeetingWithStartDateAlreadyCreatedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public MeetingWithStartDateAlreadyCreatedException() {
        super(
            ErrorConstants.MEETING_WITH_START_DATE_ALREADY_CREATED_TYPE,
            "Meeting with start date already created!",
            "meetingManagement",
            "meetingExists"
        );
    }
}
