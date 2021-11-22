package com.example.dailyrecipes.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.dailyrecipes.utils.PositionedMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Recipe extends ItemModel implements Parcelable{
    private String description;
    private PositionedMap<Ingredient> ingredients = new PositionedMap<>();
    private int multiplier = 1;
    private String imageName;
    private DayOfWeek day;

    Recipe(){
        super(0, "");
        day = null;
    }

    Recipe(int id, String name, String description, PositionedMap<Ingredient> ingredients, int multiplier, String imageName, DayOfWeek day) {
        super(id, name);
        this.ingredients = ingredients;
        this.description = description;
        this.multiplier = multiplier;
        this.imageName = imageName;
        this.day = day;
    }

    Recipe(int id, String name, String description, List<Ingredient> ingredients, int multiplier, String imageName, DayOfWeek day) {
        this(id, name, description, new PositionedMap<>(), multiplier, imageName,day);
        for (Ingredient i:ingredients) {
            this.ingredients.put(i.id, i);
        }
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

            DayOfWeek day;
            int d = in.readInt();
            if(d<0)day = null;
            else day = DayOfWeek.of(d);

            return new Recipe(id, name,description, ingredients, multiplier, imageName, day);
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
        for (Ingredient i : this.ingredients.values())
            ingredients.put(i.convertToJSON());
        result.accumulate("Ingredients", ingredients);
        if(day != null) result.accumulate("Day", day.getValue());
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

    public PositionedMap<Ingredient> getIngredients() {
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
        dest.writeTypedList(new ArrayList<>(ingredients.values()));
        dest.writeInt(multiplier);
        dest.writeString(imageName);
        if(day == null)
            dest.writeInt(-1);
        else
            dest.writeInt(day.getValue());
    }

    public void setIngredients(PositionedMap<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients){
        for (Ingredient i:ingredients) {
            this.ingredients.put(i.id, i);
        }
    }

    public DayOfWeek getDay() {
        return day;
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
    }
}
