package com.marcianos.transfer_citizen.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@Table(name = "user")
@Entity
public class User {
    @Id
    private String id;
    @Column
    private String documentId;
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
}
