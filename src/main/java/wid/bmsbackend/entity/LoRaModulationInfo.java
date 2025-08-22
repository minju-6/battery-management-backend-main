package wid.bmsbackend.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class LoRaModulationInfo {
    @JsonProperty("bandwidth")
    private int bandwidth;

    @JsonProperty("spreadingFactor")
    private int spreadingFactor;

    @JsonProperty("codeRate")
    private String codeRate;

    @JsonProperty("polarizationInversion")
    private boolean polarizationInversion;

}
