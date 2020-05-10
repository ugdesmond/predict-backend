package com.example.demo.model.viewModel;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class SymptomsViewModel {
    @NotNull
    @NotEmpty
    private String symptomsName;
    private String description;
    Integer id;


    public String getSymptomsName() {
        return symptomsName;
    }

    public void setSymptomsName(String symptomsName) {
        this.symptomsName = symptomsName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
