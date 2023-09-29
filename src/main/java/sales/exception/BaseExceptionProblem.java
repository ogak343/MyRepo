package sales.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class BaseExceptionProblem extends ErrorResponseException {

    public BaseExceptionProblem(ProblemDetail detail) {
        super(HttpStatus.valueOf(detail.getStatus()), detail, null);
    }

    public static BaseExceptionProblem details(Error error, HttpStatus status) {
        return new BaseExceptionProblem(constructBody(error, status));
    }

    private static ProblemDetail constructBody(Error error, HttpStatus status) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setTitle(error.getTitle());
        problemDetail.setDetail(error.getDetail());
        return problemDetail;
    }
}
