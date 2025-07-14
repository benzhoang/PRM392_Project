package com.example.prm392_project.data.model.main.order;

import com.google.gson.annotations.SerializedName;

public class OrderItemRequest {
    @SerializedName("product_id")
    public int productId;
    @SerializedName("quantity")
    public int quantity;

    public OrderItemRequest(int productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}
