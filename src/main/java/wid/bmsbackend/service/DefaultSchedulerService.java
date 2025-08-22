package wid.bmsbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wid.bmsbackend.repository.BatteryCellRepository;

@Service
@RequiredArgsConstructor
public class DefaultSchedulerService {
    private final BatteryCellRepository cellRepository;

    //    @Scheduled(fixedDelay = 1000
    public void communicationErrorCheck() {


    }
}