package ru.llm.pivocore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.llm.pivocore.model.entity.AppUserEntity;

@Repository
public interface AppUserRepository extends JpaRepository<AppUserEntity, Long> {

    AppUserEntity findByUsername(String username);
}
