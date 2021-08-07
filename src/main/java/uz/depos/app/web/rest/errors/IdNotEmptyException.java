package uz.depos.app.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class IdNotEmptyException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public IdNotEmptyException() {
        super(ErrorConstants.ID_NOT_EMPTY_TYPE, "ID must be Null (Empty) for creator user", Status.BAD_REQUEST);
    }
}
