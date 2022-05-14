package ru.llm.pivocore.configuration.security;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import ru.llm.pivocore.configuration.security.filter.PiVoCoreAuthenticationFilter;
import ru.llm.pivocore.configuration.security.filter.PiVoJwtAuthorizationFilter;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static ru.llm.pivocore.enums.UserRoles.APP_USER;
import static ru.llm.pivocore.enums.UserRoles.RESTAURANT_USER;

@RequiredArgsConstructor
@EnableWebSecurity
class PiVoCoreSecurityConfig extends WebSecurityConfigurerAdapter {

    private final SecretsConfig secretsConfig;

    private final PiVoCoreAuthenticationManager piVoCoreAuthenticationManager;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        var appUserFilter = new PiVoCoreAuthenticationFilter(piVoCoreAuthenticationManager, secretsConfig.getJwtSecret());
        var authorizationFilter = new PiVoJwtAuthorizationFilter(secretsConfig.getJwtSecret());
        appUserFilter.setRequiresAuthenticationRequestMatcher(request -> request.getServletPath().endsWith("login"));
        http = http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().antMatchers("/api/restaurant_user/**").hasAuthority(RESTAURANT_USER.name());
        http.authorizeRequests().antMatchers(GET, "/api/app_user/**").hasAuthority(APP_USER.name());
        http.authorizeRequests().requestMatchers(request -> request.getServletPath().endsWith("/register")).permitAll();
        http.addFilter(appUserFilter);
        http.addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
