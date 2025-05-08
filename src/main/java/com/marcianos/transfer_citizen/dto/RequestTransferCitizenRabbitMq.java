package com.marcianos.transfer_citizen.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestTransferCitizenRabbitMq {
    @JsonProperty("id")
    private String id;
    @JsonProperty("operatorId")
    private String operatorId;
    @JsonProperty("operatorName")
    private String operatorName;
    @JsonProperty("transferUrl")
    private String transferUrl;
}
