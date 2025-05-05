package com.marcianos.transfer_citizen.service;

import com.marcianos.transfer_citizen.dto.RequestTransferCitizen;
import com.marcianos.transfer_citizen.dto.RequestTransferCitizenOperator;
import com.marcianos.transfer_citizen.dto.documents_microservice.ResponseDocumentMicroservice;
import com.marcianos.transfer_citizen.dto.govcarpeta.RequestGovCarpetaUnregisterCitizen;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class RestTemplateService {

    @Value("${service.govcarpeta.unregister.url}")
    private String unregisterCitizenUrl;

    @Value("${service.document.microservice.url}")
    private String documentsUrl;

    private final RestTemplate restTemplate;

    public HttpStatusCode unregisterCitizen(RequestTransferCitizen requestTransferCitizen) {
        RequestGovCarpetaUnregisterCitizen requestGovCarpeta = RequestGovCarpetaUnregisterCitizen.builder()
                .id(requestTransferCitizen.getCitizenId())
                .operatorId(requestTransferCitizen.getOperatorId())
                .operatorName(requestTransferCitizen.getOperatorName())
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RequestGovCarpetaUnregisterCitizen> entity = new HttpEntity<>(requestGovCarpeta, headers);

        ResponseEntity<String> response = restTemplate.exchange(unregisterCitizenUrl, HttpMethod.DELETE, entity, String.class);
       return response.getStatusCode();
    }

    public ResponseDocumentMicroservice getDocumentById(String documentId) {
        String url = documentsUrl+ "folder/" + documentId;
        ResponseEntity<ResponseDocumentMicroservice> response = restTemplate.getForEntity(url, ResponseDocumentMicroservice.class);
        return response.getBody();
    }

    public HttpStatusCode transferCitizen(String url,RequestTransferCitizenOperator requestTransferCitizen) {
        ResponseEntity<?> response = restTemplate.postForEntity(url, requestTransferCitizen, Object.class);
        return response.getStatusCode();
    }

}
