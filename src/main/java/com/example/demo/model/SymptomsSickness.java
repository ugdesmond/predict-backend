package com.example.demo.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "sickness_symptoms")
public class SymptomsSickness implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @ManyToOne(targetEntity = Symptoms.class)
    private Symptoms symptoms;
    @ManyToOne(targetEntity = Sickness.class)
    private Sickness sickness;
    @ManyToOne(targetEntity = User.class)
    private User user;
    private String status;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private Date dateTimeCreated;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Symptoms getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(Symptoms symptoms) {
        this.symptoms = symptoms;
    }

    public Sickness getSickness() {
        return sickness;
    }

    public void setSickness(Sickness sickness) {
        this.sickness = sickness;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDateTimeCreated() {
        return dateTimeCreated;
    }

    public void setDateTimeCreated(Date dateTimeCreated) {
        this.dateTimeCreated = dateTimeCreated;
    }
}
