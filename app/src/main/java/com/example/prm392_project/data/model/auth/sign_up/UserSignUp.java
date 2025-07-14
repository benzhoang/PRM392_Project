package com.example.prm392_project.data.model.auth.sign_up;

import com.google.gson.annotations.SerializedName;

public class UserSignUp {
    private String email;
    private String password;
    private String name;

    public UserSignUp( String email, String password, String name) {

        this.email = email;
        this.password = password;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
