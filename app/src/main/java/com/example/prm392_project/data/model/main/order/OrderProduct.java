package com.example.prm392_project.data.model.main.order;

import com.google.gson.annotations.SerializedName;

public class OrderProduct {
    @SerializedName("_id")
    private String id;

    @SerializedName("product")
    private String productId;

    @SerializedName("quantity")
    private int quantity;

    public OrderProduct(String id, String productId, int quantity) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
    }

    // Getters and setters...
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
