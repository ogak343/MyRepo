package sales.mapper;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import sales.entity.UserEntity;
import sales.model.dto.UserDto;


@Primary
@Component
@Lazy
public class UserMapperImpl implements UserMapper {

    private final UserMapper mapper;

    public UserMapperImpl(UserMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public UserDto toDto(UserEntity user) {
        return mapper.toDto(user);
    }

    @Override
    public void update(UserEntity user, UserDto userDto) {
        mapper.update(user, userDto);
    }

}
