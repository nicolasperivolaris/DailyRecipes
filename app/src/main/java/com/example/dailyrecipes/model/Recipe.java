package com.example.dailyrecipes.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.dailyrecipes.utils.JSONable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Recipe implements Parcelable, JSONable<Recipe> {
    private int id;
    private String name;
    private String description;
    private List<Ingredient> ingredients;
    private int multiplier = 1;
    private String imageName;

    Recipe(){
        ingredients = new ArrayList<>();
    }

    Recipe(int id, String name, String description, List<Ingredient> ingredients, int multiplier, String imageName) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.description = description;
        this.multiplier = multiplier;
        this.imageName = imageName;
    }

    protected Recipe(Parcel in) {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        ingredients = new ArrayList<>();
        imageName = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            int id = in.readInt();
            String name = in.readString();
            String description = in.readString();
            List<Ingredient> ingredients = in.readArrayList(Ingredient.class.getClassLoader());
            int multiplier = in.readInt();
            String imageName = in.readString();
            Recipe recipe = new Recipe(id, name,description, ingredients, multiplier, imageName);
            return recipe;
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public JSONObject convertToJSON() throws JSONException {
        JSONObject result = new JSONObject();
        result.accumulate("Id", id);
        result.accumulate("Name", name);
        result.accumulate("Description", description);
        result.accumulate("Multiplier", multiplier);
        result.accumulate("ImagePath", imageName);
        JSONArray ingredients = new JSONArray();
        for (Ingredient i : this.ingredients) {
            ingredients.put(i.convertToJSON());
        }
        result.accumulate("Ingredients", ingredients);
        return result;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public int getId() {
        return id;
    }

    public Bitmap getImage() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("http://192.168.2.8/" + imageName).openConnection();
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);

        } catch (IOException e) {
            //e.printStackTrace();
            Log.i("Recipe", "no image server");
            return null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeTypedList(ingredients);
        dest.writeInt(multiplier);
        dest.writeString(imageName);
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = new ArrayList<>(ingredients);
    }
}
