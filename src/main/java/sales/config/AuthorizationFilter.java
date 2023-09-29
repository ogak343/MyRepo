package sales.config;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import sales.exception.AuthenticationProblem;
import sales.service.JwtService;

import java.io.IOException;
import java.util.Objects;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static sales.exception.Error.NO_ACCESS;

@Component
public class AuthorizationFilter extends OncePerRequestFilter {

    private final ApplicationConfiguration applicationConfiguration;
    private final JwtService jwtService;

    public AuthorizationFilter(ApplicationConfiguration applicationConfiguration, JwtService jwtService) {
        this.applicationConfiguration = applicationConfiguration;
        this.jwtService = jwtService;
    }


    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain) throws ServletException, IOException {

        final String header = request.getHeader(AUTHORIZATION);
        if (header != null && !request.getRequestURI().startsWith("auth", 7)) {
            final String jwt = header.substring("Bearer ".length());
            final Long userId = Long.valueOf(request.getRequestURI().substring(13));
            final Long id = jwtService.extractId(jwt);

            if (!Objects.equals(id, userId)) {
                throw AuthenticationProblem.details(NO_ACCESS);
            }

            final String username = jwtService.claimUsername(jwt);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = applicationConfiguration.userDetailsService().loadUserByUsername(username);
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userDetails.getUsername(),
                        null,
                        userDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        }
        filterChain.doFilter(request, response);
    }
}
