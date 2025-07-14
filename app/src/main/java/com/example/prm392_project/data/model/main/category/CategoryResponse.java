package com.example.prm392_project.data.model.main.category;

import java.util.List;

public class CategoryResponse {
    private List<Category> data;

    public CategoryResponse(List<Category> data) {
        this.data = data;
    }

    public List<Category> getData() {
        return data;
    }

    public void setData(List<Category> data) {
        this.data = data;
    }
}
