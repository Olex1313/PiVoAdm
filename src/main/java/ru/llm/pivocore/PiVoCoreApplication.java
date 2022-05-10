package ru.llm.pivocore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
public class PiVoCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(PiVoCoreApplication.class, args);
    }

}
