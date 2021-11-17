package com.example.dailyrecipes.queries.ingredients;

import static com.example.dailyrecipes.queries.Query.Flag.ADD_INGREDIENT;

import android.os.Parcelable;

import com.example.dailyrecipes.model.Ingredient;
import com.example.dailyrecipes.model.Recipe;
import com.example.dailyrecipes.queries.Query;
import com.example.dailyrecipes.utils.JSONable;

import org.json.JSONException;
import org.json.JSONObject;

public class AddQuery<I extends JSONable<I>> extends Query<I, Void> {

    private I i;

    public AddQuery(I i, QueryListener<I> callback) {
        super(callback,ADD_INGREDIENT);
        this.i = i;
    }

    @Override
    protected Void formatData(String JSON) throws JSONException {
        return null;
    }

    @Override
    public String getArg() {
        try {
            JSONObject toSave = i.convertToJSON();
            return toSave.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "java error";
    }
}
