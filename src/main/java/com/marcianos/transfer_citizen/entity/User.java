package com.marcianos.transfer_citizen.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import java.time.LocalDateTime;


@Builder
@Getter
@Setter
@Table(name = "users")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private String id;
    @Column(name = "\"documentType\"")
    private String documentType;
    @Column(name = "\"documentNumber\"")
    private String documentNumber;
    @Column
    private String name;
    @Column
    private String email;
    @Column
    private String phone;
    @Column
    private String address;
    @Column
    private String country;
    @Column
    private String city;
    @Column
    private String department;
    @Column(name="\"createdAt\"")
    private LocalDateTime createdAt;
    @Column(name="\"updatedAt\"")
    private LocalDateTime updatedAt;
}
