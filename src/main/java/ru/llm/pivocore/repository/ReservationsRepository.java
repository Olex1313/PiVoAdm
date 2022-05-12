package ru.llm.pivocore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.llm.pivocore.model.entity.ReservationEntity;

@Repository
public interface ReservationsRepository extends JpaRepository<ReservationEntity, Long> {
}
