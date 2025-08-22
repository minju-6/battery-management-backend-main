package wid.bmsbackend.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Data;

@Data
@Embeddable
public class LoRaTxInfo {
    @JsonProperty("frequency")
    private long frequency;

    @JsonProperty("modulation")
    private String modulation;

    @JsonProperty("loRaModulationInfo")
    @Embedded
    private LoRaModulationInfo loRaModulationInfo;
}
