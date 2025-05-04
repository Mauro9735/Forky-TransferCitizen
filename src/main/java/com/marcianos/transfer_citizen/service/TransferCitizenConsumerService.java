package com.marcianos.transfer_citizen.service;

import com.marcianos.transfer_citizen.dto.RequestTransferCitizen;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class TransferCitizenConsumerService {

    @RabbitListener(queues = "transfer_citizen_queue")
    public void processRequestTransferCitizen(RequestTransferCitizen requestTransferCitizen) {
        // Process the message received from RabbitMQ
        System.out.println("Received message: " + requestTransferCitizen.getTransferUrl());
        // Here you can add your logic to handle the received message
    }

}
