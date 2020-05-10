package com.example.demo.model.viewModel;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class SicknessViewModel {
    @NotEmpty
    @NotNull
    private String sicknessName;
    private String sicknessDescription;
    private Integer id;

    public String getSicknessName() {
        return sicknessName;
    }

    public void setSicknessName(String sicknessName) {
        this.sicknessName = sicknessName;
    }

    public String getSicknessDescription() {
        return sicknessDescription;
    }

    public void setSicknessDescription(String sicknessDescription) {
        this.sicknessDescription = sicknessDescription;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
