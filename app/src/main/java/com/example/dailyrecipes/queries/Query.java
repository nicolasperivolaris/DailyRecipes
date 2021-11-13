package com.example.dailyrecipes.queries;

import org.json.JSONException;

import java.io.PrintWriter;

public abstract class Query<Param, Result> {
    public static final int GET_RECIPE_LIST = 0;
    public static final int GET_RECIPE_INGREDIENTS = 1;
    public static final int ADD_INGREDIENT = 2;
    public static final int DELETE_INGREDIENT = 3;
    public static final int GET_SHOPPING_LIST = 4;
    public static final int SAVE_RECIPE = 5;
    public static final int GET_INGREDIENT_LIST = 6;

    private static int currentId = 0;
    public final int id;
    private final QueryListener listener;
    private Param param;
    private Result dataSet;

    protected Query(QueryListener callback) {
        id = getNewId();
        this.listener = callback;
    }

    public Result getData() {
        return this.dataSet;
    }

    public void setJSONData(String data) {
        try {
            this.dataSet = formatData(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Thread t = new Thread(() -> listener.dataReceived(getData()));
        t.start();
    }

    public void setData(Result data) {
        this.dataSet = data;
    }

    public void setParam(Param param) {
        this.param = param;
    }

    public Param getParam() {
        return param;
    }

    /**
     * Format the result of the query
     * @param JSON
     * @return
     * @throws JSONException
     */
    protected abstract Result formatData(String JSON) throws JSONException;

    public abstract int getFlag();

    /**
     * Parameter of the request
     * @return
     */
    public abstract String getArg();

    protected void printData(String s, PrintWriter out) {
        out.print(s + "\t");
    }

    protected void printData(int i, PrintWriter out) {
        printData(String.valueOf(i), out);
    }

    protected void printData(double d, PrintWriter out) {
        throw new RuntimeException("Not implemented");
        //printData(String.valueOf(DecimalFormat.getInstance().), out);
    }

    public void print(PrintWriter out) {
        out.print(id + "\t");
        out.print(getFlag() + "\t");
        out.print(getArg() + "\n");
        out.flush();
    }

    protected static int getNewId() {
        return currentId++;
    }

    public interface QueryListener<Result> {
        void dataReceived(Result result);
    }
}
