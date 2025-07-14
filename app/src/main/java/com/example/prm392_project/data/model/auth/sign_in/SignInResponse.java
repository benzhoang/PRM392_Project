package com.example.prm392_project.data.model.auth.sign_in;

import com.google.gson.annotations.SerializedName;

public class SignInResponse {
    @SerializedName("accessToken") // hoặc tên đúng trong JSON
    private String accessToken;
    @SerializedName("_id")
    private String id;
    private String email;
    private String avatar;
    private String firstName;
    private String lastName;
    private String birthDate;
    private String address;
    private String phoneNumber;

    public SignInResponse() {
    }

    public SignInResponse(String id, String email, String avatar, String firstName, String lastName,
                      String birthDate, String address, String phoneNumber) {
        this.id = id;
        this.email = email;
        this.avatar = avatar;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
    public String getAccessToken() {
        return accessToken;
    }
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}
