package com.example.prm392_project.data.model.main.order;

import com.google.gson.annotations.SerializedName;

public class OrderItemResponse {
    @SerializedName("order_item_id")
    private int orderItemId;

    @SerializedName("order_id")
    private int orderId;

    @SerializedName("product_id")
    private int productId;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("price")
    private String price; // để String đúng response. Nếu muốn double, đổi thành double.

    // Getters & Setters
    public int getOrderItemId() { return orderItemId; }
    public void setOrderItemId(int orderItemId) { this.orderItemId = orderItemId; }
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }
}
