package com.example.prm392_project.data.model.auth.sign_in;

import com.google.gson.annotations.SerializedName;

public class SignInResponse {
    @SerializedName("token") // hoặc tên đúng trong JSON
    private String accessToken;
    @SerializedName("userId")
    private String id;
    private String email;
    private String userName;

    public SignInResponse() {
    }

    public SignInResponse(String id, String email, String avatar, String userName) {
        this.id = id;
        this.email = email;
        this.userName = userName;
    }
    public String getAccessToken() {
        return accessToken;
    }
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName;}

}
