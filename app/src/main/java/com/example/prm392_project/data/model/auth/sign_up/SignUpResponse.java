package com.example.prm392_project.data.model.auth.sign_up;

public class SignUpResponse {
    private UserSignUp data;
    private String accessToken;

    public SignUpResponse(UserSignUp data, String accessToken) {
        this.data = data;
        this.accessToken = accessToken;
    }

    public SignUpResponse() {}

    // Getters and setters
    public UserSignUp getData() { return data; }
    public void setData(UserSignUp data) { this.data = data; }

    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
}
