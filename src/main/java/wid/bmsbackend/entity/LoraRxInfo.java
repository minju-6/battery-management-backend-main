package wid.bmsbackend.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Data;

@Data
@Embeddable
public class LoraRxInfo {
    @JsonProperty("gatewayID")
    private String gatewayID;

    @JsonProperty("time")
    private String time;

    @JsonProperty("timeSinceGPSEpoch")
    private String timeSinceGPSEpoch;

    @JsonProperty("rssi")
    private int rssi;

    @JsonProperty("loRaSNR")
    private float loRaSNR;

    @JsonProperty("channel")
    private int channel;

    @JsonProperty("rfChain")
    private int rfChain;

    @JsonProperty("board")
    private int board;

    @JsonProperty("antenna")
    private int antenna;

    @JsonProperty("location")
    @Embedded
    private DeviceLocation deviceLocation;

    @JsonProperty("fineTimestampType")
    private String fineTimestampType;

    @JsonProperty("context")
    private String context;

    @JsonProperty("uplinkID")
    private String uplinkID;

}
