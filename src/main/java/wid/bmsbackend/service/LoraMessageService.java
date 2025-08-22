package wid.bmsbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wid.bmsbackend.dto.LoraMessageResponse;
import wid.bmsbackend.dto.SearchOptions;
import wid.bmsbackend.entity.LoraMessage;
import wid.bmsbackend.repository.LoraMessageRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class LoraMessageService {
    private final LoraMessageRepository loraMessageRepository;

    public Long saveLoraMessage(LoraMessage loraMessage) {
        LoraMessage message = loraMessageRepository.save(loraMessage);
        return message.getId();
    }

    @Transactional(readOnly = true)
    public Page<LoraMessageResponse> findAll(Pageable pageable, SearchOptions messageSearchOptions) {

        Page<LoraMessage> pages = loraMessageRepository.findAllWithSearchOptions(pageable, messageSearchOptions);
        return pages.map(LoraMessageResponse::of);
    }

}
