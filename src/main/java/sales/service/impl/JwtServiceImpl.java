package sales.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sales.entity.UserEntity;
import sales.service.JwtService;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.access}")
    private Integer access;
    @Value("${jwt.secret-key}")
    private String secret;

    @Override
    public String generateToken(UserEntity user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setAudience(user.getId().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + access * 1000 * 60))
                .signWith(signInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String claimUsername(String jwt) {
        return extractClaims(jwt, Claims::getSubject);
    }

    @Override
    public Long extractId(String jwt) {
        return Long.valueOf(extractClaims(jwt, Claims::getAudience));
    }

    private <T> T extractClaims(String jwt, Function<Claims, T> claimResolver) {
        return claimResolver.apply(Jwts.parserBuilder()
                .setSigningKey(signInKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody());
    }

    private Key signInKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }
}
