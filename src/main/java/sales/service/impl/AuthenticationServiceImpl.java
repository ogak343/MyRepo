package sales.service.impl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sales.dto.AuthenticationResponse;
import sales.entity.UserEntity;
import sales.exception.AuthenticationProblem;
import sales.exception.BadRequestException;
import sales.exception.DataSourceProblem;
import sales.exception.Error;
import sales.model.constants.Role;
import sales.model.dto.UserCreationReqDto;
import sales.entity.ConfirmationCodeEntity;
import sales.repo.ConfirmationCodeRepository;
import sales.repo.UserRepository;
import sales.service.AuthenticationService;
import sales.service.JwtService;
import sales.service.MailService;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import static org.springframework.http.HttpStatus.CREATED;
import static sales.exception.Error.USER_NOT_FOUND;
import static sales.exception.Error.USER_ALREADY_CONFIRMED;
import static sales.exception.Error.USER_NOT_ENABLED;
import static sales.exception.Error.PASSWORD_DOES_NOT_MATCH;


@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final ConfirmationCodeRepository codeRepository;
    private final BCryptPasswordEncoder encoder;
    private final MailService mailService;
    private final JwtService jwtService;

    public AuthenticationServiceImpl(UserRepository userRepository,
                                     ConfirmationCodeRepository codeRepository,
                                     BCryptPasswordEncoder encoder,
                                     MailService mailService, JwtService jwtService) {
        this.userRepository = userRepository;
        this.codeRepository = codeRepository;
        this.encoder = encoder;
        this.mailService = mailService;
        this.jwtService = jwtService;
    }

    @Override
    public String confirm(String username, Integer code) {

        ConfirmationCodeEntity confirmationCode = codeRepository.findLastByUsername(username)
                .orElseThrow(() -> DataSourceProblem.details(USER_NOT_FOUND));
        if (confirmationCode.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw BadRequestException.details(Error.CODE_EXPIRED);
        }
        boolean enabled = userRepository.checkStatusByEmail(username);
        if (confirmationCode.getConfirmedAt() != null && enabled) {
            throw BadRequestException.details(Error.USER_ALREADY_CONFIRMED);
        }
        if (!Objects.equals(confirmationCode.getCode(), code)) {
            throw BadRequestException.details(Error.INCORRECT_CODE);
        }
        confirmationCode.setConfirmedAt(LocalDateTime.now());
        userRepository.enableUser(username);
        codeRepository.save(confirmationCode);
        return "User has been confirmed";
    }

    @Override
    public String resend(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> DataSourceProblem.details(USER_NOT_FOUND));

        if (user.isEnabled()) {
            throw BadRequestException.details(USER_ALREADY_CONFIRMED);
        }
        int code = new Random().nextInt(100000, 999999);
        ConfirmationCodeEntity confirmationCode = new ConfirmationCodeEntity(
                code,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(5),
                user.getId());
        codeRepository.save(confirmationCode);
        mailService.sendCode(code, user.getName(), user.getUsername());
        return String.format("Verification code was resent to %s", username);
    }

    @Override
    public String logout() {
        return null;
    }

    @Override
    public String login(String username, String password) {
        Optional<UserEntity> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw DataSourceProblem.details(USER_NOT_FOUND);
        }
        UserEntity user = userOptional.get();

        if (!encoder.matches(password, user.getPassword())) {
            throw AuthenticationProblem.details(PASSWORD_DOES_NOT_MATCH);
        }

        if (!user.isEnabled()) {
            throw AuthenticationProblem.details(USER_NOT_ENABLED);
        }

        return jwtService.generateToken(user);
    }

    @Override
    public boolean checkValid(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AuthenticationResponse create(UserCreationReqDto request) {

        boolean exists = userRepository.existsByUsername(request.getUsername());
        if (exists) {
            throw DataSourceProblem.details(Error.USER_TAKEN);
        }
        UserEntity user = new UserEntity(
                request.getName(),
                request.getSurname(),
                request.getUsername(),
                encoder.encode(request.getPassword()),
                false,
                Role.USER);
        UserEntity savedEntity = userRepository.save(user);
        int code = new Random().nextInt(100000, 999999);
        ConfirmationCodeEntity confirmationCode = new ConfirmationCodeEntity(
                code,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(5),
                savedEntity.getId());
        codeRepository.save(confirmationCode);

        mailService.sendCode(code, request.getName(), request.getUsername());
        return AuthenticationResponse.builder().status(CREATED).info("").build();
    }
}
