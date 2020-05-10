package com.example.demo.model.viewModel;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UserViewModel {
    @NotEmpty
    @NotNull
    private String userName;
    @NotEmpty
    @NotNull
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
