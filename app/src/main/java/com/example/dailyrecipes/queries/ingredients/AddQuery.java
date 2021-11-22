package com.example.dailyrecipes.queries.ingredients;

import static com.example.dailyrecipes.queries.Query.Flag.ADD_INGREDIENT;

import com.example.dailyrecipes.queries.Query;
import com.example.dailyrecipes.model.ItemModel;

import org.json.JSONException;
import org.json.JSONObject;

public class AddQuery<I extends ItemModel> extends Query<I, Void> {

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
