package ru.llm.pivocore.configuration.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.llm.pivocore.configuration.security.PiVoCoreAuthenticationManager;
import ru.llm.pivocore.model.entity.AppUserEntity;
import ru.llm.pivocore.model.entity.RestaurantUserEntity;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
public class PiVoCoreAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final PiVoCoreAuthenticationManager authenticationManager;

    private final String secret;

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws AuthenticationException {
        var username = request.getHeader("username");
        var password = request.getHeader("password");
        log.info("Authentication attempt: username={}, password={}", username, password);
        var authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        if (request.getServletPath().endsWith("restaurant_user/login")) {
            authenticationToken.setDetails(RestaurantUserEntity.class);
        }
        if (request.getServletPath().endsWith("app_user/login")) {
            authenticationToken.setDetails(AppUserEntity.class);
        }
        var authentication = authenticationManager.authenticate(authenticationToken);
        if (authentication == null) throw new RuntimeException("Auth exception");
        return authentication;
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authentication
    ) throws IOException {
        var springSecUser = (User) authentication.getPrincipal();
        var algorithm = Algorithm.HMAC256(secret.getBytes(StandardCharsets.UTF_8));
        var accessToken = JWT.create()
                .withSubject(springSecUser.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .withClaim("roles", springSecUser.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList())
                )
                .sign(algorithm);
        var refreshToken = JWT.create()
                .withSubject(springSecUser.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .sign(algorithm);
        var tokensMap = Map.of("access_token", accessToken, "refresh_token", refreshToken);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokensMap);
    }

}
