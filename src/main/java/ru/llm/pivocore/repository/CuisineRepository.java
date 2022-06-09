package ru.llm.pivocore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.llm.pivocore.model.entity.CuisineEntity;

@Repository
public interface CuisineRepository extends JpaRepository<CuisineEntity, Long> {

}
