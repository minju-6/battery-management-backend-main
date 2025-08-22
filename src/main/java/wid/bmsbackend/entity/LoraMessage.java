package wid.bmsbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import wid.bmsbackend.dto.LoraMessageResponse;

import java.util.List;

@Entity
@Getter
@Table(name = "lora_messages", indexes = {
        @Index(name = "idx_lora_messages_eui_idx", columnList = "devEUI"),
        @Index(name = "idx_lora_message_create_date_idx", columnList = "createdDate")

})
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class LoraMessage extends BaseEntity {
    @JsonProperty("applicationID")
    private String applicationID;

    @JsonProperty("applicationName")
    private String applicationName;

    @JsonProperty("deviceName")
    private String deviceName;

    @JsonProperty("devEUI")
    private String devEUI;

    @JsonProperty("rxInfo")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "lora_rx_info", joinColumns = @JoinColumn(name = "lora_message_id"))
    private List<LoraRxInfo> loraRxInfo;

    @JsonProperty("txInfo")
    @Embedded
    private LoRaTxInfo loRaTxInfo;

    @JsonProperty("adr")
    private boolean adr;

    @JsonProperty("dr")
    private int dr;

    @JsonProperty("fCnt")
    private int fCnt;

    @JsonProperty("fPort")
    private int fPort;

    @JsonProperty("data")
    private String data;

    @JsonProperty("objectJSON")
    private String objectJSON;

    @Override
    public String toString() {
        return "LoraMessage{" +
                "applicationID='" + applicationID + '\'' +
                ", applicationName='" + applicationName + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", devEUI='" + devEUI + '\'' +
                ", rxInfo=" + loraRxInfo +
                ", txInfo=" + loRaTxInfo +
                ", adr=" + adr +
                ", dr=" + dr +
                ", fCnt=" + fCnt +
                ", fPort=" + fPort +
                ", data='" + data + '\'' +
                ", objectJSON='" + objectJSON + '\'' +
                '}';
    }
}
