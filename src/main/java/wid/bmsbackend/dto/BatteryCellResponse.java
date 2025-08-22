package wid.bmsbackend.dto;

import lombok.Builder;
import wid.bmsbackend.entity.BatteryCell;
import wid.bmsbackend.entity.DeviceCategory;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record BatteryCellResponse(
        Long id,
        String eui,
        String name,
        Long deviceCategory,
        SensingValue temp,
        SensingValue current,
        SensingValue voltage,
        SensingValue impedance,
        DecisionStatus status,
        LocalDateTime createdDate
) {
    public static BatteryCellResponse of(Long id, String eui, String name,
                                         DeviceCategory deviceCategory, SensingValue temp, SensingValue current,
                                         SensingValue voltage, SensingValue impedance, DecisionStatus status, LocalDateTime createdDate) {

        return BatteryCellResponse.builder()
                .id(id)
                .eui(eui)
                .name(name)
                .deviceCategory(deviceCategory.getId())
                .temp(temp)
                .current(current)
                .voltage(voltage)
                .impedance(impedance)
                .status(status)
                .createdDate(createdDate)
                .build();
    }
}
