package com.example.dailyrecipes.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.dailyrecipes.R;
import com.example.dailyrecipes.queries.RecipeListQuery;
import com.example.dailyrecipes.queries.RecipeQuery;
import com.example.dailyrecipes.utils.ConnectionManager;

public class ShowRecipeFragment extends Fragment {
    ConnectionManager connection;
    private RecipeQuery recipeQuery;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_recipe, container, false);
        connection = new ViewModelProvider(requireActivity()).get(ConnectionManager.class);
        Bundle b = getArguments();
        recipeQuery = new RecipeQuery(b.getInt("id"), () -> setRecipe());

        Thread t = new Thread(() -> {
            int time = 0; boolean send = false;
            while(time <5000 && !send) {
                if(connection.isReady()) {
                    connection.askServer(recipeQuery);
                    send = true;
                }
                try {
                    Thread.sleep(time+= 100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t.start();
        return view;
    }

    private void setRecipe() {
        recipeQuery.getData();
    }
}
