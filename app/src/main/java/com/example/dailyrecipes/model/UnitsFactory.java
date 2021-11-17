package com.example.dailyrecipes.model;

import com.example.dailyrecipes.queries.Query;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

public class UnitsFactory extends QueryableFactory<Unit> {

    public static UnitsFactory instance = new UnitsFactory();
    private UnitsFactory(){};

    @Override
    public Query.Flag getFlag() {
        return Query.Flag.GET_UNIT_LIST;
    }

    public String[] getNames(){
        String[] UnitsName = new String[dataList.size()];
        for (int i = 0; i< dataList.size(); i++) {
            UnitsName[i] = dataList.get(i).getName();
        }
        return UnitsName;
    }

    public Unit convertJSON(JSONObject jsonObject) throws JSONException {
        int id = jsonObject.getInt("Id");
        String name = jsonObject.getString("Name");
        String symbol = jsonObject.getString("Symbol");
        return new Unit(id, name, symbol);
    }
}
