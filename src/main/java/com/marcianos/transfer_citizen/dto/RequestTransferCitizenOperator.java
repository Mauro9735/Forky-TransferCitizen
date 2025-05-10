package com.marcianos.transfer_citizen.dto;

import lombok.*;


import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RequestTransferCitizenOperator {
    private long id;
    private String citizenName;
    private String citizenEmail;
    private Map<String,String[]> urlDocuments;
    @Builder.Default
    private String  confirmAPI = "https://api.marcianos.me/api/transferCitizenConfirm";

    public void setUrls(List<String> values) {
        for (int i = 0; i < values.size(); i++) {
            urlDocuments.put("url" + (i + 1),new String[]{values.get(i)});
        }
    }

}
