package sales.service.impl;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sales.exception.DataSourceProblem;
import sales.mapper.UserMapper;
import sales.model.dto.UserDto;
import sales.entity.UserEntity;
import sales.repo.ConfirmationCodeRepository;
import sales.repo.UserRepository;
import sales.service.UserService;

import static sales.exception.Error.USER_NOT_FOUND;


@Lazy
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ConfirmationCodeRepository codeRepository;
    private final UserMapper userMapper;


    public UserServiceImpl(UserRepository userRepository,
                           ConfirmationCodeRepository codeRepository,
                           UserMapper userMapper) {
        this.userRepository = userRepository;
        this.codeRepository = codeRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto update(Long id, UserDto userDto) {

        var user = getUserById(id);

        userMapper.update(user, userDto);

        return userMapper.toDto(user);
    }

    @Override
    public UserDto get(Long id) {
        return userMapper.toDto(getUserById(id));
    }

    private UserEntity getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> DataSourceProblem.details(USER_NOT_FOUND));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String delete(Long id) {
        if (validateUser(id)) {
            codeRepository.deleteByUserId(id);
            userRepository.deleteById(id);
            return String.format("User with Id %s deleted", id);
        } else {
            throw DataSourceProblem.details(USER_NOT_FOUND);
        }
    }

    private boolean validateUser(Long id) {
        return userRepository.existsById(id);
    }
}
