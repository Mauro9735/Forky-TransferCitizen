package com.marcianos.transfer_citizen.service;

import com.marcianos.transfer_citizen.dto.RequestTransferCitizenRabbitMq;
import com.marcianos.transfer_citizen.dto.RequestTransferCitizenOperator;
import com.marcianos.transfer_citizen.dto.lotso_documents_microservice.ResponseDocumentMicroservice;
import com.marcianos.transfer_citizen.dto.mrpotato_adapter.RequestMrpotatoAdapterUnregisterCitizen;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class RestTemplateService {

    private static final String UNREGISTER_CITIZEN_URL = "http://mrpotato-adapter-service.mrpotato-adapter.svc.cluster.local/v1/adapter/unregisterCitizen";
    private static final String GET_DOCUMENTS_URL = "http://lotso-documents-service.documents.svc.cluster.local:8080/v1/documents/folder/";
    private static final Logger LOGGER = Logger.getLogger(RestTemplateService.class.getName());
    private final RestTemplate restTemplate;

    public HttpStatusCode unregisterCitizen(RequestTransferCitizenRabbitMq requestTransferCitizenRabbitMq,String id ) {
        try {
            RequestMrpotatoAdapterUnregisterCitizen requestGovCarpeta = RequestMrpotatoAdapterUnregisterCitizen.builder()
                    .id(id)
                    .operatorId(requestTransferCitizenRabbitMq.getOperatorId())
                    .operatorName(requestTransferCitizenRabbitMq.getOperatorName())
                    .build();

            LOGGER.info("Began Unregister citizen request");
            ResponseEntity<?> response = restTemplate.postForEntity(UNREGISTER_CITIZEN_URL, requestGovCarpeta, Object.class);
            LOGGER.info("Unregister citizen request completed");
            return response.getStatusCode();
        }catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Error when deregistering citizen: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error when deregistering citizen", e);
        }
    }

    public Map<String,ResponseDocumentMicroservice> getDocumentById(String documentId) {
       try {
           String url = GET_DOCUMENTS_URL + documentId;
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
            LOGGER.info("Began Transfer citizen request");
            LOGGER.info("Request: " + requestTransferCitizen.toString());
            LOGGER.info("Request url: " + url);
            var response = restTemplate.postForEntity(url, requestTransferCitizen, String.class);
            LOGGER.info("Response urllllllll: " + response.getBody());
            LOGGER.info("Transfer citizen request completed " + response.getStatusCode() );
            return response.getStatusCode();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.info("Error when transferring citizen HttpClientError: " + e.getMessage());
            throw new RuntimeException("Error when transferring citizen: " + e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.info("Error when transferring citizen exception: " + e.getMessage());
            throw new RuntimeException("Unexpected error when transferring citizen", e);
        }
    }

}
