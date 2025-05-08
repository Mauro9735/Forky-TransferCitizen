package com.marcianos.transfer_citizen.dto.mrpotato_adapter;

import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestMrpotatoAdapterUnregisterCitizen {
    private String id;
    private String operatorId;
    private String operatorName;
}
