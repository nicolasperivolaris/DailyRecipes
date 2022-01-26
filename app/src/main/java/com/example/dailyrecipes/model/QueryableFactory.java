package com.example.dailyrecipes.model;

import android.util.Log;

import com.example.dailyrecipes.queries.ListQuery;
import com.example.dailyrecipes.queries.Query;
import com.example.dailyrecipes.utils.ConnectionManager;
import com.example.dailyrecipes.utils.PositionedMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;
import java.util.Observable;

public abstract class QueryableFactory<T extends ItemModel> extends Observable {
    protected ConnectionManager connection;
    protected PositionedMap<T> dataList = new PositionedMap<>();
    protected boolean loaded = false;
    private Thread connectionThread;

    protected QueryableFactory(ConnectionManager connectionManager){
        connection = connectionManager;
    }

    public void update(){
        loaded = false;
        QueryableFactory instance = this;
        Thread t = new Thread(() -> {
            waitDependencies();
            connectionThread = connection.make(new ListQuery<T>(instance, list -> instance.SetList(list)));
            instance.wait(instance);
            onUpdated();
        });
        t.start();
    }
    protected abstract void onUpdated();
    protected abstract void waitDependencies();

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
        Iterator<Integer> i = list.keySet().iterator();
        while (i.hasNext()) {
            Log.i("QueryableFactory",list.get(i.next()).toString());
        }
        notifyObservers(list);
    }

    public boolean isLoaded() {
        return loaded;
    }

    public Thread getConnectionThread() {
        return connectionThread;
    }

    protected void wait(QueryableFactory<?> factory){
        long tsStart = System.currentTimeMillis();
        while(!factory.isLoaded() && System.currentTimeMillis() - tsStart < 10000) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        /*if(factory.connectionThread.isAlive()) {
            try {
                factory.connectionThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
    }
}
