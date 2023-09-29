package sales.dto;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record AuthenticationResponse(HttpStatus status, String info) {
}
