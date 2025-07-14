package com.example.prm392_project.data.model.main.cart;

import com.google.gson.annotations.SerializedName;

public class Cart {
    @SerializedName("_id")
    private String id;

    private String user;

    private ItemCart items;

    public Cart(String id, String user, ItemCart items) {
        this.id = id;
        this.user = user;
        this.items = items;
    }

    // Getter and Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUser() { return user; }
    public void setUser(String user) { this.user = user; }

    public ItemCart getItems() { return items; }
    public void setItems(ItemCart items) { this.items = items; }
}
