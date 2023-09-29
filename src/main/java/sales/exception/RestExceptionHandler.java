package sales.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;


@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestExceptionHandler extends RuntimeException {

    @ExceptionHandler(Exception.class)
    ProblemDetail unhandledException(Exception e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(INTERNAL_SERVER_ERROR, "Unhandled Exception");

        problemDetail.setTitle("Unhandled Exception");
        problemDetail.setDetail(e.getMessage());
        return problemDetail;
    }

    @ExceptionHandler(BaseExceptionProblem.class)
    ProblemDetail exceptionHandling(BaseExceptionProblem e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(e.getStatusCode());

        problemDetail.setTitle(e.getTitleMessageCode());
        problemDetail.setDetail(e.getMessage());
        return problemDetail;
    }

}
