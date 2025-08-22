package wid.bmsbackend.dto;

import lombok.*;
import wid.bmsbackend.entity.BatteryModel;
import wid.bmsbackend.entity.DeviceCategory;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceCategoryResponse {
    private Long id;
    private String name;
    private BatteryModel batteryModel;

    public static DeviceCategoryResponse of(DeviceCategory deviceCategory){
        return DeviceCategoryResponse.builder()
                .id(deviceCategory.getId())
                .name(deviceCategory.getName())
                .batteryModel(deviceCategory.getBatteryModel())
                .build();
    }
}
