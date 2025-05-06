package com.marcianos.transfer_citizen.service;

import com.marcianos.transfer_citizen.dto.RequestTransferCitizenRabbitMq;
import com.marcianos.transfer_citizen.dto.RequestTransferCitizenOperator;
import com.marcianos.transfer_citizen.dto.documents_microservice.ResponseDocumentMicroservice;
import com.marcianos.transfer_citizen.dto.notification.RequestNotificationMessage;
import com.marcianos.transfer_citizen.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransferCitizenConsumerService {

    private final RestTemplateService restTemplateService;
    private final UserService userService;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "transfer_citizen_queue")
    public void processRequestTransferCitizen(RequestTransferCitizenRabbitMq requestTransferCitizenRabbitMq) {
        HttpStatusCode statusCode = restTemplateService.unregisterCitizen(requestTransferCitizenRabbitMq);
        if(statusCode.value() == 200 || statusCode.value()==204){
            Map<String,ResponseDocumentMicroservice> responseDocumentMicroservice = restTemplateService.getDocumentById(String.valueOf(requestTransferCitizenRabbitMq.getCitizenId()));
            List<String> documents =  responseDocumentMicroservice.values().stream()
                       .map(ResponseDocumentMicroservice::getUrl)
                       .toList();
            User user = userService.getUserById(String.valueOf(requestTransferCitizenRabbitMq.getCitizenId()));
            if(user == null){
                throw new RuntimeException("User not found");
            }
            RequestTransferCitizenOperator transferCitizenBody = RequestTransferCitizenOperator.builder().id(requestTransferCitizenRabbitMq.getCitizenId())
                    .citizenName(user.getName())
                    .citizenEmail(user.getEmail())
                    .urlDocuments(new HashMap<>()).build();
            transferCitizenBody.setUrls(documents);
            HttpStatusCode statusCodeTransfer = restTemplateService.transferCitizen(requestTransferCitizenRabbitMq.getTransferUrl(), transferCitizenBody);
            if(statusCodeTransfer.is2xxSuccessful()){
                userService.deleteUserById(user.getId());
                rabbitTemplate.convertAndSend("notifications", RequestNotificationMessage.builder().action("Transfer Well").to_email(user.getEmail()).build());
            }else {
                throw new RuntimeException("Error when transferring citizen: " + statusCodeTransfer);
            }
        }else {
            throw new RuntimeException("Error when deregistering citizen: " + "statusCode");
        }
    }

}
