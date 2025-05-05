package com.marcianos.transfer_citizen.service;

import com.marcianos.transfer_citizen.dto.RequestTransferCitizen;
import com.marcianos.transfer_citizen.dto.RequestTransferCitizenOperator;
import com.marcianos.transfer_citizen.dto.documents_microservice.ResponseDocumentMicroservice;
import com.marcianos.transfer_citizen.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransferCitizenConsumerService {

    private final RestTemplateService restTemplateService;
    private final UserService userService;

    @RabbitListener(queues = "transfer_citizen_queue")
    public void processRequestTransferCitizen(RequestTransferCitizen requestTransferCitizen) {
        try {
        HttpStatusCode statusCode = restTemplateService.unregisterCitizen(requestTransferCitizen);
            if(statusCode.is2xxSuccessful()){
                ResponseDocumentMicroservice responseDocumentMicroservice = restTemplateService.getDocumentById(String.valueOf(requestTransferCitizen.getCitizenId()));
                List<String> documents = responseDocumentMicroservice.getDocuments().values().stream().toList();
                User user = userService.getUserById(String.valueOf(requestTransferCitizen.getCitizenId()));
                RequestTransferCitizenOperator transferCitizenBody = RequestTransferCitizenOperator.builder().id(requestTransferCitizen.getCitizenId())
                        .citizenName(user.getName())
                        .citizenEmail(user.getEmail())
                        .urlDocuments(new HashMap<>()).build();
                transferCitizenBody.setUrls(documents);
                HttpStatusCode statusCodeTransfer = restTemplateService.transferCitizen(requestTransferCitizen.getTransferUrl(), transferCitizenBody);
                if(statusCodeTransfer.is2xxSuccessful()){
                    userService.deleteUserById(user.getId());
                }
            }
        } catch (Exception e) {
        }
    }

}
