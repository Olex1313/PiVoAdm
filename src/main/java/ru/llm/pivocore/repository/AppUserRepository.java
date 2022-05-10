package ru.llm.pivocore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import ru.llm.pivocore.model.entity.AppUserEntity;

public interface AppUserRepository extends JpaRepository<AppUserEntity, Long> {

    AppUserEntity findByUsername(String username);

}
