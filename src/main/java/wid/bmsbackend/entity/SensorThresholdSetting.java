package wid.bmsbackend.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import wid.bmsbackend.dto.DecisionStatus;
import wid.bmsbackend.dto.SensingValue;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
public class SensorThresholdSetting extends BaseEntity {
    private double tempHigh;
    private double tempLow;
    private double voltageHigh;
    private double voltageLow;
    private double impedanceHigh;
    private double impedanceLow;
    private double currentHigh;
    private double currentLow;

    public void updateThreshold(double tempHigh, double tempLow,
                                double voltageHigh, double voltageLow,
                                double impedanceHigh, double impedanceLow,
                                double currentHigh, double currentLow) {
        this.tempHigh = tempHigh;
        this.tempLow = tempLow;
        this.voltageHigh = voltageHigh;
        this.voltageLow = voltageLow;
        this.impedanceHigh = impedanceHigh;
        this.impedanceLow = impedanceLow;
        this.currentHigh = currentHigh;
        this.currentLow = currentLow;
    }

    public SensingValue.SensingValueWrapper getSensingValue(BatteryCell cell) {
        SensingValue tempSensingValue = getTempSensingValue(cell.getTemp());
        SensingValue voltageSensingValue = getVoltageSensingValue(cell.getVoltage());
        SensingValue impedanceSensingValue = getImpedanceSensingValue(cell.getImpedance());
        SensingValue currentSensingValue = getCurrentSensingValue(cell.getCurrent());
        DecisionStatus status = DecisionStatus.NORMAL;
        if (tempSensingValue.getStatus() == DecisionStatus.FAIL ||
                voltageSensingValue.getStatus() == DecisionStatus.FAIL ||
                impedanceSensingValue.getStatus() == DecisionStatus.FAIL ||
                currentSensingValue.getStatus() == DecisionStatus.FAIL) {
            status = DecisionStatus.FAIL;
        }
        return new SensingValue.SensingValueWrapper(
                tempSensingValue,
                voltageSensingValue,
                impedanceSensingValue,
                currentSensingValue,
                status
        );
    }

    public DecisionStatus getDecisionStatusFromSensingValues(double temp, double voltage, double impedance, double current) {
        SensingValue tempSensingValue = getTempSensingValue(temp);
        SensingValue voltageSensingValue = getVoltageSensingValue(voltage);
        SensingValue impedanceSensingValue = getImpedanceSensingValue(impedance);
        SensingValue currentSensingValue = getCurrentSensingValue(current);
        if (tempSensingValue.getStatus() == DecisionStatus.FAIL ||
                voltageSensingValue.getStatus() == DecisionStatus.FAIL ||
                impedanceSensingValue.getStatus() == DecisionStatus.FAIL ||
                currentSensingValue.getStatus() == DecisionStatus.FAIL) {
            return DecisionStatus.FAIL;
        }
        return DecisionStatus.NORMAL;
    }

    public SensingValue getTempSensingValue(double temp) {
        if (temp > tempHigh) {
            return new SensingValue(temp, DecisionStatus.FAIL, "Temperature is high");
        }
        return new SensingValue(temp, DecisionStatus.NORMAL, "");
    }

    public SensingValue getCurrentSensingValue(double current) {
        if (current > currentHigh) {
            return new SensingValue(current, DecisionStatus.FAIL, "Current is high");
        }
        if (current < currentLow) {
            return new SensingValue(current, DecisionStatus.FAIL, "Current is low");
        }
        return new SensingValue(current, DecisionStatus.NORMAL, "");
    }

    private SensingValue getImpedanceSensingValue(double impedance) {
        if (impedance > impedanceHigh) {
            return new SensingValue(impedance, DecisionStatus.FAIL, "Impedance is high");
        }
        if (impedance < impedanceLow) {
            return new SensingValue(impedance, DecisionStatus.FAIL, "Impedance is low");
        }
        return new SensingValue(impedance, DecisionStatus.NORMAL, "");
    }

    private SensingValue getVoltageSensingValue(double voltage) {
        if (voltage > voltageHigh) {
            return new SensingValue(voltage, DecisionStatus.FAIL, "Voltage is high");
        }
        if (voltage < voltageLow) {
            return new SensingValue(voltage, DecisionStatus.FAIL, "Voltage is low");
        }
        return new SensingValue(voltage, DecisionStatus.NORMAL, "");
    }
}
