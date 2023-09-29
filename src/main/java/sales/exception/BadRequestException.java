package sales.exception;

import org.springframework.http.ProblemDetail;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class BadRequestException extends BaseExceptionProblem {

    public BadRequestException(ProblemDetail problemDetail) {
        super(problemDetail);
    }

    public static BaseExceptionProblem details(Error error) {
        return BaseExceptionProblem.details(error, BAD_REQUEST);
    }
}
