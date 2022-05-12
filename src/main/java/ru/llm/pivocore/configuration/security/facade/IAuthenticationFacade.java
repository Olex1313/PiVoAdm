package ru.llm.pivocore.configuration.security.facade;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public interface IAuthenticationFacade {
    Authentication getAuthentication();
}
