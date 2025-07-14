package com.example.prm392_project.data.model.main.order;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreateOrderRequest {
    @SerializedName("user_id")
    public int userId;
    @SerializedName("payment_method")
    public String paymentMethod;
    @SerializedName("orderItems")
    public List<OrderItemRequest> orderItems;

    public CreateOrderRequest(int userId, String paymentMethod, List<OrderItemRequest> orderItems) {
        this.userId = userId;
        this.paymentMethod = paymentMethod;
        this.orderItems = orderItems;
    }
}