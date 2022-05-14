package ru.llm.pivocore.configuration.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import ru.llm.pivocore.enums.UserRoles;
import ru.llm.pivocore.model.entity.AppUserEntity;
import ru.llm.pivocore.model.entity.RestaurantUserEntity;
import ru.llm.pivocore.service.AppUserService;
import ru.llm.pivocore.service.RestaurantUserService;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PiVoCoreAuthenticationManager {

    private final BCryptPasswordEncoder passwordEncoder;

    private final AppUserService appUserService;

    private final RestaurantUserService restaurantUserService;

    public Authentication authenticate(
            UsernamePasswordAuthenticationToken authentication
    ) throws AuthenticationException {
        log.info("Managing authentication {}", authentication.toString());
        if (authentication.getDetails() == AppUserEntity.class) {
            return authentication(appUserService, authentication, UserRoles.APP_USER);
        }
        if (authentication.getDetails() == RestaurantUserEntity.class) {
            return authentication(restaurantUserService, authentication, UserRoles.RESTAURANT_USER);
        }
        throw new RuntimeException("Authentication exception");
    }

    private Authentication authentication(
            UserDetailsService userDetailsService,
            UsernamePasswordAuthenticationToken authentication,
            UserRoles role
    ) {
        val user = userDetailsService.loadUserByUsername((String) authentication.getPrincipal());
        if (passwordEncoder.matches(authentication.getCredentials().toString(), user.getPassword())) {
            return new UsernamePasswordAuthenticationToken(
                    new User((String) authentication.getPrincipal(), (String) authentication.getCredentials(), List.of(new SimpleGrantedAuthority(role.name()))),
                    authentication.getCredentials(),
                    List.of(new SimpleGrantedAuthority(role.name()))
            );
        } else throw new RuntimeException("message");
    }

}
