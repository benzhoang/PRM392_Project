package com.example.prm392_project.data.model.main.cart;

public class ItemCartRequest {
    private String product;  // Nếu là id, giữ String. Nếu là Product, đổi thành Product product;
    private int quantity;
    private String size;
    private double price;

    public ItemCartRequest(String product, int quantity, String size, double price) {
        this.product = product;
        this.quantity = quantity;
        this.size = size;
        this.price = price;
    }

    // Getter và Setter
    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
