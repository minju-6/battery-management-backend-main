package wid.bmsbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import wid.bmsbackend.entity.LoraMessage;

import java.time.LocalDateTime;

public interface LoraMessageRepository extends JpaRepository<LoraMessage, Long>, CustomLoraMessageRepository {
    Page<LoraMessage> findAllByCreatedDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
