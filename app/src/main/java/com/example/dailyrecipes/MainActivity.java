package com.example.dailyrecipes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.dailyrecipes.queries.RecipeListQuery;
import com.example.dailyrecipes.utils.ConnectionManager;

public class MainActivity extends AppCompatActivity {
    private ConnectionManager connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connection = new ViewModelProvider(this).get(ConnectionManager.class);
        connection.connect("192.168.2.1", 5500);

    }

}