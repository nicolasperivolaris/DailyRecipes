package com.example.dailyrecipes.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.dailyrecipes.R;
import com.example.dailyrecipes.queries.RecipeListQuery;
import com.example.dailyrecipes.utils.ConnectionManager;

public class HomeFragment extends Fragment {
    ConnectionManager connection;
    private RecipeListQuery recipeListQuery;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        connection = new ViewModelProvider(requireActivity()).get(ConnectionManager.class);
        recipeListQuery = new RecipeListQuery(() -> setRecipeList());

        Thread t = new Thread(() -> {
            int time = 0; boolean send = false;
            while(time <3000 && !send) {
                if(connection.isReady()) {
                    connection.askServer(recipeListQuery);
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

    private void setRecipeList(){
        requireActivity().runOnUiThread(() -> {
            ListView recipes = requireActivity().findViewById(R.id.recipe_list);
            recipes.setAdapter(new ArrayAdapter<>(requireActivity(), R.layout.show_edit_delete_list, R.id.item_name_tv, recipeListQuery.getData().toArray()));

        });
    }
}
