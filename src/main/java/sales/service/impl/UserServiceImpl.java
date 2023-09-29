package sales.service.impl;

import jakarta.persistence.criteria.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sales.dto.PageRequestDto;
import sales.dto.UserSpecificationRequest;
import sales.entity.DepartmentEntity;
import sales.exception.DataSourceProblem;
import sales.mapper.UserMapper;
import sales.model.dto.UserDto;
import sales.entity.UserEntity;
import sales.repo.ConfirmationCodeRepository;
import sales.repo.UserRepository;
import sales.service.UserService;
import sales.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static sales.exception.Error.USER_NOT_FOUND;


@Lazy
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ConfirmationCodeRepository codeRepository;
    private final UserMapper userMapper;


    public UserServiceImpl(UserRepository userRepository,
                           ConfirmationCodeRepository codeRepository, UserMapper userMapper) {
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
        return userRepository.findById(id)
                .orElseThrow(() -> DataSourceProblem.details(USER_NOT_FOUND));
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

    @Override
    public Page<UserDto> getSpec(UserSpecificationRequest request, PageRequestDto pageRequestDto) {

        return userMapper.toPage(userRepository.findAll(JpaSpecDetails(Utils.getSpecs(request)),
                getPage(pageRequestDto)));
    }

    private Pageable getPage(PageRequestDto dto) {
        return PageRequest.of(dto.getPage(), dto.getSize());
    }

    private Specification<UserEntity> JpaSpecDetails(Map<String, Object> specs) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            final String joinKey = "departmentId";
            if (specs.containsKey(joinKey)) {
                Join<UserEntity, DepartmentEntity> join = root.join("userDepartment");
                criteriaBuilder.equal(join.get("id"), specs.remove(joinKey));
            }
            specs.forEach((key, val) -> Optional.ofNullable(val)
                    .ifPresent(value -> addPredicate(predicates, value, key, root, criteriaBuilder)));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
    private <T> void addPredicate(
            List<Predicate> predicateList,
            T item,
            String pathString,
            Root<UserEntity> root,
            CriteriaBuilder cb
    ) {
        Path<Object> path = root.get(pathString);
        predicateList.add(cb.equal(path, item));
    }

    private boolean validateUser(Long id) {
        return userRepository.existsById(id);
    }
}
