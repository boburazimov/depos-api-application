package uz.depos.app.web.rest.errors;

import java.net.URI;

public final class ErrorConstants {

    public static final String ERR_CONCURRENCY_FAILURE = "error.concurrencyFailure";
    public static final String ERR_VALIDATION = "error.validation";
    public static final String PROBLEM_BASE_URL = "https://www.depos.uz/problem";
    public static final URI DEFAULT_TYPE = URI.create(PROBLEM_BASE_URL + "/problem-with-message");
    public static final URI CONSTRAINT_VIOLATION_TYPE = URI.create(PROBLEM_BASE_URL + "/constraint-violation");
    public static final URI INVALID_PASSWORD_TYPE = URI.create(PROBLEM_BASE_URL + "/invalid-password");
    public static final URI EMAIL_ALREADY_USED_TYPE = URI.create(PROBLEM_BASE_URL + "/email-already-used");
    public static final URI LOGIN_ALREADY_USED_TYPE = URI.create(PROBLEM_BASE_URL + "/login-already-used");
    public static final URI PINFL_ALREADY_USED_TYPE = URI.create(PROBLEM_BASE_URL + "/pinfl-already-used");
    public static final URI INN_ALREADY_USED_TYPE = URI.create(PROBLEM_BASE_URL + "/inn-already-used");
    public static final URI PHONENUMBER_ALREADY_USED_TYPE = URI.create(PROBLEM_BASE_URL + "/phonenumber-already-used");
    public static final URI PASSPORT_ALREADY_USED_TYPE = URI.create(PROBLEM_BASE_URL + "/passport-already-used");

    public static final URI COMPANY_EMAIL_ALREADY_USED_TYPE = URI.create(PROBLEM_BASE_URL + "/company-email-already-used");
    public static final URI COMPANY_INN_ALREADY_USED_TYPE = URI.create(PROBLEM_BASE_URL + "/company-inn-already-used");
    public static final URI COMPANY_NAME_ALREADY_USED_TYPE = URI.create(PROBLEM_BASE_URL + "/company-name-already-used");

    public static final URI MEETING_WITH_START_DATE_ALREADY_CREATED_TYPE = URI.create(
        PROBLEM_BASE_URL + "/meeting-with-start-date-already-created"
    );

    public static final URI SUBJECT_ALREADY_USED_TYPE = URI.create(PROBLEM_BASE_URL + "/subject-in-meeting-already-created");

    public static final URI VOTING_TEXT_ALREADY_USED_TYPE = URI.create(PROBLEM_BASE_URL + "/voting-text-in-agenda-already-created");

    private ErrorConstants() {}
}
