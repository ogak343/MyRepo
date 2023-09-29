package sales.service;

import sales.dto.AuthenticationResponse;
import sales.model.dto.UserCreationReqDto;

public interface AuthenticationService {
    String confirm(String email, Integer code);

    String resend(String email);

    String logout();

    String login(String username, String password);

    boolean checkValid(String username);

    AuthenticationResponse create(UserCreationReqDto request);
}
