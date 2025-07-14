package com.example.prm392_project.data.model.main.cart;

public class CartRequest {
    private String user;
    private ItemCartRequest items;

    public CartRequest(String user, ItemCartRequest items) {
        this.user = user;
        this.items = items;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public ItemCartRequest getItems() {
        return items;
    }

    public void setItems(ItemCartRequest items) {
        this.items = items;
    }
}
