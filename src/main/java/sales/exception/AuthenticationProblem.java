package sales.exception;

import org.springframework.http.ProblemDetail;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class AuthenticationProblem extends BaseExceptionProblem {
    public AuthenticationProblem(ProblemDetail detail) {
        super(detail);
    }

    public static BaseExceptionProblem details(Error error) {
        return BaseExceptionProblem.details(error, UNAUTHORIZED);
    }

}
