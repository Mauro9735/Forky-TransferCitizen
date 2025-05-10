package com.marcianos.transfer_citizen.service;

import com.marcianos.transfer_citizen.dto.RequestTransferCitizenRabbitMq;
import com.marcianos.transfer_citizen.dto.RequestTransferCitizenOperator;
import com.marcianos.transfer_citizen.dto.lotso_documents_microservice.ResponseDocumentMicroservice;
import com.marcianos.transfer_citizen.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class TransferCitizenConsumerService {

    private final RestTemplateService restTemplateService;
    private final UserService userService;
    private static final Logger LOGGER = Logger.getLogger(TransferCitizenConsumerService.class.getName());



    // Service to transfer the citizen

    @RabbitListener(queues = "transfer_citizen_queue")
    public void processRequestTransferCitizen(RequestTransferCitizenRabbitMq requestTransferCitizenRabbitMq) {
        try {
            User user = fetchUserById(requestTransferCitizenRabbitMq.getId());

            HttpStatusCode statusCode = restTemplateService.unregisterCitizen(requestTransferCitizenRabbitMq,user.getDocumentNumber());
            validateStatusCode(statusCode, "Error when deregistering citizen");

            Map<String, ResponseDocumentMicroservice> responseDocuments = restTemplateService.getDocumentById(user.getDocumentNumber());
            List<String> documents = null;
            if(responseDocuments != null && !responseDocuments.isEmpty()){
                documents = extractDocumentUrls(responseDocuments);
            }

            RequestTransferCitizenOperator transferCitizenBody = buildTransferCitizenBody(user, documents);

            HttpStatusCode statusCodeTransfer = restTemplateService.transferCitizen(requestTransferCitizenRabbitMq.getTransferUrl(), transferCitizenBody);
            validateStatusCode(statusCodeTransfer, "Error when transferring citizen");

        }catch (Exception e){
            throw new RuntimeException("Error when processing transfer citizen request: " + e.getMessage(), e);
        }
    }


    private void validateStatusCode(HttpStatusCode statusCode, String errorMessage) {
        if (!statusCode.is2xxSuccessful()) {
            LOGGER.info("Status code not 2xx");
            throw new RuntimeException(errorMessage + ": " + statusCode);
        }
    }

    private List<String> extractDocumentUrls(Map<String, ResponseDocumentMicroservice> responseDocuments) {
       LOGGER.info("Extracting document URLs");
        return responseDocuments.values().stream()
                .map(ResponseDocumentMicroservice::getUrl)
                .toList();
    }

    private User fetchUserById(String id) {
        LOGGER.info("Fetching user with ID");
        User user = userService.getUserById(id);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return user;
    }

    private RequestTransferCitizenOperator buildTransferCitizenBody(User user, List<String> documents) {
        LOGGER.info("Building transfer citizen body");
        RequestTransferCitizenOperator requestTransfer = RequestTransferCitizenOperator.builder()
                .id(Long.parseLong(user.getDocumentNumber()))
                .citizenName(user.getName())
                .citizenEmail(user.getEmail())
                .urlDocuments(new HashMap<>())
                .build();
        if(documents!=null && !documents.isEmpty()){
            LOGGER.info("Setting urls");
            requestTransfer.setUrls(documents);
        }
        return requestTransfer;
    }


}
