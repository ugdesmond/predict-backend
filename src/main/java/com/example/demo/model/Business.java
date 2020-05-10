package com.example.demo.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;


@Entity
@Table(name = "business")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Business implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "Business name must not be blank")
    @Column(nullable = false, unique = true)
    private String name;

    @NotEmpty(message = "Business mobile no must not be blank")
    @Column(nullable = false)
    private String mobileNo;

    @NotEmpty(message = "Business email must not be null")
    @Column(nullable = false, unique = true)
    private String email;

    @NotEmpty(message = "Business address must not be blank")
    @Column(nullable = false)
    private String address;

    private String businessCode;

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
