package sales.service;

import sales.entity.UserEntity;

public interface JwtService {
    String generateToken(UserEntity user);

    String claimUsername(String jwt);

    Long extractId(String jwt);
}
