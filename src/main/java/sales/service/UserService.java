package sales.service;

import org.springframework.data.domain.Page;
import sales.dto.PageRequestDto;
import sales.dto.UserSpecificationRequest;
import sales.model.dto.UserDto;

public interface UserService {

    UserDto update(Long id, UserDto userDto);

    UserDto get(Long id);

    String delete(Long id);

    Page<UserDto> getSpec(UserSpecificationRequest request, PageRequestDto pageable);
}
