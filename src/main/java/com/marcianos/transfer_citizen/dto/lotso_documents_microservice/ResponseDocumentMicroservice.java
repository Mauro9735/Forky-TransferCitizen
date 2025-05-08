
package com.marcianos.transfer_citizen.dto.lotso_documents_microservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseDocumentMicroservice {

        @JsonProperty("url")
        private String url;

        @JsonProperty("firmado")
        private String firmado;

}

