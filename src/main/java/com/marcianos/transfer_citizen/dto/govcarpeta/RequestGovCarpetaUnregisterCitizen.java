package com.marcianos.transfer_citizen.dto.govcarpeta;

import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestGovCarpetaUnregisterCitizen {
    private long id;
    private String operatorId;
    private String operatorName;
}
