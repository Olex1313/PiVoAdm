package ru.llm.pivocore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.llm.pivocore.model.entity.RestaurantTableEntity;

@Repository
public interface TableRepository extends JpaRepository<RestaurantTableEntity, Long> {
}
