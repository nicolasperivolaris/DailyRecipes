package com.example.dailyrecipes.utils;

import android.util.Log;

import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.example.dailyrecipes.queries.Query;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

public class ConnectionManager extends ViewModel {

    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private Hashtable<Integer, Query<?>> queries;

    public ConnectionManager(){
        queries = new Hashtable<>();
    }

    public void connect(String ip, int port){
        if(socket != null && !socket.isConnected()) return;
        Thread thread = new Thread(() -> {
            try {
                socket = new Socket(ip, port);
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream());
                while(socket.isConnected()){
                    try{
                        formatResponse();
                    }catch(IOException e){
                        Log.i("ConnectionManager: ", "Connection Lost");
                    }catch (NumberFormatException e){
                        Log.i("ConnectionManager: ", "Data format error");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    private Query<?> formatResponse() throws IOException, NumberFormatException {
        String[] line = input.readLine().split("\t");
        int queryId = Integer.parseInt(line[0]);
        Query<?> q = queries.get(queryId);
        if(q != null && q.getData() == null){
            ArrayList<String> lines = new ArrayList<>(Arrays.asList(line));
            q.setData(lines);
        }
        return q;
    }

    public void askServer(Query query){
        queries.put(query.id, query);
        Thread t = new Thread(() -> {
            boolean done = false;
            while(!done){
                if(socket.isConnected()){
                    query.print(output);
                    done = true;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    public boolean isReady(){
        return socket != null && socket.isConnected();
    }
}
