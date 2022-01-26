package com.example.dailyrecipes.utils;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.dailyrecipes.queries.Query;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Hashtable;

public class ConnectionManager extends ViewModel {

    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private boolean sending;
    private final Hashtable<Integer, QueryHandler> queries;

    public ConnectionManager() {
        queries = new Hashtable<>();
    }

    public void connect(String ip, int port) {
        if (socket != null && !socket.isConnected()) return;
        Thread thread = new Thread(() -> {
            try {
                socket = new Socket(ip, port);
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream());
                sending=false;
                while (socket.isConnected() && !socket.isClosed()) {
                    Log.i("Connection Manager", "Listening...");
                    try {
                        formatResponse();
                    } catch (IOException e) {
                        Log.i("ConnectionManager: ", "Connection Lost");
                        e.printStackTrace();
                    } catch (NumberFormatException e) {
                        Log.i("ConnectionManager: ", "Data format error");
                        e.printStackTrace();
                    }
                }
                Log.i("Connection Manager", "Socket close");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    private void formatResponse() throws IOException, NumberFormatException {
        String line = input.readLine();
        Log.i("Connection Manager", "Receiving : " + line);
        String queryIdString = line.split("\t")[0];
        Query<?, ?> q = queries.get(Integer.parseInt(queryIdString)).query;
        Log.i("Connection Manager", "working... " );
        if (q != null) {
            q.setJSONData(line.substring(queryIdString.length()));
            queries.remove(q.id);
        }
    }

    public void askServer(Query<?,?> query) {
        QueryHandler queryHandler = new QueryHandler();
        queryHandler.query = query;
        queryHandler.tsSend = System.currentTimeMillis();
        queries.put(query.id, queryHandler);
        Thread t = new Thread(() -> {
            boolean done = false;
            while (!done) {
                if (socket.isConnected() && !sending) {
                    sending = true;
                    query.print(output);
                    sending = false;
                    done = true;
                    Log.i("Connection Manager", query.id + " done. Flag :" + query.getFlag());
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

    public Thread make(Query<?, ?> query) {
        Thread t = new Thread(() -> {
            int time = 0;
            boolean send = false;
            while (time < 3000 && !send) {
                if (isReady()) {
                    askServer(query);
                    send = true;
                }
                try {
                    Thread.sleep(time += 100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t.start();
        return t;
    }

    public boolean isReady() {
        return socket != null && socket.isConnected();
    }

    private class QueryHandler{
        Query query;
        long tsSend;
        boolean finished;
    }
}
