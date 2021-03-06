package com.example.dailyrecipes.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.dailyrecipes.R;
import com.example.dailyrecipes.utils.PositionedMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Recipe extends ItemModel implements Parcelable{
    public final static Uri noImage = Uri.parse("android.resource://com.example.dailyrecipes/" + R.drawable.nofile);
    private String description;
    private PositionedMap<Ingredient> ingredients = new PositionedMap<>();
    private int multiplier = 1;
    private Uri image;
    private String imageName;
    private boolean imageFromFTP;
    private Day day;


    Recipe(){
        super(0, "");
        image = noImage;
        day = Day.NOT_DAY;
    }

    Recipe(int id, String name, String description, PositionedMap<Ingredient> ingredients, int multiplier, Uri image, String imageName, Day day) {
        super(id, name);
        this.ingredients = ingredients;
        this.description = description;
        this.multiplier = multiplier;
        if(image == null || image.toString().trim().equals(""))
            image = noImage;
        this.image = image;
        this.imageName = imageName;
        this.day = day;
    }

    Recipe(int id, String name, String description, List<Ingredient> ingredients, int multiplier, Uri image, String imageName, Day day) {
        this(id, name, description, new PositionedMap<>(), multiplier, image, imageName, day);
        for (Ingredient i:ingredients) {
            this.ingredients.put(i.getId(), i);
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
            Uri imageUri = Uri.parse(in.readString());
            String imageName = in.readString();
            Day day = in.readParcelable(Day.class.getClassLoader());

            return new Recipe(id, name,description, ingredients, multiplier, imageUri, imageName, day);
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
        if(day != null)
            result.accumulate("Day", day.convertToJSON());
        JSONObject x = result.getJSONObject("Day");
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

    public PositionedMap<Ingredient> getIngredients() {
        return ingredients;
    }

    public Bitmap getNetImage() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("http://192.168.2.8/" + "imageName").openConnection();
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
        dest.writeString(image.toString());
        dest.writeString(imageName);
        if(day == null)
            dest.writeInt(-1);
        else
            dest.writeParcelable(day, Parcelable.CONTENTS_FILE_DESCRIPTOR);
    }

    public void setIngredients(PositionedMap<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients){
        for (Ingredient i:ingredients) {
            this.ingredients.put(i.getId(), i);
        }
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public Uri getImage(){
        Log.i("Recipe : ", image.toString());
        return image;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setImage(Uri image) {
        if(image != null)
            this.image = image;
        else this.image = noImage;
    }

    public boolean isImageFromFTP() {
        return imageFromFTP;
    }

    public void setImageFromFTP(boolean imageFromFTP) {
        this.imageFromFTP = imageFromFTP;
    }
}
