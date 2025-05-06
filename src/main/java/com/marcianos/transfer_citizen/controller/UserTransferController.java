package com.marcianos.transfer_citizen.controller;

import com.marcianos.transfer_citizen.dto.RequestTransferCitizenOperator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserTransferController {

    @PostMapping("/transferCitizen")
    public String transferCitizen(@RequestBody RequestTransferCitizenOperator requestTransferCitizenOperator) {
        // Logic to transfer citizen data
        return "Citizen data transferred successfully!";
    }


}
