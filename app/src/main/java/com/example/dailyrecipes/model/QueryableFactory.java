package com.example.dailyrecipes.model;

import android.app.Activity;

import androidx.annotation.UiThread;

import com.example.dailyrecipes.queries.Query;
import com.example.dailyrecipes.queries.ingredients.AddQuery;
import com.example.dailyrecipes.queries.ListQuery;
import com.example.dailyrecipes.utils.ConnectionManager;
import com.example.dailyrecipes.utils.JSONable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;

public abstract class QueryableFactory<T extends JSONable<T>> extends Observable {
    private static ConnectionManager connection;
    protected List<T> dataList = new ArrayList<>();

    public void ConnectFactory(ConnectionManager connectionManager){
        connection = connectionManager;
        update();
    }

    public void update(){
        connection.make(new ListQuery<T>(this, this::SetList));
    }

    public abstract Query.Flag getFlag();

    public List<T> getDataList(){
        return Collections.unmodifiableList(dataList);
    }

    public abstract String[] getNames();

    public abstract T convertJSON(JSONObject jsonObject) throws JSONException;

    private void SetList(List<T> list){
        dataList = list;
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
