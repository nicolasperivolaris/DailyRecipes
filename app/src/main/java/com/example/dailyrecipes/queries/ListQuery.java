package com.example.dailyrecipes.queries;

import androidx.annotation.NonNull;

import com.example.dailyrecipes.model.QueryableFactory;
import com.example.dailyrecipes.model.ItemModel;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class ListQuery<T extends ItemModel> extends Query<Void, Map<Integer, T>> {
    private final QueryableFactory<T> factory;

    public ListQuery(QueryableFactory<T> factory, QueryListener<Map<Integer, T>> run) {
        super(run, factory.getFlag());
        this.factory = factory;
    }

    @Override
    protected Map<Integer, T> formatData(String JSON) {
        Map<Integer, T> list = new HashMap<Integer, T>();
        try{
        JSONArray array = new JSONArray(JSON);
        for (int i = 0; i < array.length(); i++) {
            T t = factory.convertJSON(array.getJSONObject(i));
            list.put(t.getId(), t);
        }
        }catch(JSONException e){
            e.printStackTrace();
        }
        return list;
    }

    @NonNull
    @Override
    public String getArg() {
        return "all";
    }
}
