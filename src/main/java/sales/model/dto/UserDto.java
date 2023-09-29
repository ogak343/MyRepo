package sales.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sales.model.constants.Role;
import sales.entity.DepartmentEntity;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String surname;
    private String username;
    private Role role;
    private Set<DepartmentEntity> department;
}
