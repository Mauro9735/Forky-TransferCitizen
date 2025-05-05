package com.marcianos.transfer_citizen.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestTransferCitizen {
    private long citizenId;
    private String operatorId;
    private String operatorName;
    private String transferUrl;
}
