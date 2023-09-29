package sales.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import sales.entity.UserEntity;
import sales.model.dto.UserDto;


@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        implementationName = "UserMapperImplementation",
        componentModel = "spring")
public interface UserMapper {

    UserDto toDto(UserEntity user);

    void update(@MappingTarget UserEntity user, UserDto userDto);

    Page<UserDto> toPage(Page<UserEntity> all);
}
