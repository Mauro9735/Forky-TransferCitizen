package com.marcianos.transfer_citizen.service;

import com.marcianos.transfer_citizen.dto.RequestTransferCitizenRabbitMq;
import com.marcianos.transfer_citizen.dto.RequestTransferCitizenOperator;
import com.marcianos.transfer_citizen.dto.documents_microservice.ResponseDocumentMicroservice;
import com.marcianos.transfer_citizen.dto.govcarpeta.RequestGovCarpetaUnregisterCitizen;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RestTemplateService {

    @Value("${service.mrpotato.unregister.url}")
     String unregisterCitizenUrl;

    @Value("${service.document.microservice.url}")
     String documentsUrl;

    private final RestTemplate restTemplate;

    public HttpStatusCode unregisterCitizen(RequestTransferCitizenRabbitMq requestTransferCitizenRabbitMq) {
        try {
            RequestGovCarpetaUnregisterCitizen requestGovCarpeta = RequestGovCarpetaUnregisterCitizen.builder()
                    .id(requestTransferCitizenRabbitMq.getCitizenId())
                    .operatorId(requestTransferCitizenRabbitMq.getOperatorId())
                    .operatorName(requestTransferCitizenRabbitMq.getOperatorName())
                    .build();

            ResponseEntity<?> response = restTemplate.postForEntity(unregisterCitizenUrl, requestGovCarpeta, Object.class);
            return response.getStatusCode();
        }catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Error when deregistering citizen: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error when deregistering citizen", e);
        }
    }

    public Map<String,ResponseDocumentMicroservice> getDocumentById(String documentId) {
       try {
           String url = documentsUrl + "folder/" + documentId;
           ResponseEntity<Map<String,ResponseDocumentMicroservice>> response =  restTemplate.exchange(
                   url,
                   HttpMethod.GET,
                   null,
                   new ParameterizedTypeReference<Map<String, ResponseDocumentMicroservice>>() {}
           );
           return response.getBody();
       }catch (HttpClientErrorException | HttpServerErrorException e) {
           throw new RuntimeException("Error when obtaining documents: " + e.getMessage(), e);
       }catch (Exception e) {
           throw new RuntimeException("Unexpected error when obtaining documents", e);
       }
    }

    public HttpStatusCode transferCitizen(String url, RequestTransferCitizenOperator requestTransferCitizen) {
        try {
            ResponseEntity<?> response = restTemplate.postForEntity(url, requestTransferCitizen, Object.class);
            return response.getStatusCode();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Error when transferring citizen: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error when transferring citizen", e);
        }
    }

}
