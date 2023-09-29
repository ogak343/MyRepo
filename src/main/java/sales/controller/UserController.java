package sales.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Page;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;

import sales.dto.PageRequestDto;
import sales.dto.UserSpecificationRequest;
import sales.model.dto.UserDto;
import sales.service.UserService;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable("id") Long id, @RequestBody UserDto userDto) {
        return ResponseEntity.ok().body(userService.update(id, userDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(userService.get(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(userService.delete(id));
    }

    @GetMapping
    public ResponseEntity<Page<UserDto>> getBySpecification(@RequestBody UserSpecificationRequest request,
                                                            @RequestBody PageRequestDto pageRequestDto) {
        return ResponseEntity.ok().body(userService.getSpec(request, pageRequestDto));
    }

}

