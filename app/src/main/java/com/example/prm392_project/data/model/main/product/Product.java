package com.example.prm392_project.data.model.main.product;

import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("product_id")
    private String id;

    private String name;
    private String description;
    @SerializedName("price")
    private double price; // hoặc double nếu backend trả chắc chắn là số
    private String brand;
    @SerializedName("skin_type")
    private String skinType;
    @SerializedName("image_url")
    private String imageUrl;
    @SerializedName("stock_quantity")
    private int stockQuantity;
    @SerializedName("category_id")
    private String categoryId;
    private String barcode;
    private String createdAt;
    private String updatedAt;

    // --- Getter, Setter, Constructor ---

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }





    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSkinType() {
        return skinType;
    }

    public void setSkinType(String skinType) {
        this.skinType = skinType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Product(String id, String name, String description, double price, String brand, String skinType, String imageUrl,
                   int stockQuantity, String categoryId, String barcode, String createdAt, String updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.brand = brand;
        this.skinType = skinType;
        this.imageUrl = imageUrl;
        this.stockQuantity = stockQuantity;
        this.categoryId = categoryId;
        this.barcode = barcode;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Các getter và setter ở đây
    // ...
}
