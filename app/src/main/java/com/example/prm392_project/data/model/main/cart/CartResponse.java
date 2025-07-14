package com.example.prm392_project.data.model.main.cart;

import java.util.List;

public class CartResponse {
    private List<Cart> data;

    public CartResponse(List<Cart> data) {
        this.data = data;
    }

    public List<Cart> getData() {
        return data;
    }

    public void setData(List<Cart> data) {
        this.data = data;
    }
}
