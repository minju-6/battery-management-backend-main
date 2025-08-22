package wid.bmsbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import wid.bmsbackend.dto.SearchOptions;
import wid.bmsbackend.entity.LoraMessage;

public interface CustomLoraMessageRepository {
    Page<LoraMessage> findAllWithSearchOptions(Pageable pageable, SearchOptions messageSearchOptions);
}
