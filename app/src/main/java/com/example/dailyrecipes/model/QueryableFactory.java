package com.example.dailyrecipes.model;

import com.example.dailyrecipes.queries.Query;
import com.example.dailyrecipes.queries.ingredients.AddQuery;
import com.example.dailyrecipes.queries.ListQuery;
import com.example.dailyrecipes.utils.ConnectionManager;
import com.example.dailyrecipes.utils.PositionedMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public abstract class QueryableFactory<T extends ItemModel> extends Observable {
    private static ConnectionManager connection;
    protected PositionedMap<T> dataList = new PositionedMap<>();

    public void ConnectFactory(ConnectionManager connectionManager){
        connection = connectionManager;
        update();
    }

    public void update(){
        connection.make(new ListQuery<T>(this, this::SetList));
    }

    public abstract Query.Flag getFlag();

    public PositionedMap<T> getDataList(){
        return dataList;
    }

    public abstract String[] getNames();

    public abstract T convertJSON(JSONObject jsonObject) throws JSONException;

    private void SetList(Map<Integer, T> list){
        dataList = new PositionedMap<>(list);
        setChanged();
        notifyObservers(list);
    }

    public void Add(T t){
        connection.make(new AddQuery<>(t, i -> {}));
    }

    public void Update(T T){

    }

    public void Delete(T T){

    }
}
