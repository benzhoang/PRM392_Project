package com.example.prm392_project.data.model.main.order;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Order {
    @SerializedName("_id")
    private String id;

    @SerializedName("user")
    private String userId;

    @SerializedName("address")
    private String address;

    @SerializedName("email")
    private String email;

    @SerializedName("phoneNumber")
    private String phoneNumber;

    @SerializedName("paymentMethod")
    private String paymentMethod;

    @SerializedName("status")
    private String status;

    @SerializedName("total")
    private int total;

    @SerializedName("products")
    private List<OrderProduct> products;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("updatedAt")
    private String updatedAt;

    public Order(String id, String userId, String address, String email, String phoneNumber,
                 String paymentMethod, String status, int total, List<OrderProduct> products,
                 String createdAt, String updatedAt) {
        this.id = id;
        this.userId = userId;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.total = total;
        this.products = products;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and setters...
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getTotal() { return total; }
    public void setTotal(int total) { this.total = total; }
    public List<OrderProduct> getProducts() { return products; }
    public void setProducts(List<OrderProduct> products) { this.products = products; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}
