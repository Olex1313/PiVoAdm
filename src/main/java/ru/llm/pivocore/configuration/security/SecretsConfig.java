package ru.llm.pivocore.configuration.security;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class SecretsConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;

}
