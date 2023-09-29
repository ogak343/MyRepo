package sales.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

import sales.dto.AuthenticationResponse;
import sales.model.dto.UserCreationReqDto;
import sales.service.AuthenticationService;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/create")
    public ResponseEntity<AuthenticationResponse> create(@RequestBody UserCreationReqDto request) {
        return ResponseEntity.status(CREATED).body(authenticationService.create(request));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam("username") String username,
                                        @RequestParam("password") String password) {
        return ResponseEntity.ok().body(authenticationService.login(username, password));
    }

    @PostMapping("/confirm")
    public ResponseEntity<String> confirm(@RequestParam("email") String email, @RequestParam("code") Integer code) {
        return ResponseEntity.ok().body(authenticationService.confirm(email, code));
    }

    @PatchMapping("/resend")
    public ResponseEntity<String> resend(@RequestParam("email") String email) {
        return ResponseEntity.ok().body(authenticationService.resend(email));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok().body(authenticationService.logout());
    }

    @PostMapping("/check")
    public ResponseEntity<Boolean> check(@RequestParam("username") String username) {
        return ResponseEntity.ok().body(authenticationService.checkValid(username));
    }
}
