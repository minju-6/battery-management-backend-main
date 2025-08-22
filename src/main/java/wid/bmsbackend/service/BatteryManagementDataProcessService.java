package wid.bmsbackend.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wid.bmsbackend.dto.DecisionStatus;
import wid.bmsbackend.dto.SensingValue;
import wid.bmsbackend.entity.BatteryCell;
import wid.bmsbackend.entity.BatteryModel;
import wid.bmsbackend.entity.DeviceMessage;
import wid.bmsbackend.exception.InvalidProtocolFormatException;
import wid.bmsbackend.repository.DeviceMessageRepository;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BatteryManagementDataProcessService {
    private final ProtocolDecoder protocolDecoder;
    private final DeviceMessageService deviceMessageService;
    private final BatteryCellService batteryCellService;

    public void processPayload(String eui, long msgLogId, String payload, BatteryModel batteryModel) {
        try {
            DeviceMessage deviceMessage = protocolDecoder.decode(payload);
            deviceMessage.setEui(eui);
            deviceMessage.setMsgLogId(msgLogId);

            DecisionStatus decisionStatus = batteryModel.getDecisionStatusFromSensingValues(deviceMessage.getTemp(), deviceMessage.getVoltage(), deviceMessage.getCurrent(), deviceMessage.getImpedance());
            deviceMessage.setStatus(decisionStatus);
            log.info("Device message: {}", deviceMessage);

            deviceMessageService.save(deviceMessage);
            log.debug("Device message saved: {}", deviceMessage);
            
            BatteryCell batteryCell = batteryCellService.findByEui(eui);
            batteryCell.updateSensorData(
                    deviceMessage.getTemp(),
                    deviceMessage.getVoltage(),
                    deviceMessage.getImpedance(),
                    deviceMessage.getCurrent());

        } catch (EntityNotFoundException e) {
            log.error("Error while finding device: {}", e.getMessage());
            throw new InvalidProtocolFormatException("Device not found", e);
        } catch (Exception e) {
            log.error("Error while processing payload: {}", e.getMessage());
            throw new InvalidProtocolFormatException("Invalid device payload format", e);
        }
    }
}
