package com.example.prm392_project.data.model.auth.sign_up;

public class SignUpResponse {
    private UserSignUp user;
    private String message;
    public SignUpResponse() {
    }
    public SignUpResponse(UserSignUp user, String message) {
        this.user = user;
        this.message = message;
    }

    public UserSignUp getUser() {
        return user;
    }

    public void setUser(UserSignUp user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
