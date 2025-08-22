package wid.bmsbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import wid.bmsbackend.dto.BatteryModelRequest;
import wid.bmsbackend.dto.DecisionStatus;
import wid.bmsbackend.dto.SensingValue;

@Entity
@Table(name = "battery_models")
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BatteryModel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String modelName;
    private double tempHigh;
    private double tempLow;
    private double voltageHigh;
    private double voltageLow;
    private double impedanceThreshold;
    private double currentHigh;
    private double currentLow;

    public void updateBatteryModel(BatteryModelRequest batteryModel) {
        this.modelName = batteryModel.getModelName();
        this.tempHigh = batteryModel.getTempHigh();
        this.tempLow = batteryModel.getTempLow();
        this.voltageHigh = batteryModel.getVoltageHigh();
        this.voltageLow = batteryModel.getVoltageLow();
        this.impedanceThreshold = batteryModel.getImpedanceThreshold();
        this.currentHigh = batteryModel.getCurrentHigh();
        this.currentLow = batteryModel.getCurrentLow();
    }

    public SensingValue.SensingValueWrapper getSensingValue(BatteryCell cell) {
        SensingValue tempSensingValue = getTempSensingValue(cell.getTemp());
        SensingValue voltageSensingValue = getVoltageSensingValue(cell.getVoltage());
        SensingValue impedanceSensingValue = getImpedanceSensingValue(cell.getImpedance());
        SensingValue currentSensingValue = getCurrentSensingValue(cell.getCurrent());

        if (impedanceSensingValue.getStatus() == DecisionStatus.WARNING) {
            return new SensingValue.SensingValueWrapper(
                tempSensingValue,
                voltageSensingValue,
                impedanceSensingValue,
                currentSensingValue,
                DecisionStatus.WARNING
            );
        }
        if (tempSensingValue.getStatus() == DecisionStatus.FAIL ||
                voltageSensingValue.getStatus() == DecisionStatus.FAIL ||
                impedanceSensingValue.getStatus() == DecisionStatus.FAIL ||
                currentSensingValue.getStatus() == DecisionStatus.FAIL) {
            return new SensingValue.SensingValueWrapper(tempSensingValue, voltageSensingValue, impedanceSensingValue, currentSensingValue, DecisionStatus.FAIL);
        }
        return new SensingValue.SensingValueWrapper(
                tempSensingValue,
                voltageSensingValue,
                impedanceSensingValue,
                currentSensingValue,
                DecisionStatus.NORMAL
        );
    }

    public DecisionStatus getDecisionStatusFromSensingValues(double temp, double voltage, double impedance, double current) {
        SensingValue tempSensingValue = getTempSensingValue(temp);
        SensingValue voltageSensingValue = getVoltageSensingValue(voltage);
        SensingValue impedanceSensingValue = getImpedanceSensingValue(impedance);
        SensingValue currentSensingValue = getCurrentSensingValue(current);
        if (impedanceSensingValue.getStatus() == DecisionStatus.WARNING) {
            return DecisionStatus.WARNING;
        }
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
        if (impedance >= impedanceThreshold * 2) {
            return new SensingValue(impedance, DecisionStatus.FAIL, "Impedance is high");
        }
        if (impedance >= impedanceThreshold * 1.5) {
            return new SensingValue(impedance, DecisionStatus.WARNING, "Impedance is low");
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
