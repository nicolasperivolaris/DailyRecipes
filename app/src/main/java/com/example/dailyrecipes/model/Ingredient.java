package com.example.dailyrecipes.model;

public class Ingredient {
    private String name;
    private String unity;
    private float quantity;

    public Ingredient(String name, String unity, float quantity) {
        this.name = name;
        this.unity = unity;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnity() {
        return unity;
    }

    public void setUnity(String unity) {
        this.unity = unity;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }
}
