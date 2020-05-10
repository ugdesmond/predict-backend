package com.example.demo.model.dto;

import java.util.List;

public class SicknessSymptomDTO {
    Integer id;
    String sickness;
    List<String> symptoms;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSickness() {
        return sickness;
    }

    public void setSickness(String sickness) {
        this.sickness = sickness;
    }

    public List<String> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(List<String> symptoms) {
        this.symptoms = symptoms;
    }
}
