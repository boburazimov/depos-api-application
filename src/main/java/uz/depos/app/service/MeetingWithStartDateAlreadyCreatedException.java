package uz.depos.app.service;

public class MeetingWithStartDateAlreadyCreatedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MeetingWithStartDateAlreadyCreatedException() {
        super("Meeting with start date already created!");
    }
}
