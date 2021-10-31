package com.example.dailyrecipes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.dailyrecipes.fragments.HomeFragment;
import com.example.dailyrecipes.queries.RecipeListQuery;
import com.example.dailyrecipes.utils.ConnectionManager;

public class MainActivity extends AppCompatActivity {
    private ConnectionManager connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container_view, HomeFragment.class, savedInstanceState)
                .setReorderingAllowed(true)
                .addToBackStack("name") // name can be null
                .commit();

        connection = new ViewModelProvider(this).get(ConnectionManager.class);
        connection.connect("192.168.2.1", 5500);
    }

    public void onShowClick(View view) {

    }
}