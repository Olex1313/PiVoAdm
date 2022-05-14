package ru.llm.pivocore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.llm.pivocore.model.entity.RestaurantUserEntity;

@Repository
public interface RestaurantUsersRepository extends JpaRepository<RestaurantUserEntity, Long> {
    RestaurantUserEntity findByUsername(String userName);
}
