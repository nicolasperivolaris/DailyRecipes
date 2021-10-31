package com.example.dailyrecipes.queries;

import com.example.dailyrecipes.model.Recipe;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class Query<T> {
    public static final int GET_RECIPE_LIST = 0;
    public static final int GET_RECIPE = 1;
    public static final int SET_RECIPE = 2;
    private static int currentId = 0;
    public final int id;
    private final QueryListener listener;
    private T dataSet;

    protected Query(QueryListener callback){
        id = getNewId();
        this.listener = callback;
    }

    public T getData(){
        return this.dataSet;
    }

    public void setData(ArrayList<String> s){
        this.dataSet = formatData(s);
        Thread t = new Thread(() -> listener.dataReceived());
        t.start();
    }

    protected abstract T formatData(ArrayList<String> s);

    public abstract int getFlag();

    public abstract String getArg();

    protected void printData(String s, PrintWriter out){
        out.print(s + "\t");
    }

    protected void printData(int i, PrintWriter out){
        printData(String.valueOf(i), out);
    }

    protected void printData(double d, PrintWriter out){
        throw new RuntimeException("Not implemented");
        //printData(String.valueOf(DecimalFormat.getInstance().), out);
    }

    public void print(PrintWriter out){
        out.print(id + "\t");
        out.print(getFlag() + "\t");
        out.print(getArg() + "\n");
        out.flush();
    }

    protected static int getNewId(){
        return currentId++;
    }

    public interface QueryListener{
        void dataReceived();
    }
}
