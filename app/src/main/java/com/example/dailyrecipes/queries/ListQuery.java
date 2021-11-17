package com.example.dailyrecipes.queries;

import androidx.annotation.NonNull;

import com.example.dailyrecipes.model.QueryableFactory;
import com.example.dailyrecipes.queries.Query;
import com.example.dailyrecipes.utils.JSONable;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ListQuery<T extends JSONable<T>> extends Query<Void, List<T>> {
    private final QueryableFactory<T> factory;

    public ListQuery(QueryableFactory<T> factory, QueryListener<List<T>> run) {
        super(run, factory.getFlag());
        this.factory = factory;
    }

    @Override
    protected List<T> formatData(String JSON) throws JSONException {
        List<T> list = new ArrayList<>();
        try{
        JSONArray array = new JSONArray(JSON);
        for (int i = 0; i < array.length(); i++) {
            T t = factory.convertJSON(array.getJSONObject(i));
            list.add(t);
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
