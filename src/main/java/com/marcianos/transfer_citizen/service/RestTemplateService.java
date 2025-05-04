package com.marcianos.transfer_citizen.service;

import com.marcianos.transfer_citizen.dto.RequestTransferCitizen;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class RestTemplateService {

    private final RestTemplate restTemplate;

    public String unregisterCitizen(RequestTransferCitizen requestTransferCitizen) {
       String url = "https://govcarpeta-apis-4905ff3c005b.herokuapp.com/apis/unregisterCitizen";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RequestTransferCitizen> entity = new HttpEntity<>(requestTransferCitizen, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to unregister citizen: " + response.getStatusCode());
        }
    }
}
