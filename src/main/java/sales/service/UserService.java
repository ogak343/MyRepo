package sales.service;

import sales.model.dto.UserDto;

public interface UserService {

    UserDto update(Long id, UserDto userDto);

    UserDto get(Long id);

    String delete(Long id);

}
