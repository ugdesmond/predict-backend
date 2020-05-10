package com.example.demo.model.viewModel;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class SymptomsSicknessViewModel {

    @NotNull
    private Integer sicknessId;
    @NotEmpty
    @NotNull
    private String symptoms;

    public Integer getSicknessId() {
        return sicknessId;
    }

    public void setSicknessId(Integer sicknessId) {
        this.sicknessId = sicknessId;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }
}
