package ru.llm.pivocore.configuration.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.llm.pivocore.configuration.security.filter.PiVoCoreAuthenticationFilter;
import ru.llm.pivocore.configuration.security.filter.PiVoJwtAuthorizationFilter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static ru.llm.pivocore.enums.UserRoles.APP_USER;
import static ru.llm.pivocore.enums.UserRoles.RESTAURANT_USER;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    private final BCryptPasswordEncoder passwordEncoder;

    private final SecretsConfig secretsConfig;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        var authenticationFilter = new PiVoCoreAuthenticationFilter(authenticationManagerBean(), secretsConfig.getJwtSecret());
        var authorizationFilter = new PiVoJwtAuthorizationFilter(secretsConfig.getJwtSecret());
        authenticationFilter.setFilterProcessesUrl("/api/app_user/login");
        http = http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().antMatchers(GET, "/api/app_user/**").hasAuthority(APP_USER.name());
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(authenticationFilter);
        http.addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

}
