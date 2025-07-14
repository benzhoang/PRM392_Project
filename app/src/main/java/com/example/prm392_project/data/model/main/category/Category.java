package com.example.prm392_project.data.model.main.category;

import com.google.gson.annotations.SerializedName;

public class Category {
    @SerializedName("category_id")
    private String id;
    private String name;

    public Category(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
