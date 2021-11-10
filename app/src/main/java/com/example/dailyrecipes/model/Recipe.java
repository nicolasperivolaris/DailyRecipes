package com.example.dailyrecipes.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Recipe implements Parcelable {
    private final int id;
    private String name;
    private List<Ingredient> ingredients;
    private int multiplier = 1;
    private final String imageName;

    public Recipe(int id, String name, List<Ingredient> ingredients, String imageName) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.imageName = imageName;
    }

    protected Recipe(Parcel in) {
        id = in.readInt();
        name = in.readString();
        ingredients = new ArrayList<>();
        imageName = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            int id = in.readInt();
            String name = in.readString();
            String imageName = in.readString();
            Recipe recipe = new Recipe(id, name, new ArrayList<>(), imageName);
            return recipe;
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public static Recipe convertJSON(JSONObject jsonObject) throws JSONException {
        int id = jsonObject.getInt("Id");
        String name = jsonObject.getString("Name");
        String imageName = jsonObject.getString("ImagePath");
        ArrayList<Ingredient> ingredients;
        if (jsonObject.isNull("Ingredients")) ingredients = null;
        else {
            ingredients = new ArrayList<>();
            JSONArray array = jsonObject.getJSONArray("Ingredients");
            for (int i = 0; i < array.length(); i++)
                ingredients.add(Ingredient.convertJSON(array.getJSONObject(i)));
        }
        return new Recipe(id, name, ingredients, imageName);
    }

    public JSONObject convertToJSON() throws JSONException {
        JSONObject result = new JSONObject();
        result.accumulate("Id", id);
        result.accumulate("Name", name);
        result.accumulate("ImagePath", imageName);
        result.accumulate("Multiplier", multiplier);
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
        dest.writeString(imageName);
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = new ArrayList<>(ingredients);
    }
}
