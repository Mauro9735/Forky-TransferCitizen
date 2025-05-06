package com.marcianos.transfer_citizen.dto.notification;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestNotificationMessage {
    private String action;
    private String to_email;

}
