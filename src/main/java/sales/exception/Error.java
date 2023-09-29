package sales.exception;

import lombok.Getter;

@Getter
public enum Error {
    USER_TAKEN("USER TAKEN", "this username is not available"),
    PASSWORD_DOES_NOT_MATCH("PASSWORD MISMATCH", "username or password does not match"),
    USER_NOT_ENABLED("USER NOT ENABLED", "user is not confirmed yet!"),
    CODE_EXPIRED("CODE EXPIRED", "confirmation code expired"),
    USER_ALREADY_CONFIRMED("USER CONFIRMED", "you have already confirmed your identity"),
    INCORRECT_CODE("INVALID CODE", "your confirmation code was incorrect"),
    USER_NOT_FOUND("USER NOT FOUND", "user not found"),
    NO_ACCESS("WRONG CREDENTIALS", "you have no access to see details");

    private final String title;
    private final String detail;

    Error(String title, String detail) {
        this.title = title;
        this.detail = detail;
    }
}
