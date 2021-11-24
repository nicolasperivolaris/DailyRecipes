package com.example.dailyrecipes.model.day;

import com.example.dailyrecipes.model.QueryableFactory;
import com.example.dailyrecipes.queries.Query;

import org.json.JSONException;
import org.json.JSONObject;

public class DayFactory extends QueryableFactory<Day> {

    public static DayFactory instance = new DayFactory();
    private DayFactory(){};

    @Override
    public Query.Flag getFlag() {
        return Query.Flag.GET_DAY_LIST;
    }

    public String[] getNames(){
        String[] DayName = new String[dataList.size()];
        int cp = 0;
        for (Integer i : dataList.ids()) {
            DayName[cp++] = dataList.get(i).getName();
        }
        return DayName;
    }

    public Day convertJSON(JSONObject jsonObject) throws JSONException {
        return new Day(jsonObject.getInt("Id"), jsonObject.getString("Name"));
    }
}