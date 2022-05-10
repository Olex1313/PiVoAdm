package ru.llm.pivocore.configuration.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
public class PiVoJwtAuthorizationFilter extends OncePerRequestFilter {

    private final String secret;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        logger.info("Authorization filter invocation");
        if (request.getServletPath().equals("/api/login")) {
            filterChain.doFilter(request, response);
            return;
        }
        var authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            logger.info("Checking token");
            try {
                var token = authorizationHeader.substring("Bearer ".length());
                var algorithm = Algorithm.HMAC256(secret.getBytes(StandardCharsets.UTF_8));
                var verifier = JWT.require(algorithm).build();
                var decodedJWT = verifier.verify(token);
                var username = decodedJWT.getSubject();
                var roles = decodedJWT.getClaim("roles").asList(String.class);
                var authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
                var authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                logger.info("Authorization success");
            } catch (JWTVerificationException e) {
                log.error("Error logging in: {}", e.getMessage());
                response.setHeader("error", e.getMessage());
                response.setStatus(SC_FORBIDDEN);
                response.setContentType(APPLICATION_JSON_VALUE);
                return;
            } catch (Exception e) {
                log.error("Unexpected error in logging in: {}", e.getMessage());
                response.setHeader("error", e.getMessage());
                response.setStatus(SC_INTERNAL_SERVER_ERROR);
                var tokensMap = Map.of("error_message", e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokensMap);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
