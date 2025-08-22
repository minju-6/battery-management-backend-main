package wid.bmsbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import wid.bmsbackend.entity.DeviceCommandType;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceCommandRequest {
    private List<String> euiList;
    private DeviceCommandType commandType;
}
