package wid.bmsbackend.dto;

import lombok.Builder;
import lombok.Data;
import wid.bmsbackend.entity.BatteryCell;
import wid.bmsbackend.entity.BatteryModel;
import wid.bmsbackend.entity.DeviceCategory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
public class BatteryCellRequest {
    private Long id;
    private String eui;
    private String name;
    private double temp;
    private double current;
    private double voltage;
    private double impedance;
    private Long deviceCategory;
    private LocalDateTime createdDate;
    private int batteryCount;
    private Long batteryModelId;

    public List<BatteryCell> createBatteryCell(DeviceCategory deviceCategory, BatteryModel batteryModel) {
        List<BatteryCell> batteryCells = new ArrayList<>();
        for(int i=0; i<batteryCount; i++){
            String eui = BigDecimal.valueOf(Long.valueOf(getEui(), 16))
                    .add(new BigDecimal(i)).toBigInteger().toString(16);
            String name = "BMS_TD" + (i + 1);
            BatteryCell batteryCell = BatteryCell.builder()
                    .eui(eui)
                    .name(name)
                    .temp(temp)
                    .current(current)
                    .voltage(voltage)
                    .impedance(impedance)
                    .deviceCategory(deviceCategory)
                    .createdDate(createdDate)
                    .batteryModel(batteryModel)
                    .build();
            batteryCells.add(batteryCell);
        }
        return batteryCells;
    }
}
