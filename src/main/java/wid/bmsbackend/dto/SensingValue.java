package wid.bmsbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SensingValue {
    double value;
    DecisionStatus status;
    String errorMessage;

    //    public SensingValue decisionStatus() {
//        if (value > thresholdHigh) {
//            status = DecisionStatus.DANGER;
//            errorMessage = "Value is higher than threshold";
//        } else if (value < thresholdLow) {
//            status = DecisionStatus.DANGER;
//            errorMessage = "Value is lower than threshold";
//        } else {
//            status = DecisionStatus.NORMAL;
//            errorMessage = "Value is normal";
//        }
//        return this;
//    }
    public record SensingValueWrapper(
            SensingValue temp,
            SensingValue voltage,
            SensingValue impedance,
            SensingValue current,
            DecisionStatus status
    ) {

    }
}
