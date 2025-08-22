package wid.bmsbackend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import wid.bmsbackend.config.MqttConfig;
import wid.bmsbackend.dto.DecisionStatus;
import wid.bmsbackend.dto.MqttMessageSendDto;
import wid.bmsbackend.entity.*;
import wid.bmsbackend.repository.BatteryModelRepository;
import wid.bmsbackend.repository.DeviceCommandRepository;

import java.util.Base64;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoraMessageProcessService implements MessageProcessService {
    private final LoraMessageService loraMessageService;
    private final BatteryManagementDataProcessService batteryManagementDataProcessService;
    private final LoraMessageConverter loraMessageConverter;
    private final ObjectMapper jacksonObjectMapper;
    private final DeviceCommandService deviceCommandService;
    private final MqttConfig.MyGateWay myGateWay;
    private final DeviceCommandRepository deviceCommandRepository;
    private final BatteryCellService batteryCellService;

    @Value("${mqtt.downlinkTopic}")
    private String downlinkTopic;

    @Override
    public void processMessage(Message<String> message) {
        String payload = message.getPayload();
        log.info("Received message in processMessage Object: {}", payload);

        try {
            LoraMessage loraMessage = loraMessageConverter.convert(message);
            log.info("converted lora message: {}", loraMessage);
            long msgLogId = storeMessageLog(loraMessage);

            String devEUI = loraMessage.getDevEUI();
            BatteryCell batteryCell = batteryCellService.findByEui(devEUI);
            BatteryModel batteryModel = batteryCell.getBatteryModel();
            processDevicePayload(devEUI, msgLogId, loraMessage, batteryModel);

            DeviceCommand deviceCommand = deviceCommandService.findByEui(devEUI);
            if (deviceCommand != null) {
                MqttMessageSendDto deviceCommandDto = new MqttMessageSendDto();
                String topic = downlinkTopic.replaceAll("DevEUI", devEUI);
                String downlinkMsg = getDownlinkPayload(deviceCommand.getCommand());
                deviceCommandDto.setData(downlinkMsg);
                String downlinkPayload = jacksonObjectMapper.writeValueAsString(deviceCommandDto);
                myGateWay.sentToMqtt(topic, downlinkPayload);
                updateDeviceCommandType(deviceCommand);
            }

        } catch (Exception e) {
            log.error("Error while processing message: {}", e.getMessage());
//            throw new InvalidLoraMessageFormatException(e);
        }
    }

    private void updateDeviceCommandType(DeviceCommand deviceCommand) {
        DeviceCommandType newCommand = switch (deviceCommand.getCommand()) {
            case PULSE_START_REQUEST -> DeviceCommandType.PULSE_START_PROCESSED;
            case PULSE_STOP_REQUEST -> DeviceCommandType.PULSE_STOP_PROCESSED;
            case CELL_BALANCING_START_REQUEST -> DeviceCommandType.CELL_BALANCING_START_PROCESSED;
            case CELL_BALANCING_STOP_REQUEST -> DeviceCommandType.CELL_BALANCING_STOP_PROCESSED;
            default -> deviceCommand.getCommand();
        };
        deviceCommand.changeCommand(newCommand);
        deviceCommandRepository.save(deviceCommand);
    }

    private void processDevicePayload(String eui, long msgLogId, LoraMessage loraMessage, BatteryModel batteryModel) {
        batteryManagementDataProcessService.processPayload(eui, msgLogId, loraMessage.getData(), batteryModel);
    }

    private Long storeMessageLog(LoraMessage loraMessage) {
        return loraMessageService.saveLoraMessage(loraMessage);
    }

    private String getDownlinkPayload(DeviceCommandType commandType) {
        return switch (commandType) {
            case PULSE_START_REQUEST -> Base64.getEncoder().encodeToString(new byte[]{(byte) 0x41});
            case PULSE_STOP_REQUEST -> Base64.getEncoder().encodeToString(new byte[]{(byte) 0x40});
            case CELL_BALANCING_START_REQUEST -> Base64.getEncoder().encodeToString(new byte[]{(byte) 0x51});
            case CELL_BALANCING_STOP_REQUEST -> Base64.getEncoder().encodeToString(new byte[]{(byte) 0x50});
            default -> throw new IllegalStateException("Unexpected value: " + commandType);
        };
    }
}