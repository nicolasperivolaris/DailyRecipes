package com.example.dailyrecipes.model;

import com.example.dailyrecipes.queries.ListQuery;
import com.example.dailyrecipes.queries.Query;
import com.example.dailyrecipes.utils.ConnectionManager;
import com.example.dailyrecipes.utils.PositionedMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Observable;

public abstract class QueryableFactory<T extends ItemModel> extends Observable {
    protected ConnectionManager connection;
    protected PositionedMap<T> dataList = new PositionedMap<>();
    protected boolean loaded = false;
    private Thread connectionThread;

    protected QueryableFactory(ConnectionManager connectionManager){
        connection = connectionManager;
        update();
    }

    public void update(){
        connectionThread = connection.make(new ListQuery<T>(this, this::SetList));
    }

    public abstract Query.Flag getFlag();

    public PositionedMap<T> getDataList(){
        return dataList;
    }

    public abstract String[] getNames();

    public abstract T convertJSON(JSONObject jsonObject) throws JSONException;

    private void SetList(Map<Integer, T> list){
        dataList = new PositionedMap<>(list);
        loaded = true;
        setChanged();
        notifyObservers(list);
    }

    public boolean isLoaded() {
        return loaded;
    }

    public Thread getConnectionThread() {
        return connectionThread;
    }

    protected void wait(QueryableFactory<?> factory){
        while(!factory.isLoaded()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(factory.getConnectionThread().isAlive()) {
            try {
                factory.getConnectionThread().join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
