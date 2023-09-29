package sales.exception;

import org.springframework.http.ProblemDetail;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class DataSourceProblem extends BaseExceptionProblem {

    public DataSourceProblem(ProblemDetail detail) {
        super(detail);
    }
    public static BaseExceptionProblem details(Error error) {
        return BaseExceptionProblem.details(error, NOT_FOUND);
    }

}