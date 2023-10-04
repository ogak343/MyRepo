package sales.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import sales.entity.UserEntity;
import sales.model.dto.UserDto;


@Mapper(
        unmappedTargetPolicy = ReportingPolicy.WARN,
        implementationName = "UserMapperImplementation",
        componentModel = "spring")
public interface UserMapper {

    @Named("toDto")
    UserDto toDto(UserEntity user);

    void update(@MappingTarget UserEntity user, UserDto userDto);
}
