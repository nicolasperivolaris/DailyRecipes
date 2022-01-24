package com.example.dailyrecipes.model;

import com.example.dailyrecipes.model.QueryableFactory;
import com.example.dailyrecipes.model.Unit;
import com.example.dailyrecipes.queries.Query;
import com.example.dailyrecipes.utils.ConnectionManager;

import org.json.JSONException;
import org.json.JSONObject;

public class UnitsFactory extends QueryableFactory<Unit> {
    public UnitsFactory(ConnectionManager connectionManager){
        super(connectionManager);
    };

    @Override
    public Query.Flag getFlag() {
        return Query.Flag.GET_UNIT_LIST;
    }

    public String[] getNames(){
        String[] UnitsName = new String[dataList.size()];
        int cp = 0;
        for (Integer i : dataList.ids()) {
            UnitsName[cp++] = dataList.get(i).getName();
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
