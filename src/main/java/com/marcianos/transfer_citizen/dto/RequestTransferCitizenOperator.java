package com.marcianos.transfer_citizen.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
public class RequestTransferCitizenOperator {
    private long id;
    private String citizenName;
    private String citizenEmail;
    private Map<String,String[]> urlDocuments;

    public void setUrls(List<String> values) {
        for (int i = 0; i < values.size(); i++) {
            urlDocuments.put("url" + (i + 1),new String[]{values.get(i)});
        }
    }

}
