package ru.llm.pivocore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.llm.pivocore.model.entity.RestaurantEntity;

public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {

    RestaurantEntity findByName(String name);

}
